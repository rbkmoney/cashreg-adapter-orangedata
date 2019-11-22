package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentType {

    //Приход
    public static final int INCOME = 1;
    //Возврат прихода
    public static final int INCOME_REFUND = 2;
    //Расход
    public static final int COST = 3;
    //Возврат расхода
    public static final int COST_REFUND = 4;

}
