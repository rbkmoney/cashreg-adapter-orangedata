package com.rbkmoney.adapter.orangedata;


import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class MockUtils {

    public static void mockClient(OrangeDataClient client) {
        doAnswer((Answer<ResponseEntity<Response>>) invocation -> ResponseEntity
                .status(org.apache.http.HttpStatus.SC_CREATED)
                .body(Response.builder()
                        .id("id")
                        .fsNumber("FsNumber")
                        .deviceRN("DeviceRN")
                        .fnsWebsite("FnsWebsite")
                        .shiftNumber(1)
                        .processedAt("ProcessedAt")
                        .build())).when(client).debit(any());

        doAnswer((Answer<ResponseEntity<Response>>) invocation -> ResponseEntity
                .status(org.apache.http.HttpStatus.SC_OK)
                .body(Response.builder()
                        .id("id")
                        .fsNumber("FsNumber")
                        .deviceRN("DeviceRN")
                        .fnsWebsite("FnsWebsite")
                        .shiftNumber(1)
                        .processedAt("ProcessedAt")
                        .build())).when(client).status(any());
    }
}
