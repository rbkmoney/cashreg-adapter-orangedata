package com.rbkmoney.adapter.orangedata.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.converter.ip.ConverterIp;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataApi;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.signer.AsymSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrangeDataClientConfiguration {

    @Bean
    OrangeDataClient orangeDataClient(ObjectMapper objectMapper, RestTemplate restTemplate, ConverterIp converterIp) {
        return new OrangeDataClient(new OrangeDataApi(new AsymSigner(), restTemplate, objectMapper, converterIp));
    }

}
