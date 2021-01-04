package com.aliyun.kms.secretsmanager;

public class MssqlSecretsManagerSimpleDriver extends MssqlSecretsManagerDriver {

    static {
        MssqlSecretsManagerSimpleDriver driver = new MssqlSecretsManagerSimpleDriver();
        registerDriver(driver, driver.getRealDriverClass());
    }

    public MssqlSecretsManagerSimpleDriver() {
        super();
    }

}
