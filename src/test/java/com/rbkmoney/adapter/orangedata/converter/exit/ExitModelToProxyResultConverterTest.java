package com.rbkmoney.adapter.orangedata.converter.exit;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.orangedata.AbstractIntegrationTest;
import com.rbkmoney.adapter.orangedata.TestData;
import com.rbkmoney.adapter.orangedata.converter.entry.CtxToEntryModelConverter;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import com.rbkmoney.damsel.cashreg.type.Debit;
import com.rbkmoney.damsel.cashreg.type.Type;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.Assert.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ExitModelToProxyResultConverterTest extends AbstractIntegrationTest {

    @Autowired
    private CtxToEntryModelConverter ctxConverter;

    @Autowired
    private ExitModelToProxyResultConverter exitConverter;

    // Проверка конвертера по маппингу ExitStateModel на CashRegResult
    @Test
    public void testExitModelToProxyResultConverter() {
        EntryStateModel entryStateModel = ctxConverter.convert(makeCashRegContext(Type.debit(new Debit())));
        ExitStateModel exitStateModel = ExitStateModel.builder()
                .adapterContext(new AdapterState())
                .cashRegId(TestData.CASHREG_ID)
                .entryStateModel(entryStateModel)
                .build();

        // Проверка, что без состояния вернется поллинг статуса
        CashRegResult regResult = exitConverter.convert(exitStateModel);
        assertTrue("CashRegResult status of a intent isn't sleep", regResult.getIntent().isSetSleep());

        ExitStateModel exitStateModelFailure = ExitStateModel.builder()
                .adapterContext(new AdapterState())
                .cashRegId(TestData.CASHREG_ID)
                .entryStateModel(entryStateModel)
                .errorCode("code")
                .errorMessage("message")
                .build();

        // Проверка, что без состояния вернется поллинг статуса
        CashRegResult regResultFailure = exitConverter.convert(exitStateModelFailure);
        assertTrue("CashRegResult status of a intent isn't failure", regResultFailure.getIntent().getFinish().getStatus().isSetFailure());
    }

}
