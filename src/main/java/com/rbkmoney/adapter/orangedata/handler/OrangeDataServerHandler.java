package com.rbkmoney.adapter.orangedata.handler;


import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.cashreg.proto.provider.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.thrift.TException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.rbkmoney.adapter.orangedata.utils.wrapper.cashreg.constant.Error.DEFAULT_ERROR_CODE;
import static com.rbkmoney.adapter.orangedata.utils.wrapper.cashreg.creators.CashRegProviderCreators.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrangeDataServerHandler implements CashRegProviderSrv.Iface {

    private final OrangeDataClient client;

    @Override
    public CashRegResult debit(CashRegContext context) throws TException {
        String requestId = context.getRequestId();
        log.info("Start: debit with request_id {}", requestId);
        try {
            ResponseEntity<Response> responseEntity = (ResponseEntity<Response>) client.debit(context);
            Intent intent = getIntent(responseEntity, responseEntity.getBody(), requestId);
            String originalResponse = Objects.requireNonNull(responseEntity.getBody()).toString();
            CashRegResult cashRegResult = createCashRegResult(intent, originalResponse);
            log.info("Finish: debit with request_id {}, response {}", requestId, responseEntity);
            return cashRegResult;
        } catch (Exception ex) {
            log.error("Exception with request_id {}: ", context.getRequestId());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CashRegResult credit(CashRegContext context) throws TException {
        throw new TException("Method Credit Not Supported");
    }

    @Override
    public CashRegResult refundDebit(CashRegContext context) throws TException {
        String requestId = context.getRequestId();
        log.info("Start: refund debit with request_id {}", requestId);
        try {
            ResponseEntity<Response> responseEntity = (ResponseEntity<Response>) client.refundDebit(context);
            Intent intent = getIntent(responseEntity, responseEntity.getBody(), requestId);
            String originalResponse = Objects.requireNonNull(responseEntity.getBody()).toString();
            CashRegResult cashRegResult = createCashRegResult(intent, originalResponse);
            log.info("Finish: refund debit with request_id {}, response {}", requestId, responseEntity);
            return cashRegResult;
        } catch (Exception ex) {
            log.error("Exception with request_id {}: ", context.getRequestId());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CashRegResult refundCredit(CashRegContext context) throws TException {
        throw new TException("Method Credit Not Supported");
    }

    @Override
    public CashRegResult getStatus(CashRegContext context) throws TException {
        String requestId = context.getRequestId();
        log.info("Start: status with request_id {}", requestId);
        try {
            ResponseEntity<Response> responseEntity = (ResponseEntity<Response>) client.getStatus(context);
            Intent intent;
            if (responseEntity.getStatusCode().is4xxClientError()) {
                String reasonPhrase = org.springframework.http.HttpStatus.valueOf(responseEntity.getStatusCode().value()).getReasonPhrase();
                intent = createFinishIntentFailure(createFailure(DEFAULT_ERROR_CODE, reasonPhrase));
            } else {
                Response response = responseEntity.getBody();
                intent = getIntent(responseEntity, response, requestId);
            }

            String originalResponse = Objects.requireNonNull(responseEntity.getBody()).toString();
            CashRegResult cashRegResult = createCashRegResult(intent, originalResponse);
            log.info("Finish: status with request_id {}, response {}", context.getRequestId(), responseEntity);
            return cashRegResult;
        } catch (Exception ex) {
            log.error("Exception with request_id {}: ", context.getRequestId());
            throw new RuntimeException(ex);
        }
    }

    private static boolean hasError(Response response) {
        return response.getErrors().isEmpty();
    }

    private Intent getIntent(ResponseEntity<Response> responseEntity, Response response, String requestId) {
        Intent intent;
        if (responseEntity.getStatusCode().is4xxClientError()) {
            intent = createFinishIntentFailure(createFailure(DEFAULT_ERROR_CODE, getReasonPhraseFromEntityByCode(responseEntity)));
        } else if (hasError(response)) {
            intent = createFinishIntentFailure(createFailure(DEFAULT_ERROR_CODE, convertListToString(response.getErrors())));
        } else if (isCreated(responseEntity)) {
            intent = createFinishIntentSuccess(requestId);
        } else if (isDelivered(responseEntity)) {
            intent = createFinishIntentSuccess(preparePayload(response));
        } else {
            intent = createFinishIntentFailure(createFailure(DEFAULT_ERROR_CODE, getReasonPhraseFromEntityByCode(responseEntity)));
        }
        return intent;
    }

    public String getReasonPhraseFromEntityByCode(ResponseEntity<Response> responseEntity) {
        return org.springframework.http.HttpStatus.valueOf(responseEntity.getStatusCode().value()).getReasonPhrase();
    }

    public boolean isCreated(ResponseEntity<Response> responseEntity) {
        return HttpStatus.SC_CREATED == responseEntity.getStatusCode().value();
    }

    public boolean isDelivered(ResponseEntity<Response> responseEntity) {
        return HttpStatus.SC_OK == responseEntity.getStatusCode().value();
    }

    private static Payload preparePayload(Response response) {
        Payload payload = new Payload();
        payload.setFnsSite(response.getFnsWebsite());
        payload.setFnNumber(response.getFsNumber());
        payload.setShiftNumber(response.getShiftNumber().toString());
        return payload;
    }

    public static String convertListToString(List<String> errors) {
        return errors.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }
}
