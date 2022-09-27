package com.aliyun.kms.secretsmanager;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class MysqlSecretsManagerDriverTest {

    @Test
    public void testConnectWithUrl() throws SQLException {
        SecretsManagerDriver driver = new MysqlSecretsManagerDriver();
        Properties properties = new Properties();
        properties.put("user", "#secretName#");
        Connection connect = driver.connect("secrets-manager:mysql://#endpoint#:#port#/#database#?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF8", properties);
        assertNotNull(connect);
    }
}