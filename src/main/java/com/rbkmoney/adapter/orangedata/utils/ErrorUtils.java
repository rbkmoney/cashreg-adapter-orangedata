package com.rbkmoney.adapter.orangedata.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtils {
    public static boolean hasError(ResponseEntity<?> entity) {
        return entity.getStatusCode().is4xxClientError();
    }
}
