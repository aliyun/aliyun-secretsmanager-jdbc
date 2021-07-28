package com.aliyun.kms.secretsmanager;


import com.aliyun.kms.secretsmanager.service.SecretsManagerDriverRefreshStrategy;
import com.aliyun.kms.secretsmanager.utils.Constants;
import com.aliyun.kms.secretsmanager.utils.ExceptionUtils;
import com.aliyun.kms.secretsmanager.utils.UrlUtils;
import com.aliyuncs.kms.secretsmanager.client.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.client.SecretCacheClientBuilder;
import com.aliyuncs.kms.secretsmanager.client.exception.CacheSecretException;
import com.aliyuncs.kms.secretsmanager.client.model.CredentialsProperties;
import com.aliyuncs.kms.secretsmanager.client.model.RegionInfo;
import com.aliyuncs.kms.secretsmanager.client.model.SecretInfo;
import com.aliyuncs.kms.secretsmanager.client.service.DefaultSecretManagerClientBuilder;
import com.aliyuncs.kms.secretsmanager.client.utils.CredentialsPropertiesUtils;
import com.aliyuncs.kms.secretsmanager.client.utils.TypeUtils;
import com.aliyuncs.utils.StringUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class SecretsManagerDriver implements Driver {

    private static final String SCHEMA = "secrets-manager";
    private static final int LIMIT_RETRY_TIMES = 3;

    private static CredentialsProperties credentialsProperties;

    protected SecretCacheClient secretCacheClient;

    private String urlUnique;

    static {
        credentialsProperties = CredentialsPropertiesUtils.loadCredentialsProperties(Constants.DEFAULT_CONFIG_NAME);
    }

    public String getUrlUnique() {
        return this.urlUnique;
    }

    public void setUrlUnique(String urlUnique) {
        this.urlUnique = urlUnique;
    }

    public SecretsManagerDriver() {
        this(null);
    }

    public SecretsManagerDriver(SecretCacheClient secretCacheClient) {
        this(secretCacheClient, null);
    }

    public SecretsManagerDriver(SecretCacheClient secretCacheClient, String urlUnique) {
        this.urlUnique = urlUnique;
        try {
            if (secretCacheClient != null) {
                this.secretCacheClient = secretCacheClient;
            } else {
                if (credentialsProperties != null) {
                    long rotationTTL = Long.parseLong(TypeUtils.parseString(credentialsProperties.getSourceProperties().getOrDefault(Constants.REFRESH_SECRET_TTL_KEY, 0)));
                    DefaultSecretManagerClientBuilder clientBuilder = DefaultSecretManagerClientBuilder.standard();
                    for (RegionInfo regionInfo : credentialsProperties.getRegionInfoList()) {
                        clientBuilder.addRegion(regionInfo);
                    }
                    clientBuilder.withCredentialsProvider(credentialsProperties.getProvider());
                    try {
                        this.secretCacheClient = SecretCacheClientBuilder.newCacheClientBuilder(clientBuilder.build()).withRefreshSecretStrategy(new SecretsManagerDriverRefreshStrategy(rotationTTL)).build();
                    } catch (CacheSecretException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.secretCacheClient = SecretCacheClientBuilder.newCacheClientBuilder(DefaultSecretManagerClientBuilder.standard().build()).withRefreshSecretStrategy(new SecretsManagerDriverRefreshStrategy()).build();
                }
            }
        } catch (CacheSecretException e) {
            throw new RuntimeException(e);
        }
        registerDriver(this, getRealDriverClass());
    }

    public static void registerDriver(SecretsManagerDriver driver, String realDriverClass) {
        try {
            DriverManager.registerDriver(driver, () -> shutdown(driver));
            Class.forName(realDriverClass);
        } catch (SQLException sqlException) {
            throw new RuntimeException(String.format("Can not register %s!", driver.getClass().getSimpleName()), sqlException);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Can not find driver %s!", realDriverClass), e);
        }
    }

    private static void shutdown(SecretsManagerDriver driver) {
        try {
            driver.secretCacheClient.close();
        } catch (IOException e) {
            throw new RuntimeException("SecretCacheClient close fail", e);
        }
    }

    private Driver getWrappedDriver() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getName().equals(getRealDriverClass())) {
                return driver;
            }
        }
        throw new IllegalStateException("Can not find wrapped driver");
    }

    protected abstract String getRealDriverClass();

    protected abstract String contactUrl(String host, String port, String dbName);

    protected abstract boolean isAuthError(Exception e);

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }
        String unwrappedUrl = "";
        if (url.startsWith(SCHEMA)) {
            unwrappedUrl = unwrapUrl(url);
        } else {
            try {
                if (!StringUtils.isEmpty(url)) {
                    SecretInfo secretInfo = secretCacheClient.getSecretInfo(url);
                    if (secretInfo != null) {
                        String secretValue = secretInfo.getSecretValue();
                        Properties urlInfo = new Gson().fromJson(secretValue, Properties.class);
                        String endpoint = urlInfo.getProperty(Constants.PROPERTY_NAME_KEY_ENDPOINT);
                        String port = urlInfo.getProperty(Constants.PROPERTY_NAME_KEY_PORT);
                        String dbName = urlInfo.getProperty(Constants.PROPERTY_NAME_KEY_DB_NAME);
                        if (!(StringUtils.isEmpty(endpoint) || StringUtils.isEmpty(port) || StringUtils.isEmpty(dbName))) {
                            unwrappedUrl = contactUrl(endpoint, port, dbName);
                        }
                    }
                }
            } catch (CacheSecretException e) {
                if (!ExceptionUtils.isResourceNotFound(e)) {
                    throw new RuntimeException("Invalid url cache info", e);
                }
            }
        }
        String userKey = info.getProperty(Constants.PROPERTY_NAME_KEY_USER);
        if (info != null && userKey != null) {
            try {
                return connectWithSecret(unwrappedUrl, userKey);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return getWrappedDriver().connect(unwrappedUrl, info);
    }

    private String unwrapUrl(String url) {
        if (StringUtils.isEmpty(url) || !url.startsWith(SCHEMA)) {
            throw new IllegalArgumentException("JDBC URL is invalid");
        }
        return url == null ? null : url.replaceFirst(SCHEMA, "jdbc");
    }

    private Connection connectWithSecret(String url, String userKey) throws SQLException {
        int retryTimes = 0;
        Properties info = new Properties();
        while (retryTimes++ <= LIMIT_RETRY_TIMES) {
            try {
                SecretInfo secretInfo = secretCacheClient.getSecretInfo(userKey);
                if (StringUtils.isEmpty(url)) {
                    if (Constants.SECRET_TYPE_RDS_ROTATE.equalsIgnoreCase(secretInfo.getSecretType())) {
                        String extendedConfig = secretInfo.getExtendedConfig();
                        Map<String, Object> extendedInfo = new Gson().fromJson(extendedConfig, Map.class);
                        if (extendedInfo.containsKey(Constants.PROPERTY_NAME_KEY_CUSTOM_DATA)) {
                            Properties customData = new Gson().fromJson(extendedInfo.get(Constants.PROPERTY_NAME_KEY_CUSTOM_DATA).toString(), Properties.class);
                            String endpoint = customData.getProperty(Constants.PROPERTY_NAME_KEY_ENDPOINT);
                            String port = customData.getProperty(Constants.PROPERTY_NAME_KEY_PORT);
                            String dbName = customData.getProperty(Constants.PROPERTY_NAME_KEY_DB_NAME);
                            if (!(StringUtils.isEmpty(endpoint) || StringUtils.isEmpty(port) || StringUtils.isEmpty(dbName))) {
                                url = contactUrl(endpoint, port, dbName);
                            } else {
                                throw new IllegalArgumentException("Invalid extended config");
                            }
                        } else {
                            throw new IllegalArgumentException("Invalid extended config");
                        }
                    } else {
                        throw new IllegalArgumentException(String.format("SecretsManager driver only supports secret type[%s]", Constants.SECRET_TYPE_RDS_ROTATE));
                    }
                }
                String secretValue = secretInfo.getSecretValue();
                Properties userInfo = new Gson().fromJson(secretValue, Properties.class);
                info.put(Constants.DRIVER_PROPERTIES_KEY_USER, userInfo.getProperty(Constants.SECRET_INFO_KEY_ACCOUNT_NAME));
                info.put(Constants.DRIVER_PROPERTIES_KEY_PASSWORD, userInfo.getProperty(Constants.SECRET_INFO_KEY_ACCOUNT_PASSWORD));
            } catch (CacheSecretException e) {
                throw new RuntimeException("Get user info from cache fail", e);
            }
            try {
                return getWrappedDriver().connect(url, info);
            } catch (SQLException e) {
                if (isAuthError(e)) {
                    try {
                        if (!secretCacheClient.refreshNow(userKey)) {
                            throw e;
                        }
                    } catch (InterruptedException ie) {
                        throw new RuntimeException("Refresh cache fail", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
        throw new SQLException("Connect times limit exceeded");
    }

    private void checkConfigParamNull(String param, String paramName) {
        if (StringUtils.isEmpty(param)) {
            throw new IllegalArgumentException(String.format("Driver config missing required parameters[%s]", paramName));
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        String urlKey = UrlUtils.parseUrlUnique(url);
        if (!StringUtils.isEmpty(this.urlUnique)) {
            if (!urlKey.equals(this.urlUnique)) {
                return false;
            }
        }
        if (url.startsWith(SCHEMA)) {
            return getWrappedDriver().acceptsURL(unwrapUrl(url));
        } else if (url.startsWith("jdbc:")) {
            return false;
        }
        return true;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return getWrappedDriver().getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return getWrappedDriver().getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return getWrappedDriver().getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return getWrappedDriver().jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getWrappedDriver().getParentLogger();
    }
}
