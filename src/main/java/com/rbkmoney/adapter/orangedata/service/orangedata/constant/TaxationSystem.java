package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaxationSystem {

    GENERAL(0, "Общая, ОСН"),
    SIMPLIFIED_INCOME(1, "Упрощенная доход, УСН доход"),
    SIMPLIFIED_REVENUE_MINUS_CONSUMPTION(2, "Упрощенная доход минус расход, УСН доход - расход"),
    UNIFIED_TAX_ON_IMPUTED_INCOME(3, "Единый налог на вмененный доход, ЕНВД"),
    UNIFIED_AGRICULTURAL_TAX(4, "Единый сельскохозяйственный налог, ЕСН"),
    PATENT_SYSTEM_OF_TAXATION(5, "Патентная система налогообложения, Патент");
    
    private final Integer code;
    private final String message;

    public static TaxationSystem valueOf(Integer code) {
        for (TaxationSystem value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No matching for [" + code + "]");
    }
}
