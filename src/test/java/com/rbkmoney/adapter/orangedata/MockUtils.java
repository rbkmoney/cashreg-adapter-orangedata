package com.rbkmoney.adapter.orangedata;

import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import org.apache.http.HttpStatus;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class MockUtils {

    public static void mockClientSuccess(OrangeDataClient client) {
        Response response = Response.builder()
                .id("id")
                .fsNumber("FsNumber")
                .deviceRN("DeviceRN")
                .fnsWebsite("FnsWebsite")
                .shiftNumber(1)
                .processedAt("ProcessedAt")
                .build();

        mockClient(client, response);
        mockClientStatus(client, response, HttpStatus.SC_OK);
    }

    public static void mockClientFailed(OrangeDataClient client) {
        Response response = Response.builder()
                .id("id")
                .fsNumber("FsNumber")
                .deviceRN("DeviceRN")
                .fnsWebsite("FnsWebsite")
                .shiftNumber(1)
                .processedAt("ProcessedAt")
                .build();

        mockClient(client, response);
        mockClientStatus(client, response, HttpStatus.SC_BAD_REQUEST);
    }

    public static void mockClient(OrangeDataClient client, Response response) {
        Answer<ResponseEntity<Response>> responseEntityAnswer = invocation -> ResponseEntity
                .status(HttpStatus.SC_CREATED)
                .body(response);

        doAnswer(responseEntityAnswer).when(client).debit(any());
        doAnswer(responseEntityAnswer).when(client).credit(any());
        doAnswer(responseEntityAnswer).when(client).refundCredit(any());
        doAnswer(responseEntityAnswer).when(client).refundDebit(any());
    }

    public static void mockClientStatus(OrangeDataClient client, Response response, int status) {
        doAnswer((Answer<ResponseEntity<Response>>) invocation -> ResponseEntity
                .status(status)
                .body(response))
                .when(client)
                .status(any());
    }


}
