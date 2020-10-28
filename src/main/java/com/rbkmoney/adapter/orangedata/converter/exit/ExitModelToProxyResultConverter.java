package com.rbkmoney.adapter.orangedata.converter.exit;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.service.IntentService;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.state.serializer.AdapterSerializer;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.creators.CashregAdapterCreators;
import com.rbkmoney.adapter.common.model.PollingInfo;
import com.rbkmoney.damsel.cashreg.adapter.CashregResult;
import com.rbkmoney.damsel.cashreg.adapter.Intent;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ExitModelToProxyResultConverter implements Converter<ExitStateModel, CashregResult> {

    private final IntentService intentService;
    private final AdapterSerializer serializer;

    @Override
    public CashregResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        AdapterState adapterState = entryStateModel.getState().getAdapterContext();

        PollingInfo pollingInfo = new PollingInfo();
        if (adapterState.getPollingInfo() == null) {
            pollingInfo.setStartDateTimePolling(Instant.now());
            pollingInfo.setMaxDateTimePolling(intentService.extractMaxDateTimeInstant(entryStateModel));
            adapterState.setPollingInfo(pollingInfo);
        }

        if (Step.CREATE == adapterState.getNextStep()) {
            adapterState.setNextStep(Step.CHECK_STATUS);
        }

        Intent intent = prepareIntent(exitStateModel);
        CashregResult cashRegResult = new CashregResult()
                .setIntent(intent)
                .setState(serializer.writeByte(adapterState));

        if (exitStateModel.getInfo() != null) {
            entryStateModel.getState().getAdapterContext().setNextStep(Step.CHECK_STATUS);
            cashRegResult.setInfo(exitStateModel.getInfo());
        }
        return cashRegResult;
    }

    private Intent prepareIntent(ExitStateModel exitStateModel) {
        if (exitStateModel.getErrorCode() != null) {
            return intentService.getFailureByCodeAndDesc(exitStateModel);
        } else if (exitStateModel.getInfo() == null) {
            return intentService.getSleep(exitStateModel);
        } else {
            return CashregAdapterCreators.createFinishIntentSuccess();
        }
    }

}
