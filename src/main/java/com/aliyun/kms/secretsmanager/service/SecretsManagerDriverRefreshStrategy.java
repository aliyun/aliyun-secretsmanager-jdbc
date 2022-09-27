package com.aliyun.kms.secretsmanager.service;

import com.aliyun.kms.secretsmanager.utils.DateUtils;
import com.aliyuncs.kms.secretsmanager.client.exception.CacheSecretException;
import com.aliyuncs.kms.secretsmanager.client.model.CacheSecretInfo;
import com.aliyuncs.kms.secretsmanager.client.model.SecretInfo;
import com.aliyuncs.kms.secretsmanager.client.service.RefreshSecretStrategy;
import com.aliyuncs.utils.StringUtils;

import java.io.IOException;

public class SecretsManagerDriverRefreshStrategy implements RefreshSecretStrategy {

    private static final long DEFAULT_ROTATION_INTERVAL = 6 * 3600 * 1000;
    private static final long DEFAULT_MIN_ROTATION_INTERVAL = 5 * 60 * 1000;
    private static final long DEFAULT_DELAY_INTERVAL = 5 * 60 * 1000;

    private long rotationTTL;

    public SecretsManagerDriverRefreshStrategy() {
        this(DEFAULT_ROTATION_INTERVAL);
    }

    public SecretsManagerDriverRefreshStrategy(long rotationTTL) {
        if (rotationTTL <= 0) {
            rotationTTL = DEFAULT_ROTATION_INTERVAL;
        }
        if (rotationTTL <= DEFAULT_MIN_ROTATION_INTERVAL) {
            rotationTTL = DEFAULT_MIN_ROTATION_INTERVAL;
        }
        this.rotationTTL = rotationTTL;
    }

    @Override
    public void init() throws CacheSecretException {
        // do nothing
    }

    @Override
    public long getNextExecuteTime(String secretName, long ttl, long offsetTimestamp) {
        long now = System.currentTimeMillis();
        if (ttl + offsetTimestamp > now) {
            return ttl + offsetTimestamp;
        } else {
            return now + ttl;
        }
    }

    @Override
    public long parseNextExecuteTime(CacheSecretInfo cacheSecretInfo) {
        SecretInfo secretInfo = cacheSecretInfo.getSecretInfo();
        long ttl = parseTTL(secretInfo);
        if (ttl <= 0) {
            return ttl;
        }
        long nextRotationDate = parseNextRotationDate(secretInfo);
        if (nextRotationDate > 0 && nextRotationDate > System.currentTimeMillis()) {
            return nextRotationDate + DEFAULT_DELAY_INTERVAL;
        }
        return getNextExecuteTime(secretInfo.getSecretName(), ttl, cacheSecretInfo.getRefreshTimestamp());
    }

    @Override
    public long parseTTL(SecretInfo secretInfo) {
        String rotationInterval = secretInfo.getRotationInterval();
        if (StringUtils.isEmpty(rotationInterval)) {
            return rotationTTL;
        }
        return Long.parseLong(rotationInterval.replace("s", "")) * 1000;
    }

    private long parseNextRotationDate(SecretInfo secretInfo) {
        String nextRotationDate = secretInfo.getNextRotationDate();
        if (StringUtils.isEmpty(nextRotationDate)) {
            return -1;
        }
        return DateUtils.parseDate(nextRotationDate, DateUtils.TIMEZONE_DATE_PATTERN);
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
