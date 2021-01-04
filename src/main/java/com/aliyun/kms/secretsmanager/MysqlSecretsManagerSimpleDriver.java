package com.aliyun.kms.secretsmanager;

public class MysqlSecretsManagerSimpleDriver extends MysqlSecretsManagerDriver {

    static {
        MysqlSecretsManagerSimpleDriver driver = new MysqlSecretsManagerSimpleDriver();
        registerDriver(driver, driver.getRealDriverClass());
    }

    public MysqlSecretsManagerSimpleDriver() {
        super();
    }

}
