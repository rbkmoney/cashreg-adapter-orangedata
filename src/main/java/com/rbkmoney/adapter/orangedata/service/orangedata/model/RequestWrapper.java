package com.rbkmoney.adapter.orangedata.service.orangedata.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestWrapper<R> {
    private R request;
    private String url;
    private String group;
    private String company;
    private String companyAddress;
    private String privateKey;
    private String key;
}
