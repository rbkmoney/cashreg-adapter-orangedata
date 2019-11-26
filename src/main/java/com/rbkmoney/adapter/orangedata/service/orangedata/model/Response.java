package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    @JsonProperty("id")
    private String id;

    @JsonProperty("deviceSN")
    private String deviceSN;

    @JsonProperty("deviceRN")
    private String deviceRN;

    @JsonProperty("fsNumber")
    private String fsNumber;

    @JsonProperty("ofdName")
    private String ofdName;

    @JsonProperty("odfWebsite")
    private String odfWebsite;

    @JsonProperty("odfINN")
    private String odfINN;

    @JsonProperty("fnsWebsite")
    private String fnsWebsite;

    @JsonProperty("companyINN")
    private String companyINN;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("documentNumber")
    private Integer documentNumber;

    @JsonProperty("shiftNumber")
    private Integer shiftNumber;

    @JsonProperty("documentIndex")
    private Integer documentIndex;

    @JsonProperty("processedAt")
    private String processedAt;

    @JsonProperty("content")
    private Content content;

    @JsonProperty("change")
    private BigDecimal change;

    @JsonProperty("fp")
    private String fp;

    @JsonProperty("errors")
    private List<String> errors;
}
