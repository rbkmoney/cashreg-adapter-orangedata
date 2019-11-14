package com.rbkmoney.adapter.orangedata.processor;


import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.processor.Processor;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.adapter.orangedata.utils.ErrorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class ErrorProcessor implements Processor<ExitStateModel, EntryStateModel, ResponseEntity<Response>> {

    @Override
    public ExitStateModel process(ResponseEntity<Response> responseEntity, EntryStateModel entryStateModel) {
        Instant currentTime = Instant.now();
        AdapterState adapterState = new AdapterState();
        if (entryStateModel.getState().getAdapterContext() != null) {
            adapterState = entryStateModel.getState().getAdapterContext();
        }

        ExitStateModel exitStateModel = new ExitStateModel();
        if (ErrorUtils.hasError(responseEntity)) {
            exitStateModel.setEntryStateModel(entryStateModel);
        } else if (adapterState.getMaxDateTimePolling().getEpochSecond() < currentTime.getEpochSecond()) {
            log.error("Sleep Timeout for response: {}!", responseEntity);
            exitStateModel.setErrorCode(com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.Error.SLEEP_TIMEOUT.getCode());
            exitStateModel.setErrorMessage(com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.Error.SLEEP_TIMEOUT.getMessage());
        } else {
            log.error("Unknown result code for response: {}!", responseEntity);
            exitStateModel.setErrorCode(com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.Error.UNKNOWN.getCode());
            exitStateModel.setErrorMessage(com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.Error.UNKNOWN.getMessage());
        }
        return exitStateModel;
    }

}
