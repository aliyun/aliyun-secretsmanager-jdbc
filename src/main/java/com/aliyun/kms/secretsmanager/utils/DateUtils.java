package com.aliyun.kms.secretsmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtils {

    public static final String TIMEZONE_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateUtils() {
        // do nothing
    }

    public static Long parseDate(String dateStr, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(dateStr).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
