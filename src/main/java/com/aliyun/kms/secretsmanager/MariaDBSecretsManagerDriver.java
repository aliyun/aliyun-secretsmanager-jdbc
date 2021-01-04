package com.aliyun.kms.secretsmanager;

import com.aliyun.kms.secretsmanager.utils.Constants;
import com.aliyuncs.kms.secretsmanager.client.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.client.service.UserAgentManager;
import com.aliyuncs.utils.StringUtils;

import java.sql.SQLException;


public class MariaDBSecretsManagerDriver extends SecretsManagerDriver {

    public static final int ACCESS_DENIED_FOR_USER_USING_PASSWORD_TO_DATABASE = 1045;

    static {
        UserAgentManager.registerUserAgent(Constants.MARIA_DB_DRIVER_JAVA_OF_USER_AGENT, Constants.MARIA_DB_DRIVER_JAVA_OF_USER_AGENT_PRIORITY, Constants.PROJECT_VERSION);
    }

    protected MariaDBSecretsManagerDriver() {
        super();
    }

    public MariaDBSecretsManagerDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    public MariaDBSecretsManagerDriver(SecretCacheClient secretCacheClient, String urlUnique) {
        super(secretCacheClient, urlUnique);
    }

    @Override
    protected String getRealDriverClass() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public String contactUrl(String endpoint, String port, String dbName) {
        String url = "jdbc:mariadb://" + endpoint;
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
            int errorCode = sqle.getErrorCode();
            return errorCode == ACCESS_DENIED_FOR_USER_USING_PASSWORD_TO_DATABASE;
        }
        return false;
    }


}
