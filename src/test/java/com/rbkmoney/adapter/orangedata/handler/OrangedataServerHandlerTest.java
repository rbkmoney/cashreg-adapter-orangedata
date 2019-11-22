package com.rbkmoney.adapter.orangedata.handler;

import com.rbkmoney.adapter.orangedata.AbstractIntegrationTest;
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
public class OrangedataServerHandlerTest extends AbstractIntegrationTest {

    @MockBean
    public OrangeDataClient client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockUtils.mockClient(client);
    }

    /**
     * Тест на регистрацию чека и проверку статуса его регистрации
     */
    @Test
    public void testRegisterCheck() throws TException {
        CashRegContext cashRegContextCreated = makeCashRegContext();
        // Регистрация чека
        CashRegResult result = handler.register(cashRegContextCreated);
        assertTrue(result.getIntent().isSetSleep());

        // Сохранение состояния из предыдущего результата
        CashRegContext cashRegContextFinished = makeCashRegContext();
        cashRegContextFinished.getSession().setState(result.getState());

        // Запрос финального статуса
        result = handler.register(cashRegContextFinished);
        assertTrue(result.getIntent().isSetFinish());
    }

}