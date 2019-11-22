package com.rbkmoney.adapter.orangedata.service.orangedata.constant;

/**
 * Признак агента, 1057. Битовое поле, где номер бита обозначает,
 * что оказывающий услугу покупателю (клиенту) пользователь является:
 * <p>
 * Кассовый чек (БСО) может содержать реквизиты «признак агента» (тег 1057),
 * только если отчет о регистрации и (или) текущий отчет о перерегистрации
 * содержит реквизит «признак агента» (тег 1057), имеющий значение,
 * идентичное значению реквизита «признак агента» (тег 1057) кассового чека.
 */
public class AgentType {
    public static final Integer BANK_PAYING_AGENT = 0;
    public static final Integer BANK_PAYMENT_SUBAGENT = 1;
    public static final Integer PAYMENT_AGENT = 2;
    public static final Integer PAYMENT_SUBAGENT = 3;
    public static final Integer ATTORNEY = 4;
    public static final Integer COMMISSION_AGENT = 5;
    public static final Integer OTHER_AGENT = 6;
}
