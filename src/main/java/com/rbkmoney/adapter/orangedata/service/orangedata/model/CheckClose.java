package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.TaxationSystem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * Параметры закрытия чека
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckClose {

    /**
     * Оплаты
     * see {@link Payments}
     */
    @JsonProperty("payments")
    private List<Payments> payments;

    /**
     * Система налогообложения
     * see {@link TaxationSystem}
     */
    @JsonProperty("taxationSystem")
    private Integer taxationSystem;

}
