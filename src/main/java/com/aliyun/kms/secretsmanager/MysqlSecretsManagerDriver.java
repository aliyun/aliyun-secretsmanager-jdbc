package com.aliyun.kms.secretsmanager;

import com.aliyun.kms.secretsmanager.utils.Constants;
import com.aliyuncs.kms.secretsmanager.client.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.client.service.UserAgentManager;
import com.aliyuncs.utils.StringUtils;

import java.sql.SQLException;


public class MysqlSecretsManagerDriver extends SecretsManagerDriver {

    public static final int LOGIN_FAILED_CODE = 1045;

    static {
        UserAgentManager.registerUserAgent(Constants.MYSQL_DRIVER_JAVA_OF_USER_AGENT, Constants.MYSQL_DRIVER_JAVA_OF_USER_AGENT_PRIORITY, Constants.PROJECT_VERSION);
    }

    protected MysqlSecretsManagerDriver() {
        super();
    }

    public MysqlSecretsManagerDriver(SecretCacheClient secretCacheClient) {
        super(secretCacheClient);
    }

    public MysqlSecretsManagerDriver(SecretCacheClient secretCacheClient, String urlUnique) {
        super(secretCacheClient, urlUnique);
    }

    @Override
    protected String getRealDriverClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver", false, this.getClass().getClassLoader());
            return "com.mysql.cj.jdbc.Driver";
        } catch (ClassNotFoundException e) {
            return "com.mysql.jdbc.Driver";
        }
    }

    @Override
    public String contactUrl(String endpoint, String port, String dbName) {
        String url = "jdbc:mysql://" + endpoint;
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
            return errorCode == LOGIN_FAILED_CODE;
        }
        return false;
    }
}
