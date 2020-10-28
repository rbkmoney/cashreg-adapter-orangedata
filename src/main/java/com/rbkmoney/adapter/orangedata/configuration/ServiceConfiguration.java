package com.rbkmoney.adapter.orangedata.configuration;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.config.properties.TimerProperties;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.service.IntentService;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.service.IntentServiceImpl;
import com.rbkmoney.error.mapping.ErrorMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public IntentService intentService(ErrorMapping errorMapping, TimerProperties timerProperties) {
        return new IntentServiceImpl(errorMapping, timerProperties);
    }

}
