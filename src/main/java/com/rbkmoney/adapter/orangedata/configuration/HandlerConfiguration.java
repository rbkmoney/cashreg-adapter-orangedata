package com.rbkmoney.adapter.orangedata.configuration;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.converter.CashRegAdapterServiceLogDecorator;
import com.rbkmoney.adapter.orangedata.handler.OrangeDataServerHandler;
import com.rbkmoney.damsel.cashreg.provider.CashRegProviderSrv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Bean
    public CashRegProviderSrv.Iface serverHandlerLogDecorator(OrangeDataServerHandler serverHandler) {
        return new CashRegAdapterServiceLogDecorator(serverHandler);
    }

}
