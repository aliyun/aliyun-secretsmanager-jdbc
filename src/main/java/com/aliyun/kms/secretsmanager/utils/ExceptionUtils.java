package com.aliyun.kms.secretsmanager.utils;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.kms.secretsmanager.client.exception.CacheSecretException;

public class ExceptionUtils {

    private static final String FORBIDDEN_RESOURCE_NOT_FOUND_ERROR_CODE = "Forbidden.ResourceNotFound";

    private ExceptionUtils() {
        // do nothing
    }

    public static boolean isResourceNotFound(CacheSecretException e) {
        if (e.getCause() instanceof ClientException && ((ClientException) e.getCause()).getErrCode().equals(FORBIDDEN_RESOURCE_NOT_FOUND_ERROR_CODE)) {
            return true;
        }
        return false;
    }
}
