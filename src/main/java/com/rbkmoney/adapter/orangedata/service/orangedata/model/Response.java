package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    /**
     * Идентификатор документа
     * Строка от 1 до 32 символов
     */
    @JsonProperty("id")
    private String id;

    /**
     * Заводской номер устройства пробившего чек
     * Строка до 20 символов
     */
    @JsonProperty("deviceSN")
    private String deviceSN;

    /**
     * Регистрационный номер устройства пробившего чек
     * Строка до 20 символов
     */
    @JsonProperty("deviceRN")
    private String deviceRN;

    /**
     * Номер фискального накопителя
     * Строка 16 символов
     */
    @JsonProperty("fsNumber")
    private String fsNumber;

    /**
     * Наименование ОФД
     * Строка до 256 символов
     */
    @JsonProperty("ofdName")
    private String ofdName;

    /**
     * Web-сайт ОФД
     * Строка до 58? символов
     */
    @JsonProperty("odfWebsite")
    private String odfWebsite;

    /**
     * ИНН ОФД
     * Строка 12 символов
     */
    @JsonProperty("odfINN")
    private String odfINN;

    /**
     * Web-сайт ФНС
     * Строка до 256 символов
     */
    @JsonProperty("fnsWebsite")
    private String fnsWebsite;

    /**
     * ИНН пользователя
     * Строка 12 символов
     */
    @JsonProperty("companyINN")
    private String companyINN;

    /**
     * Наименование пользователя
     * Строка до 256 символов
     */
    @JsonProperty("companyName")
    private String companyName;

    /**
     * Номер ФД
     * Число
     */
    @JsonProperty("documentNumber")
    private Integer documentNumber;

    /**
     * Номер смены
     * Число
     */
    @JsonProperty("shiftNumber")
    private Integer shiftNumber;

    /**
     * Номер чека за смену
     * Число
     */
    @JsonProperty("documentIndex")
    private Integer documentIndex;

    /**
     * Время регистрации фискального документа в ФН
     * Время в виде строки в формате ISO8601
     */
    @JsonProperty("processedAt")
    private String processedAt;

    /**
     * Содержимое документа
     * Структура п.2.1.1.1
     */
    @JsonProperty("content")
    private Content content;

    /**
     * Сдача
     * Десятичное число с точностью до 2 символов после точки
     */
    @JsonProperty("change")
    private BigDecimal change;

    /**
     * Фискальный признак
     * Строка 10 символов
     */
    @JsonProperty("fp")
    private String fp;

    /**
     * Массив ошибок валидации запроса
     * Массив строк
     */
    @JsonProperty("errors")
    private List<String> errors;
}
