package com.rbkmoney.adapter.orangedata.configuration;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashregAdapterServiceLogDecorator;
import com.rbkmoney.adapter.orangedata.handler.OrangeDataServerHandler;
import com.rbkmoney.damsel.cashreg.adapter.CashregAdapterSrv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public CashregAdapterSrv.Iface serverHandlerLogDecorator(OrangeDataServerHandler serverHandler) {
        return new CashregAdapterServiceLogDecorator(serverHandler);
    }

}
