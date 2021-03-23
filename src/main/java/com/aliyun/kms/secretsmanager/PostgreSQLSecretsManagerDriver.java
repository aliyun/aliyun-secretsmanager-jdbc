package com.aliyun.kms.secretsmanager;

import com.aliyun.kms.secretsmanager.utils.Constants;
import com.aliyuncs.kms.secretsmanager.client.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.client.service.UserAgentManager;
import com.aliyuncs.utils.StringUtils;

import java.sql.SQLException;


public class PostgreSQLSecretsManagerDriver extends SecretsManagerDriver {
    public static final String ACCESS_DENIED_FOR_USER_USING_PASSWORD_TO_DATABASE = "28P01";

    static {
        UserAgentManager.registerUserAgent(Constants.POSTGRE_SQL_DRIVER_JAVA_OF_USER_AGENT, Constants.POSTGRESQL_DRIVER_JAVA_OF_USER_AGENT_PRIORITY, Constants.PROJECT_VERSION);
    }

    protected PostgreSQLSecretsManagerDriver() {
        super();
    }

    public PostgreSQLSecretsManagerDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    public PostgreSQLSecretsManagerDriver(SecretCacheClient secretCacheClient, String urlUnique) {
        super(secretCacheClient, urlUnique);
    }

    @Override
    protected String getRealDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String contactUrl(String endpoint, String port, String dbName) {
        String url = "jdbc:postgresql://" + endpoint;
        if (!StringUtils.isEmpty(port)) {
            url += ":" + port;
        }
        if (!StringUtils.isEmpty(dbName)) {
            url += "/" + dbName;
        }
        return url;
    }

    @Override
    protected boolean isAuthError(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqle = (SQLException) e;
            String sqlState = sqle.getSQLState();
            return sqlState.equals(ACCESS_DENIED_FOR_USER_USING_PASSWORD_TO_DATABASE);
        }
        return false;
    }


}