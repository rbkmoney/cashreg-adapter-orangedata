package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Group {

    DEFAULT("Main");

    private final String value;
}
