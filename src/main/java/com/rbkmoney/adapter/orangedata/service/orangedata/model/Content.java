package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.DocumentType;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {

    /**
     * Тип документа, see {@link DocumentType}
     */
    @JsonProperty("type")
    private Integer type;

    @JsonProperty("positions")
    private List<Positions> positions;

    @JsonProperty("checkClose")
    private CheckClose checkClose;

    @JsonProperty("customerContact")
    private String customerContact;

    @JsonProperty("agentType")
    private String agentType;

    @JsonProperty("paymentTransferOperatorPhoneNumbers")
    private String paymentTransferOperatorPhoneNumbers;

    @JsonProperty("paymentAgentOperation")
    private String paymentAgentOperation;

    @JsonProperty("paymentAgentPhoneNumbers")
    private String paymentAgentPhoneNumbers;

    @JsonProperty("paymentOperatorPhoneNumbers")
    private String paymentOperatorPhoneNumbers;

    @JsonProperty("paymentOperatorName")
    private String paymentOperatorName;

    @JsonProperty("paymentOperatorAddress")
    private String paymentOperatorAddress;

    @JsonProperty("paymentOperatorINN")
    private String paymentOperatorINN;

    @JsonProperty("supplierPhoneNumbers")
    private String supplierPhoneNumbers;

    @JsonProperty("additionalUserAttribute")
    private String additionalUserAttribute;

    @JsonProperty("automatNumber")
    private String automatNumber;

    @JsonProperty("settlementAddress")
    private String settlementAddress;

    @JsonProperty("settlementPlace")
    private String settlementPlace;

}
