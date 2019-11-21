package com.rbkmoney.adapter.orangedata.service.orangedata;


import com.rbkmoney.adapter.orangedata.service.orangedata.constant.DocumentType;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Request;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.RequestWrapper;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
public class OrangeDataClient implements AdapterCashReg {

    private final OrangeDataApi api;

    @Override
    public ResponseEntity<Response> debit(RequestWrapper<Request> requestWrapper) {
        requestWrapper.getRequest().getContent().setType(DocumentType.INCOME);
        return api.create(requestWrapper);
    }

    @Override
    public ResponseEntity<Response> credit(RequestWrapper<Request> requestWrapper) {
        requestWrapper.getRequest().getContent().setType(DocumentType.INCOME_REFUND);
        return api.create(requestWrapper);
    }

    @Override
    public ResponseEntity<Response> refundDebit(RequestWrapper<Request> requestWrapper) {
        requestWrapper.getRequest().getContent().setType(DocumentType.COST);
        return api.create(requestWrapper);
    }

    @Override
    public ResponseEntity<Response> refundCredit(RequestWrapper<Request> requestWrapper) {
        requestWrapper.getRequest().getContent().setType(DocumentType.COST_REFUND);
        return api.create(requestWrapper);
    }

    @Override
    public ResponseEntity<Response> status(RequestWrapper<Request> requestWrapper) {
        return api.status(requestWrapper);
    }

}
