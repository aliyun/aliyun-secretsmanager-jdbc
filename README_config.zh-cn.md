# 阿里云凭据管家JDBC客户端配置文件设置 

通过配置文件secretsmanager.properties(在程序运行目录下)初始化阿里云凭据管家JDBC客户端：

1. 采用阿里云AccessKey作为访问鉴权方式

  ```
## 访问凭据类型
credentials_type=ak
## Access Key Id
credentials_access_key_id=#credentials_access_key_id#
## Access Key Secret
credentials_access_secret=#credentials_access_secret#
## 关联的KMS服务地域
cache_client_region_id=[{"regionId":"#regionId#"}]
## 用户自定义的刷新频率, 默认为6小时，最小值为5分钟，单位为毫秒
refresh_secret_ttl=21600000
   ```

2. 采用阿里云STS作为访问鉴权方式

  ```
## 访问凭据类型
credentials_type=sts
## Access Key Id
credentials_access_key_id=#credentials_access_key_id#
## Access Key Secret
credentials_access_secret=#credentials_access_secret#
## 访问凭据Session名称
credentials_role_session_name=#credentials_role_session_name#
## RAM Role ARN
credentials_role_arn=#credentials_role_arn#
## 访问凭据Policy
credentials_policy=#credentials_policy#
## 关联的KMS服务地域
cache_client_region_id=[{"regionId":"#regionId#"}]
## 用户自定义的刷新频率, 默认为6小时，最小值为5分钟，单位为毫秒
refresh_secret_ttl=21600000
   ```
   
3. 采用阿里云Ram Role作为访问鉴权方式

  ```
## 访问凭据类型
credentials_type=ram_role
## Access Key Id
credentials_access_key_id=#credentials_access_key_id#
## Access Key Secret
credentials_access_secret=#credentials_access_secret#
## 访问凭据Session名称
credentials_role_session_name=#credentials_role_session_name#
## RAM Role ARN
credentials_role_arn=#credentials_role_arn#
## 访问凭据Policy
credentials_policy=#credentials_policy#
## 关联的KMS服务地域
cache_client_region_id=[{"regionId":"#regionId#"}]
## 用户自定义的刷新频率, 默认为6小时，最小值为5分钟，单位为毫秒
refresh_secret_ttl=21600000
   ```

4. 采用阿里云ECS Ram Role作为访问鉴权方式

  ```
## 访问凭据类型
credentials_type=ecs_ram_role
## ECS RAM Role名称
credentials_role_name=#credentials_role_name#
## 关联的KMS服务地域
cache_client_region_id=[{"regionId":"#regionId#"}]
## 用户自定义的刷新频率, 默认为6小时，最小值为5分钟，单位为毫秒
refresh_secret_ttl=21600000
   ```

5. 采用阿里云Client Key作为访问鉴权方式

  ```
## 访问凭据类型
credentials_type=client_key
## 读取client key的解密密码：支持从环境变量或者文件读取
client_key_password_from_env_variable=#your client key private key password environment variable name#
client_key_password_from_file_path=#your client key private key password file path#
## 关联的KMS服务地域
cache_client_region_id=[{"regionId":"#regionId#"}]
## 用户自定义的刷新频率, 默认为6小时，最小值为5分钟，单位为毫秒
## 下面的配置将凭据刷新频率设定为1小时
refresh_secret_ttl=3600000
   ```

6. 访问专属kms服务:
```properties
 cache_client_dkms_config_info=[{"regionId":"<your dkms region>","endpoint":"<your dkms endpoint>","passwordFromFilePath":"< your password file absolute path >","clientKeyFile":"<your client key file absolute path>","ignoreSslCerts":false,"caFilePath":"<your CA certificate file absolute path>"}]
```
```
    cache_client_dkms_config_info配置项说明:
    1. cache_client_dkms_config_info配置项为json数组，支持配置多个region实例
    2. regionId:地域Id
    3. endpoint:专属kms的域名地址
    4. passwordFromFilePath和passwordFromEnvVariable
       passwordFromFilePath:client key密码配置从文件中获取，与passwordFromEnvVariable二选一
       例:当配置passwordFromFilePath:<你的client key密码文件所在的绝对路径>,需在配置的绝对路径下配置写有password的文件
       passwordFromEnvVariable:client key密码配置从环境变量中获取，与passwordFromFilePath二选一
       例:当配置"passwordFromEnvVariable":"your_password_env_variable"时，
         需在环境变量中添加your_password_env_variable=<你的client key对应的密码>
    5. clientKeyFile:client key json文件的绝对路径
    6. ignoreSslCerts:是否忽略ssl证书 (true:忽略ssl证书,false:验证ssl证书)
    7. caFilePath:专属kms的CA证书绝对路径