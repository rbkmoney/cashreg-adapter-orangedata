package com.rbkmoney.adapter.orangedata.handler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.rbkmoney.adapter.orangedata.AbstractIntegrationTest;
import com.rbkmoney.damsel.cashreg.type.Debit;
import com.rbkmoney.damsel.cashreg.type.Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.ResourceAccessException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrangedataServerHandlerTimeoutTest extends AbstractIntegrationTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(9999));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Тест на таймаут, должны получить ResourceAccessException в чистом виде
     */
    @Test(expected = ResourceAccessException.class)
    public void testResourceAccessException() throws TException {
        wireMockRule.stubFor(post((urlEqualTo("/api/v2/documents/")))
                .willReturn(aResponse()
                        .withFixedDelay(10000)
                        .withStatus(200)));
        handler.register(makeCashRegContext(Type.debit(new Debit())));
    }

}