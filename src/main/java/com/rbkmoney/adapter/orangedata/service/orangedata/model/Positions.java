package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Positions {

    /**
     * Количество товара. Десятичное число с точностью до 3 символов после точки
     */
    @JsonProperty("quantity")
    private BigDecimal quantity;

    /**
     * Цена товара с учетом всех скидок и наценок. Десятичное число с точностью до 2 символов после точки
     */
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("tax")
    private Integer tax;

    @JsonProperty("text")
    private String text;

    @JsonProperty("paymentMethodType")
    private Integer paymentMethodType;

    @JsonProperty("paymentSubjectType")
    private Integer paymentSubjectType;

    @JsonProperty("nomenclatureCode")
    private String nomenclatureCode;

    @JsonProperty("supplierInfo")
    private String supplierInfo;

    /**
     * ИНН поставщика, 1226. Строка длиной от 10 до 12 символов,  формат ЦЦЦЦЦЦЦЦЦЦ, необязательное поле
     */
    @JsonProperty("supplierINN")
    private String supplierInn;

}
