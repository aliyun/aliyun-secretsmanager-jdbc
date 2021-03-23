package com.aliyun.kms.secretsmanager;

public class PostgreSQLSecretManagerSimpleDriver extends PostgreSQLSecretsManagerDriver {

    static {
        PostgreSQLSecretManagerSimpleDriver driver = new PostgreSQLSecretManagerSimpleDriver();
        registerDriver(driver, driver.getRealDriverClass());
    }

    public PostgreSQLSecretManagerSimpleDriver() {
        super();
    }
}