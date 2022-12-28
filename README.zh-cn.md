# 

阿里云凭据管家JDBC Java客户端可以使Java开发者快速使用阿里云动态RDS或普通凭据安全获取数据库连接。你可以通过Maven快速使用。
 
 
其他语言版本: [English](README.md), [简体中文](README.zh-cn.md)
 
- [阿里云凭据管家动态RDS凭据介绍](https://help.aliyun.com/document_detail/194269.html)
- [阿里云凭据管家客户端](https://help.aliyun.com/document_detail/190269.html)

## 许可证

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

## 优势

* 提供通用的JDBC驱动，支持简单的数据库连接
* 通过c3p0、dbcp提供数据库连接池支持
* 支持Access Key、STS、RAM Role、ECS RAM Role、Client Key多种访问方式获取动态RDS或普通凭据
* 支持用户自定义的凭据刷新频率

## 使用要求

- 您的凭据必须是动态RDS或普通凭据，建议使用双账号托管的动态RDS或普通凭据
- Java 1.8 或以上版本
- Maven
- 如果您的凭据是普通凭据，您的普通凭据格式是JSON而且要满足以下格式要求：

``` 
{
    "AccountName":"<您的数据库账号用户名>",
    "AccountPassword":"<您的数据库账号密码>"
}
```


## 客户端机制

阿里云凭据管家JDBC Java客户端会依据动态RDS凭据设定的轮转周期定时获取数据库用户名和密码，客户端每次创建数据库链接都会使用最新获取到数据库用户名和密码。

在创建数据库链接极端场景下可能会出现创建链接失败需要用户重试创建链接，相关重试代码如下。

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


## 安装

可以通过Maven的方式在项目中使用阿里云凭据管家JDBC Java客户端。导入方式如下:

```
<dependency>
      <groupId>com.aliyun</groupId>
      <artifactId>aliyun-secretsmanager-jdbc</artifactId>
      <version>1.3.1</version>
</dependency>
```

## 构建

你可以从Github检出代码通过下面的maven命令进行构建。

```
mvn clean install -DskipTests -Dgpg.skip=true
```

## 支持JDBC类型 

阿里云凭据管家JDBC Java客户端支持以下JDBC类型:

- MySQL
- MSSQL
- PostgreSQL
- MariaDB

## 示例代码

阿里云凭据管家JDBC Java客户端支持以下三种方式访问数据库:

* 使用JDBC方式访问数据库
* 使用数据库连接池访问数据库
* 使用数据库开源架中访问数据库

以MySQL数据库、c3p0数据库连接池、数据库开源架构JDBCTemplate为例分别说明:

### 使用JDBC方式访问数据库

#### 通过配置文件(secretsmanager.properties)指定访问KMS([配置文件设置详情](README_config.zh-cn.md))

   ```
## 访问凭据类型
credentials_type=client_key
## 读取client key的解密密码：支持从环境变量或者文件读取
client_key_password_from_env_variable=#your client key private key password environment variable name#
client_key_password_from_file_path=#your client key private key password file path#
## 关联的KMS服务地域
cache_client_region_id=[{"regionId":"#regionId#"}]
   ```

#### 使用JDBC方式访问MySQL代码示例 

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

### 使用数据库连接池c3p0访问数据库

#### 通过配置文件(secretsmanager.properties)指定访问KMS([配置文件设置详情](README_config.zh-cn.md))

   ```
credentials_type=ak
credentials_access_key_id=#credentials_access_key_id#
credentials_access_secret=#credentials_access_secret#
cache_client_region_id=[{"regionId":"#regionId#"}]
   ```

#### 配置c3p0配置文件(c3p0.properties)


  ```
c3p0.user=#your-mysql-secret-name#
c3p0.driverClass=com.aliyun.kms.secretsmanager.MysqlSecretsManagerSimpleDriver
c3p0.jdbcUrl=secrets-manager:mysql://<your-mysql-ip>:<your-mysql-port>/<your-database-name>
  
  ```


### 使用数据库开源架中访问数据库


#### 通过配置文件(secretsmanager.properties)指定访问KMS([配置文件设置详情](README_config.zh-cn.md))

   ```
credentials_type=ak
credentials_access_key_id=#credentials_access_key_id#
credentials_access_secret=#credentials_access_secret#
cache_client_region_id=[{"regionId":"#regionId#"}]
   ```
   
#### 在配置文件Spring配置增加如下配置

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
