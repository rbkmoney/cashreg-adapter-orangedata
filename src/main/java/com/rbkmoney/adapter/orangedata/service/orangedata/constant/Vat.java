package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Vat {

    VAT_0("0%", Tax.VAT_0.getCode(), "0% НДС"),
    VAT_10("10%", Tax.VAT_10.getCode(), "10% НДС"),
    VAT_18("18%", Tax.VAT_18.getCode(), "18% НДС"),
    VAT_20("20%", Tax.VAT_20.getCode(), "20% НДС"),
    NO_VAT("null", Tax.NONE.getCode(), "Без НДС"),
    VAT_10_110("10/110", Tax.VAT_110.getCode(), "10/110 НДС"),
    VAT_18_118("18/118", Tax.VAT_118.getCode(), "18/118 НДС"),
    VAT_20_120("20/120", Tax.VAT_120.getCode(), "20/120 НДС");

    private final String codeText;
    private final Integer code;
    private final String message;

    public static Vat codeTextOf(String codeText) {
        for (Vat vat : values()) {
            if (vat.codeText.equals(codeText)) {
                return vat;
            }
        }
        throw new IllegalArgumentException("No matching for [" + codeText + "]");
    }

}
