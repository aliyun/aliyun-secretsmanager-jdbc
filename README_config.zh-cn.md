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
## 用户自定义的刷新凭据, 默认为6小时，最小值为5分钟，单位为毫秒
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
## 用户自定义的刷新凭据, 默认为6小时，最小值为5分钟，单位为毫秒
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
## 用户自定义的刷新凭据, 默认为6小时，最小值为5分钟，单位为毫秒
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
## 用户自定义的刷新凭据, 默认为6小时，最小值为5分钟，单位为毫秒
refresh_secret_ttl=21600000
   ```
