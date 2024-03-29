# Configuration File Setting For The AliCould Secrets Manager JDBC Client 

You can use the configuration file named secretsmanager.properties (it exists in the program running directory) to initialize the AliCould Secrets Manager JDBC client:

1. Use access key to access AliCould KMS,you must set the following configuration variables

  ``` 
## the type of access credentials
credentials_type=ak
## the access key id
credentials_access_key_id=#credentials_access_key_id#
## the access key secret
credentials_access_secret=#credentials_access_secret#
cache_client_region_id=[{"regionId":"#regionId#"}]
## the custom refresh time interval of the secret, by default 6 hour, the minimum value is 5 minutes，the time unit is milliseconds
refresh_secret_ttl=21600000
  ```

2. Use STS to access AliCould KMS,you must set the following configuration variables

  ``` 
## the type of access credentials
credentials_type=sts
## the access key id
credentials_access_key_id=#credentials_access_key_id#
## the access key secret
credentials_access_secret=#credentials_access_secret#
## the session name of the RAM role 
credentials_role_session_name=#credentials_role_session_name#
## the ARN of the RAM role 
credentials_role_arn=#credentials_role_arn#
## the policy of the RAM role 
credentials_policy=#credentials_policy#
## the region related to the kms service
cache_client_region_id=[{"regionId":"#regionId#"}]
## the custom refresh time interval of the secret, by default 6 hour, the minimum value is 5 minutes，the time unit is milliseconds
refresh_secret_ttl=21600000
   ```
   
3. Use RAM role to access AliCould KMS,you must set the following configuration variables

  ```  
## the type of access credentials
credentials_type=ram_role
## the access key id
credentials_access_key_id=#credentials_access_key_id#
## the access key secret
credentials_access_secret=#credentials_access_secret#
## the session name of the RAM role 
credentials_role_session_name=#credentials_role_session_name#
## the ARN of the RAM role 
credentials_role_arn=#credentials_role_arn#
## the policy of the RAM role 
credentials_policy=#credentials_policy#
## the region related to the kms service
cache_client_region_id=[{"regionId":"#regionId#"}]
## the custom refresh time interval of the secret, by default 6 hour, the minimum value is 5 minutes，the time unit is milliseconds
refresh_secret_ttl=21600000
   ```

4. Use ECS RAM role to access AliCould KMS,you must set the following configuration variables

  ```
## the type of access credentials
credentials_type=ecs_ram_role
## the name of the ECS RAM role 
credentials_role_name=#credentials_role_name#
## the region related to the kms service
cache_client_region_id=[{"regionId":"#regionId#"}]
## the custom refresh time interval of the secret, by default 6 hour, the minimum value is 5 minutes，the time unit is milliseconds
refresh_secret_ttl=21600000
   ```

5. Use Client Key to access AliCould KMS,you must set the following configuration variables

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
## the custom refresh time interval of the secret, by default 6 hour, the minimum value is 5 minutes，the time unit is milliseconds
## the config item to set 1 hour with the custom refresh time interval of the secret 
refresh_secret_ttl=3600000
   ```

6. Access aliyun dedicated kms,you must set the following configuration variables

```properties
 cache_client_dkms_config_info=[{"regionId":"<your dkms region>","endpoint":"<your dkms endpoint>","passwordFromFilePath":"< your password file absolute path >","clientKeyFile":"<your client key file absolute path>","ignoreSslCerts":false,"caFilePath":"<your CA certificate file absolute path>"}]
```
```
    The details of the configuration item named cache_client_dkms_config_info:
    1. The configuration item named cache_client_dkms_config_info must be configured as a json array, you can configure multiple region instances
    2. regionId:Region id 
    3. endpoint:Domain address of dkms
    4. passwordFromFilePath and passwordFromEnvVariable
      passwordFromFilePath:The client key password configuration is obtained from the file,choose one of the two with passwordFromEnvVariable.
      e.g. while configuring passwordFromFilePath: < your password file absolute path >, you need to configure a file with password written under the configured absolute path
      passwordFromEnvVariable:The client key password configuration is obtained from the environment variable,choose one of the two with passwordFromFilePath.
      e.g. while configuring passwordFromEnvVariable: "your_password_env_variable",
           You need to add your_password_env_variable=< your client key private key password > in env.
    5. clientKeyFile:The absolute path to the client key json file
    6. ignoreSslCerts:If ignore ssl certs (true: Ignores the ssl certificate, false: Validates the ssl certificate)
    7. caFilePath:The absolute path of the CA certificate of the dkms
```