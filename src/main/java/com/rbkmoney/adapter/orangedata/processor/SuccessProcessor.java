package com.rbkmoney.adapter.orangedata.processor;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.AdapterState;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.processor.Processor;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.adapter.orangedata.utils.ErrorUtils;
import com.rbkmoney.damsel.cashreg.CashRegInfo;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class SuccessProcessor implements Processor<ExitStateModel, EntryStateModel, ResponseEntity<Response>> {

    private final Processor<ExitStateModel, EntryStateModel, ResponseEntity<Response>> nextProcessor;

    @Override
    public ExitStateModel process(ResponseEntity<Response> responseEntity, EntryStateModel entryStateModel) {
        if (!ErrorUtils.hasError(responseEntity)) {
            ExitStateModel exitStateModel = new ExitStateModel();
            exitStateModel.setEntryStateModel(entryStateModel);

            AdapterState adapterState = entryStateModel.getState().getAdapterContext();
            Response response = responseEntity.getBody();
            adapterState.setReceiptId(response.getId());
            adapterState.setCashRegId(entryStateModel.getCashRegId());

            if (Step.CHECK_STATUS.equals(entryStateModel.getState().getAdapterContext().getNextStep())
                    && isDelivered(responseEntity)) {
                exitStateModel.setCashRegInfo(new CashRegInfo()
                        .setReceiptId(response.getId())
                        .setTotal(entryStateModel.getTotal().toString())
                        .setFiscalDocumentNumber(response.getFsNumber())
                        .setDeviceCode(response.getDeviceRN())
                        .setFnsSite(response.getFnsWebsite())
                        .setShiftNumber(response.getShiftNumber().toString())
                        .setTimestamp(response.getProcessedAt()));
            } else if (Step.CREATE.equals(entryStateModel.getState().getAdapterContext().getNextStep())) {
                adapterState.setNextStep(Step.CHECK_STATUS);
            }
            exitStateModel.setAdapterContext(adapterState);
            return exitStateModel;
        }

        return nextProcessor.process(responseEntity, entryStateModel);
    }

    public boolean isDelivered(ResponseEntity<Response> entity) {
        return HttpStatus.SC_OK == entity.getStatusCode().value();
    }
}
