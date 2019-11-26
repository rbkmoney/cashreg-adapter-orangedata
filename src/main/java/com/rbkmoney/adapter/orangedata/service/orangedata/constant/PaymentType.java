package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {

    CASH(1, "Наличными"),
    E_CASH(2, "Электронными"),
    PREPAYMENT(14, "Предварительная оплата(Аванс)"),
    SUBSEQUENT_PAYMENT(15, "Последующая оплата(Кредит)"),
    OTHER_FORM_OF_PAYMENT(16, "(БСО) встречным предоставлением");

    private final Integer code;
    private final String message;

    public static PaymentType valueOf(Integer code) {
        for (PaymentType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No matching for [" + code + "]");
    }

}
