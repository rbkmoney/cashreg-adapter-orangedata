package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.AgentType;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.DocumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Содержимое документа
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {

    /**
     * Тип документа
     * see {@link DocumentType}
     */
    @JsonProperty("type")
    private Integer type;

    /**
     * Список позиций в чеке
     */
    @JsonProperty("positions")
    private List<Positions> positions;

    /**
     * Параметры закрытия чека
     */
    @JsonProperty("checkClose")
    private CheckClose checkClose;

    /**
     * Телефон или e-mail покупателя
     */
    @JsonProperty("customerContact")
    private String customerContact;

    /**
     * Признак агента, 1057
     * Число от 1 до 127, необязательное поле
     *
     * @see AgentType
     */
    @JsonProperty("agentType")
    private String agentType;

    /**
     * Телефон оператора перевода, 1075
     * Массив строк длиной от 1 до 19 символов, формат +{Ц}, необязательное поле
     */
    @JsonProperty("paymentTransferOperatorPhoneNumbers")
    private String paymentTransferOperatorPhoneNumbers;

    /**
     * Операция платежного агента, 1044
     * Строка длиной от 1 до 24 символов, необязательное поле
     */
    @JsonProperty("paymentAgentOperation")
    private String paymentAgentOperation;

    /**
     * Телефон платежного агента, 1073
     * Массив строк длиной от 1 до 19 символов, формат +{Ц}, необязательное поле
     */
    @JsonProperty("paymentAgentPhoneNumbers")
    private String paymentAgentPhoneNumbers;

    /**
     * Телефон оператора по приему платежей, 1074
     * Массив строк длиной от 1 до 19 символов, формат +{Ц}, необязательное поле
     */
    @JsonProperty("paymentOperatorPhoneNumbers")
    private String paymentOperatorPhoneNumbers;

    /**
     * Наименование оператора перевода, 1026
     * Строка длиной от 1 до 64 символов, необязательное поле
     */
    @JsonProperty("paymentOperatorName")
    private String paymentOperatorName;

    /**
     * Адрес оператора перевода, 1005
     * Строка длиной от 1 до 243 символов, необязательное поле
     */
    @JsonProperty("paymentOperatorAddress")
    private String paymentOperatorAddress;

    /**
     * ИНН оператора перевода, 1016
     * Строка длиной от 10 до 12 символов, формат ЦЦЦЦЦЦЦЦЦЦ, необязательное поле
     */
    @JsonProperty("paymentOperatorINN")
    private String paymentOperatorINN;

    /**
     * Телефон поставщика, 1171
     * Массив строк длиной от 1 до 19 символов, формат +{Ц}, необязательное поле
     */
    @JsonProperty("supplierPhoneNumbers")
    private String supplierPhoneNumbers;

    /**
     * Дополнительный реквизит пользователя, 1084
     * Структура п.2.1.1.5, необязательное поле
     */
    @JsonProperty("additionalUserAttribute")
    private String additionalUserAttribute;

    /**
     * Номер автомата, 1036
     * Строка длиной от 1 до 20 символов, обязательное поле,
     * если группа имеет признак передачи данных автомата,
     * в противном случае не должно передаваться
     */
    @JsonProperty("automatNumber")
    private String automatNumber;

    /**
     * Адрес расчетов, 1009
     * Строка длиной от 1 до 243 символов, обязательное поле,
     * если группа имеет признак передачи данных автомата,
     * в противном случае не должно передаваться
     */
    @JsonProperty("settlementAddress")
    private String settlementAddress;

    /**
     * Место расчетов, 1187
     * Строка длиной от 1 до 243 символов, обязательное поле,
     * если группа имеет признак передачи данных автомата,
     * в противном случае не должно передаваться
     */
    @JsonProperty("settlementPlace")
    private String settlementPlace;

}
