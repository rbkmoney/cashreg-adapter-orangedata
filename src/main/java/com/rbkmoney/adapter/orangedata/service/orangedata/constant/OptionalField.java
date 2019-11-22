package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OptionalField {

    PAYMENT_TYPE("payment_type");

    private final String field;
}
