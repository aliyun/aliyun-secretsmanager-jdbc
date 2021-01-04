package com.aliyun.kms.secretsmanager.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {

    private static final String DEFAULT_CONFIG_NAME = "secretsmanager.properties";

    public static Properties loadDriverConfig() {
        return loadConfig(DEFAULT_CONFIG_NAME);
    }

    private ConfigUtils() {
        // do nothing
    }

    public static Properties loadConfig(String configName) {
        try (InputStream in = ConfigUtils.class.getClassLoader().getResourceAsStream(configName)) {
            if (in == null) {
                return null;
            }
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
