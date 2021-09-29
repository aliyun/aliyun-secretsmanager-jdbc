# Aliyun Secrets Manager JDBC Client for Java

The Aliyun Secrets Manager JDBC Client for Java enables Java developers to easily connect to databases using dynamic RDS credentials stored in Aliyun Secrets Manager. You can get started in minutes using Maven .
 
 
Read this in other languages: [English](README.md), [简体中文](README.zh-cn.md)
 
- [Aliyun Secrets Manager Managed RDS Credentials Summary](https://help.aliyun.com/document_detail/194269.html)
- [Aliyun Secrets Manager Client](https://help.aliyun.com/document_detail/190269.html)

## License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

## Features

* Provide common JDBC drivers enabling simple database connectivity
* Provides database connection pool support through c3p0, dbcp
* Provides multiple access ways such as Access Key, STS, RAM Role, ECS RAM Role,Client Key to obtain a dynamic RDS credentials
* Provides the custom refresh time interval of the secret

## Requirements

- Your secret must be a dynamic RDS credentials, it is recommended that you use a dynamic RDS credentials with double RDS accounts
- Java 1.8 or later
- Maven

## Client Mechanism

Aliyun Secrets Manager JDBC client for Java periodically obtains the RDS account and password according to the dynamic RDS credentials. The client creates a database connection with the latest RDS user name and password every times.

It is the extreme scenario that  failure may occur to create database connection.You need to try to create the database connection again. The sample codes are as follows。

  ```Java
import com.aliyun.kms.secretsmanager.MysqlSecretsManagerSimpleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SecretManagerJDBCRetrySample {

    public static void main(String[] args) {
        Connection connect = null;
        try {
            Class.forName("com.aliyun.kms.secretsmanager.MysqlSecretsManagerSimpleDriver");
            connect = DriverManager.getConnection("secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>", "#your-mysql-secret-name#", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlSecretsManagerSimpleDriver.LOGIN_FAILED_CODE) {
                try {
                    // sleep to wait for refresh secret value
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
                try {
                    connect = DriverManager.getConnection("secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>", "#your-mysql-secret-name#", "");
                } catch (SQLException sqlEX) {
                    sqlEX.printStackTrace();
                }
            }
        }
    }
}
  ```


## Install

The recommended way to use the Aliyun Secrets Manager JDBC Client for Java in your project is to consume it from Maven. Import it as follows:

```
<dependency>
      <groupId>com.aliyun</groupId>
      <artifactId>aliyun-secretsmanager-jdbc</artifactId>
      <version>1.0.9</version>
</dependency>
```

## Build

Once you check out the code from GitHub, you can build it using Maven. Use the following command to build:

```
mvn clean install -DskipTests -Dgpg.skip=true
```

## Support JDBC categories 

Aliyun Secrets Manager JDBC client supports the following JDBC categories:

- MySQL
- MSSQL
- PostgreSQL  
- MariaDB

## Sample Code

Aliyun Secrets Manager JDBC client for Java supports following three ways to connect database:

* use JDBC to connect database
* use database connection pool to connect database
* use open source framework about database to connect database

Take MySQL database, c3p0 database connection pool and database open source architecture JDBC template as examples:

### Use JDBC to connect database

#### Use configuration file(secretsmanager.properties) to access KMS([Configuration file setting for details](README_config.md))

   ```
## the type of access credentials
credentials_type=client_key
## you could read the password of client key from environment variable or file
client_key_password_from_env_variable=#your client key private key password environment variable name#
client_key_password_from_file_path=#your client key private key password file path#
## the private key file path of the Client Key
client_key_private_key_path=#your client key private key file path#
## the region related to the kms service
cache_client_region_id=[{"regionId":"#regionId#"}]
   ```

#### MySQL sample code using JDBC to connect database 

  ```Java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SecretManagerJDBCSample {

    public static void main(String[] args) throws Exception {
        Class.forName("com.aliyun.kms.secretsmanager.MysqlSecretsManagerSimpleDriver");
        Connection connect = null;
        try {
            connect = DriverManager.getConnection("secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>", "#your-mysql-secret-name#","");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
  ```

### Use database connection pool c3p0 to connect database

#### Use configuration file(secretsmanager.properties) to access KMS([Configuration file setting for details](README_config.md))

   ```
credentials_type=ak
credentials_access_key_id=#credentials_access_key_id#
credentials_access_secret=#credentials_access_secret#
cache_client_region_id=[{"regionId":"#regionId#"}]
   ```

#### Set c3p0 configuration file(c3p0.properties)


  ```
c3p0.user=#your-mysql-secret-name#
c3p0.driverClass=com.aliyun.kms.secretsmanager.MysqlSecretsManagerSimpleDriver
c3p0.jdbcUrl=secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>
  
 ```


### use open source framework about database to connect database


#### Use configuration file(secretsmanager.properties) to access KMS([Configuration file setting for details](README_config.md))

   ```
credentials_type=ak
credentials_access_key_id=#credentials_access_key_id#
credentials_access_secret=#credentials_access_secret#
cache_client_region_id=[{"regionId":"#regionId#"}]
   ```
   
#### Set the following configuration items in spring config file

  ```
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
        <property name="driverClass" value="com.aliyun.kms.secretsmanager.MysqlSecretsManagerSimpleDriver" />
        <property name="user" value="#your-mysql-secret-name#" />
        <property name="jdbcUrl" value="secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>" />
        <property name="maxPoolSize" value="500" />
        <property name="minPoolSize" value="5" />
        <property name="initialPoolSize" value="20" />
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate" >
        <property name="dataSource" ref="dataSource" />
    </bean>
  ```
