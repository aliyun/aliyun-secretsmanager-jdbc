package com.aliyun.kms.secretsmanager;

import com.aliyun.kms.secretsmanager.service.SecretsManagerDriverRefreshStrategy;
import com.aliyuncs.kms.secretsmanager.cacheclient.SecretCacheClient;
import com.aliyuncs.kms.secretsmanager.cacheclient.SecretCacheClientBuilder;
import com.aliyuncs.kms.secretsmanager.cacheclient.cache.FileCacheSecretStoreStrategy;
import com.aliyuncs.kms.secretsmanager.cacheclient.exception.CacheSecretException;
import com.aliyuncs.kms.secretsmanager.cacheclient.service.DefaultSecretManagerClientBuilder;
import com.aliyuncs.kms.secretsmanager.cacheclient.service.FullJitterBackoffStrategy;
import com.aliyuncs.kms.secretsmanager.cacheclient.utils.CacheClientConstant;
import com.aliyuncs.kms.secretsmanager.cacheclient.utils.CredentialsProviderUtils;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

/**
 * Unit test for simple App.
 */
public class MssqlSecretsManagerDriverTest {


    @Test
    public void testConnectWithUrlCustomCacheClient() throws SQLException, CacheSecretException, IOException {
        String secretName = "MssqlRdsTest";
        SecretCacheClient client = SecretCacheClientBuilder.newCacheClientBuilder(DefaultSecretManagerClientBuilder.standard().withCredentialsProvider(CredentialsProviderUtils.withAccessKey("<your-access-key>", "<your-secret>"))
                .withRegion("cn-hangzhou").withBackoffStrategy(new FullJitterBackoffStrategy(3, 2000, 10000)).build())
                .withCacheSecretStrategy(new FileCacheSecretStoreStrategy("secrets", true, "1234abcd")).withRefreshSecretStrategy(new SecretsManagerDriverRefreshStrategy()).withSecretTTL(secretName, 60 * 1000l).withCacheStage(CacheClientConstant.STAGE_ACS_CURRENT).build();
        SecretsManagerDriver driver = new MssqlSecretsManagerDriver(client);
        Properties properties = new Properties();
        properties.put("user", secretName);
        Connection connect = driver.connect("secrets-manager:sqlserver://<your-db-url>:3433;DatabaseName=test_data_base", properties);
        assertNotNull(connect);
    }
}
