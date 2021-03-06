package com.rbkmoney.adapter.orangedata.handler.cashreg;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.handler.CommonHandlerImpl;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.EntryStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.ExitStateModel;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.model.Step;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.processor.Processor;
import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Request;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.RequestWrapper;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class StatusCommonHandler extends CommonHandlerImpl<ExitStateModel, RequestWrapper<Request>, ResponseEntity<Response>, EntryStateModel> {

    public StatusCommonHandler(
            OrangeDataClient client,
            Converter<EntryStateModel, RequestWrapper<Request>> converter,
            Processor<ExitStateModel, EntryStateModel, ResponseEntity<Response>> responseProcessorChain
    ) {
        super(client::status, converter, responseProcessorChain);
    }

    @Override
    public boolean isHandler(EntryStateModel entryStateModel) {
        return Step.CHECK_STATUS.equals(entryStateModel.getState().getAdapterContext().getNextStep());
    }

}
