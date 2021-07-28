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
## the password of the Client Key
client_key_password=#your client key private key password#
## the private key file path of the Client Key
client_key_private_key_path=#your client key private key file path#
## the region related to the kms service
cache_client_region_id=[{"regionId":"#regionId#"}]
## the custom refresh time interval of the secret, by default 6 hour, the minimum value is 5 minutes，the time unit is milliseconds
refresh_secret_ttl=21600000
   ```
