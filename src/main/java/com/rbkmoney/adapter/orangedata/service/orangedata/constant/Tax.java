package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tax {

    VAT_18(1, "ставка НДС 18%"),
    VAT_20(1, "ставка НДС 20%"),
    VAT_10(2, "ставка НДС 10%"),
    VAT_118(3, "ставка НДС расч. 18/118"),
    VAT_120(3, "ставка НДС расч. 20/120"),
    VAT_110(4, "ставка НДС расч. 10/110"),
    VAT_0(5, "ставка НДС 0%"),
    NONE(6, "НДС не облагается");

    private final Integer code;
    private final String message;

    public static Tax valueOf(Integer code) {
        for (Tax value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No matching for [" + code + "]");
    }
}
