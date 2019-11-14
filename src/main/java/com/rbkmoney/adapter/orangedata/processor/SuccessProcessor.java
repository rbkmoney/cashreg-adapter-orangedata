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

            Response response = responseEntity.getBody();

            AdapterState adapterState = entryStateModel.getState().getAdapterContext();
            adapterState.setReceiptId(response.getId());
            adapterState.setCashRegId(entryStateModel.getCashRegId());

            if (Step.CHECK_STATUS.equals(entryStateModel.getState().getAdapterContext().getNextStep())) {
                if (isDelivered(responseEntity)) {
                    CashRegInfo cashRegInfo = new CashRegInfo();
                    cashRegInfo.setReceiptId(response.getId());
                    cashRegInfo.setTotal(entryStateModel.getTotal().toString());
                    cashRegInfo.setFiscalDocumentNumber(response.getFsNumber());
                    cashRegInfo.setDeviceCode(response.getDeviceRN());
                    cashRegInfo.setFnsSite(response.getFnsWebsite());
                    cashRegInfo.setShiftNumber(response.getShiftNumber().toString());
                    cashRegInfo.setTimestamp(response.getProcessedAt());

                    exitStateModel.setCashRegInfo(cashRegInfo);
                }
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