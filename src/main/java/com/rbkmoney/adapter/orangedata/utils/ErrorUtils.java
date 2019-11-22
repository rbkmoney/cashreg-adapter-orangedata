package com.rbkmoney.adapter.orangedata.utils;

import org.springframework.http.ResponseEntity;

public class ErrorUtils {
    public static boolean hasError(ResponseEntity<?> entity) {
        return entity.getStatusCode().is4xxClientError();
    }
}
