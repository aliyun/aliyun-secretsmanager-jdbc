package com.aliyun.kms.secretsmanager.utils;

public interface Constants {
    String PROJECT_VERSION = "1.3.4";
    String PROPERTY_NAME_KEY_CUSTOM_DATA = "CustomData";
    String PROPERTY_NAME_KEY_DB_NAME = "DBName";
    String PROPERTY_NAME_KEY_ENDPOINT = "Endpoint";
    String PROPERTY_NAME_KEY_PORT = "Port";
    String PROPERTY_NAME_KEY_USER = "user";
    String SECRET_INFO_KEY_ACCOUNT_NAME = "AccountName";
    String SECRET_INFO_KEY_ACCOUNT_PASSWORD = "AccountPassword";
    String DRIVER_PROPERTIES_KEY_USER = "user";
    String DRIVER_PROPERTIES_KEY_PASSWORD = "password";
    String MYSQL_DRIVER_JAVA_OF_USER_AGENT = "alibabacloud-mysqldriver-java";
    int MYSQL_DRIVER_JAVA_OF_USER_AGENT_PRIORITY = 20;
    String MSSQL_DRIVER_JAVA_OF_USER_AGENT = "alibabacloud-mssqldriver-java";
    int MSSQL_DRIVER_JAVA_OF_USER_AGENT_PRIORITY = 15;
    String MARIA_DB_DRIVER_JAVA_OF_USER_AGENT = "alibabacloud-mariadbsqldriver-java";
    int MARIA_DB_DRIVER_JAVA_OF_USER_AGENT_PRIORITY = 5;
    String POSTGRE_SQL_DRIVER_JAVA_OF_USER_AGENT = "alibabacloud-postgresqldriver-java";
    int POSTGRESQL_DRIVER_JAVA_OF_USER_AGENT_PRIORITY = 10;
    String SECRET_TYPE_RDS_ROTATE = "Rds";
    String REFRESH_SECRET_TTL_KEY = "refresh_secret_ttl";
    String DEFAULT_CONFIG_NAME = "secretsmanager.properties";
}
