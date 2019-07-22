package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.Tax;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Список позиций в чеке
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Positions {

    /**
     * Количество товара
     * Десятичное число с точностью до 3 символов после точки
     */
    @JsonProperty("quantity")
    private BigDecimal quantity;

    /**
     * Цена товара с учетом всех скидок и наценок
     * Десятичное число с точностью до 2 символов после точки
     */
    @JsonProperty("price")
    private BigDecimal price;

    /**
     * Ставка НДС
     * see {@link Tax}
     */
    @JsonProperty("tax")
    private Integer tax;

    /**
     * Текст позиции
     */
    @JsonProperty("text")
    private String text;

    @JsonProperty("paymentMethodType")
    private Integer paymentMethodType;

    @JsonProperty("paymentSubjectType")
    private Integer paymentSubjectType;

    /**
     * Код товарной номенклатуры, 1162
     * Строка содержащая base64 кодированый массив от 1 до 32 байт либо null
     */
    @JsonProperty("nomenclatureCode")
    private String nomenclatureCode;

    /**
     * Данные поставщика, 1224
     * Структура п.2.1.1.6, либо null
     */
    @JsonProperty("supplierInfo")
    private String supplierInfo;

    /**
     * ИНН поставщика, 1226
     * Строка длиной от 10 до 12 символов,
     * формат ЦЦЦЦЦЦЦЦЦЦ, необязательное поле
     */
    @JsonProperty("supplierINN")
    private String supplierInn;

}
