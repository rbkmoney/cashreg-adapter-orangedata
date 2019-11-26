package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.Group;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {

    @JsonProperty("id")
    private String id;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("group")
    private String group = Group.DEFAULT.getValue();

    @JsonProperty("content")
    private Content content;

    @JsonProperty("key")
    private String key;

}
