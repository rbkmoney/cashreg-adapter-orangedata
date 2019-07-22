package com.rbkmoney.adapter.orangedata.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.orangedata.configuration.properties.OrangeDataProperties;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataApi;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.signer.AsymSigner;
import com.rbkmoney.adapter.orangedata.utils.ConverterIp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(OrangeDataProperties.class)
public class OrangeDataClientconfiguration {

    @Bean
    //@RequestScope
    OrangeDataClient orangeDataClient(
            ObjectMapper objectMapper,
            RestTemplate restTemplate,
            OrangeDataProperties orangeDataProperties
    ) {
        OrangeDataApi api = new OrangeDataApi(new AsymSigner(), restTemplate, objectMapper, new ConverterIp(), orangeDataProperties);
        return new OrangeDataClient(api, orangeDataProperties);
    }

}
