package com.rbkmoney.adapter.orangedata.handler.cashreg;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.TargetType;
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
public class CreateRefundCreditCommonHandler extends CommonHandlerImpl<ExitStateModel, RequestWrapper<Request>, ResponseEntity<Response>, EntryStateModel> {

    public CreateRefundCreditCommonHandler(
            OrangeDataClient client,
            Converter<EntryStateModel, RequestWrapper<Request>> converter,
            Processor<ExitStateModel, EntryStateModel, ResponseEntity<Response>> responseProcessorChain
    ) {
        super(client::refundCredit, converter, responseProcessorChain);
    }

    @Override
    public boolean isHandler(EntryStateModel entryStateModel) {
        return Step.CREATE.equals(entryStateModel.getState().getAdapterContext().getNextStep())
                && TargetType.REFUND_CREDIT.equals(entryStateModel.getState().getTargetType());
    }

}
