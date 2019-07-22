package com.rbkmoney.adapter.orangedata.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TODO: in libs
 */
public class DateFormate {

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

}