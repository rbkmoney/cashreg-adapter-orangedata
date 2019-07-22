package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.PaymentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Оплаты
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payments {

    /**
     * Тип оплаты
     * see {@link PaymentType}
     */
    @JsonProperty("type")
    private Integer type;

    @JsonProperty("amount")
    private BigDecimal amount;

}
