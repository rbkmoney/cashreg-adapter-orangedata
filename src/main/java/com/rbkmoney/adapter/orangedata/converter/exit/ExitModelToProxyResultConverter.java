package com.rbkmoney.adapter.orangedata.converter.exit;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.config.properties.TimerProperties;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.state.serializer.AdapterSerializer;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.creators.CashRegProviderCreators;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import com.rbkmoney.damsel.cashreg.provider.Intent;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.error.mapping.ErrorMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

import static com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.extractors.OptionsExtractors.extractMaxTimePolling;
import static com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.extractors.OptionsExtractors.extractPollingDelay;
import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@RequiredArgsConstructor
public class ExitModelToProxyResultConverter implements Converter<ExitStateModel, CashRegResult> {

    private final ErrorMapping errorMapping;
    private final TimerProperties timerProperties;
    private final AdapterSerializer serializer;

    @Override
    public CashRegResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        AdapterState adapterState = entryStateModel.getState().getAdapterContext();
        if (adapterState.getMaxDateTimePolling() == null) {
            int timerMaxTimePolling = extractMaxTimePolling(entryStateModel.getOptions(), timerProperties.getMaxTimePolling());
            Instant maxDateTime = Instant.now().plus(timerMaxTimePolling, MINUTES);
            adapterState.setMaxDateTimePolling(maxDateTime);
        }

        if (Step.CREATE == adapterState.getNextStep()) {
            adapterState.setNextStep(Step.CHECK_STATUS);
        }

        Intent intent = prepareIntent(exitStateModel, entryStateModel);
        CashRegResult cashRegResult = new CashRegResult()
                .setIntent(intent)
                .setState(serializer.writeByte(adapterState));

        if (exitStateModel.getCashRegInfo() != null) {
            entryStateModel.getState().getAdapterContext().setNextStep(Step.CHECK_STATUS);
            cashRegResult.setCashregInfo(exitStateModel.getCashRegInfo());
        }
        return cashRegResult;
    }

    private Intent prepareIntent(ExitStateModel exitStateModel, EntryStateModel entryStateModel) {
        if (exitStateModel.getErrorCode() != null) {
            Failure failure = errorMapping.mapFailure(exitStateModel.getErrorCode(), exitStateModel.getErrorMessage());
            return CashRegProviderCreators.createFinishIntentFailure(failure);
        } else if (exitStateModel.getCashRegInfo() == null) {
            Map<String, String> options = entryStateModel.getOptions();
            return CashRegProviderCreators.createIntentWithSleepIntent(extractPollingDelay(options, timerProperties.getPollingDelay()));
        } else {
            return CashRegProviderCreators.createFinishIntentSuccess();
        }
    }

}
