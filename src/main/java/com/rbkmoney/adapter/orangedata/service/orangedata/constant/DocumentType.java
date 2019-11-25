package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DocumentType {

    // Приход
    INCOME(1),

    // Возврат прихода
    INCOME_REFUND(2),

    //Расход
    COST(3),

    // Возврат расхода
    COST_REFUND(4);

    private final Integer type;

}
