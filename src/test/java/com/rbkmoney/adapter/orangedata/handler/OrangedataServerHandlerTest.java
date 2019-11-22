package com.rbkmoney.adapter.orangedata.handler;

import com.rbkmoney.adapter.orangedata.IntegrationTest;
import com.rbkmoney.adapter.orangedata.MockUtils;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static junit.framework.TestCase.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrangedataServerHandlerTest extends IntegrationTest {

    @MockBean
    public OrangeDataClient client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockUtils.mockClient(client);
    }

    @Test
    public void testRegister() throws TException {
        CashRegContext cashRegContext = makeCashRegContext();
        CashRegResult result = handler.register(cashRegContext);
        assertTrue(result.getIntent().isSetSleep());

        cashRegContext.getSession().setState(result.getState());

        result = handler.register(cashRegContext);
        assertTrue(result.getIntent().isSetFinish());
    }

}