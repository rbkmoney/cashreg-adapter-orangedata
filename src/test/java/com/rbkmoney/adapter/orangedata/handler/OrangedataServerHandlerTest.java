package com.rbkmoney.adapter.orangedata.handler;

import com.rbkmoney.adapter.orangedata.AbstractIntegrationTest;
import com.rbkmoney.adapter.orangedata.MockUtils;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import com.rbkmoney.damsel.cashreg.type.*;
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
     * Тест на регистрацию чека "Приход" и проверку статуса его регистрации
     */
    @Test
    public void testRegisterDebit() throws TException {
        register(Type.debit(new Debit()));
    }

    /**
     * Тест на регистрацию чека "Расход" и проверку статуса его регистрации
     */
    @Test
    public void testRegisterCredit() throws TException {
        register(Type.credit(new Credit()));
    }

    /**
     * Тест на регистрацию чека "Возврат Расхода" и проверку статуса его регистрации
     */
    @Test
    public void testRegisterRefundCredit() throws TException {
        register(Type.refund_credit(new RefundCredit()));
    }

    /**
     * Тест на регистрацию чека "Возврат Прихода" и проверку статуса его регистрации
     */
    @Test
    public void testRegisterRefundDebit() throws TException {
        register(Type.refund_debit(new RefundDebit()));
    }

    private void register(Type type) throws TException {
        CashRegContext cashRegContextCreated = makeCashRegContext(type);
        // Регистрация чека
        CashRegResult result = handler.register(cashRegContextCreated);
        assertTrue(result.getIntent().isSetSleep());

        // Сохранение состояния из предыдущего результата
        CashRegContext cashRegContextFinished = makeCashRegContext(type);
        cashRegContextFinished.getSession().setState(result.getState());

        // Запрос финального статуса
        result = handler.register(cashRegContextFinished);
        assertTrue(result.getIntent().isSetFinish());
    }

}