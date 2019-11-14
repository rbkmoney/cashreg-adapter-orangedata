package com.rbkmoney.adapter.orangedata.service.orangedata;

import com.rbkmoney.adapter.orangedata.service.orangedata.model.Request;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.RequestWrapper;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import org.springframework.http.ResponseEntity;

public interface AdapterCashReg {

    ResponseEntity<Response> debit(RequestWrapper<Request> request);

    ResponseEntity<Response> credit(RequestWrapper<Request> request);

    ResponseEntity<Response> refundDebit(RequestWrapper<Request> request);

    ResponseEntity<Response> refundCredit(RequestWrapper<Request> request);

    ResponseEntity<Response> status(RequestWrapper<Request> request);

}
