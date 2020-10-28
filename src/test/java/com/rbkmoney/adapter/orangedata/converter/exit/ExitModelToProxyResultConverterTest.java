package com.rbkmoney.adapter.orangedata.converter.exit;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.common.model.PollingInfo;
import com.rbkmoney.adapter.orangedata.AbstractIntegrationTest;
import com.rbkmoney.adapter.orangedata.TestData;
import com.rbkmoney.adapter.orangedata.converter.entry.CtxToEntryModelConverter;
import com.rbkmoney.damsel.cashreg.adapter.CashregResult;
import com.rbkmoney.damsel.cashreg.receipt.type.Debit;
import com.rbkmoney.damsel.cashreg.receipt.type.Type;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
        AdapterState adapterState = new AdapterState();

        PollingInfo pollingInfo = new PollingInfo();
        pollingInfo.setStartDateTimePolling(Instant.now());
        pollingInfo.setMaxDateTimePolling(Instant.now().plus(2, ChronoUnit.HOURS));
        adapterState.setPollingInfo(pollingInfo);

        ExitStateModel exitStateModel = ExitStateModel.builder()
                .adapterContext(adapterState)
                .receiptId(TestData.CASHREG_ID)
                .entryStateModel(entryStateModel)
                .build();

        // Проверка, что без состояния вернется поллинг статуса
        CashregResult regResult = exitConverter.convert(exitStateModel);
        assertTrue("CashRegResult status of a intent isn't sleep", regResult.getIntent().isSetSleep());

        ExitStateModel exitStateModelFailure = ExitStateModel.builder()
                .adapterContext(new AdapterState())
                .receiptId(TestData.CASHREG_ID)
                .entryStateModel(entryStateModel)
                .errorCode("code")
                .errorMessage("message")
                .build();

        // Проверка, что без состояния вернется поллинг статуса
        CashregResult regResultFailure = exitConverter.convert(exitStateModelFailure);
        assertTrue("CashRegResult status of a intent isn't failure", regResultFailure.getIntent().getFinish().getStatus().isSetFailure());
    }

}
