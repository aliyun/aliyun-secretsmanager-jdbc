package com.aliyun.kms.secretsmanager.utils;

import com.aliyuncs.utils.StringUtils;

public class UrlUtils {

    private UrlUtils() {
        // do nothing
    }

    public static String parseUrlUnique(String jdbcUrl) {
        if (!StringUtils.isEmpty(jdbcUrl)) {
            String urlKey = "";
            String[] url = jdbcUrl.split("\\?");
            if (url.length > 1) {
                String[] params = url[1].split("&");
                for (String param : params) {
                    if (param.contains("urlUnique=")) {
                        urlKey = param.replaceFirst("urlUnique=", "");
                    }
                }
            }
            if (StringUtils.isEmpty(urlKey)) {
                String[] params = jdbcUrl.split(";");
                for (String param : params) {
                    if (param.contains("urlUnique=")) {
                        urlKey = param.replaceFirst("urlUnique=", "");
                    }
                }
            }
            return urlKey;
        }
        return "";
    }
}
