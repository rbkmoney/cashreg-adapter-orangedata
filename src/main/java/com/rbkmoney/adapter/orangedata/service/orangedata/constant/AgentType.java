package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Признак агента, 1057. Битовое поле, где номер бита обозначает,
 * что оказывающий услугу покупателю (клиенту) пользователь является:
 * <p>
 * Кассовый чек (БСО) может содержать реквизиты «признак агента» (тег 1057),
 * только если отчет о регистрации и (или) текущий отчет о перерегистрации
 * содержит реквизит «признак агента» (тег 1057), имеющий значение,
 * идентичное значению реквизита «признак агента» (тег 1057) кассового чека.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AgentType {

    BANK_PAYING_AGENT(0),
    BANK_PAYMENT_SUBAGENT(1),
    PAYMENT_AGENT(2),
    PAYMENT_SUBAGENT(3),
    ATTORNEY(4),
    COMMISSION_AGENT(5),
    OTHER_AGENT(6);

    private final Integer type;
}
