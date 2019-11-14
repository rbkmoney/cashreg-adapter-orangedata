package com.rbkmoney.adapter.orangedata;


import com.rbkmoney.adapter.orangedata.service.orangedata.OrangeDataClient;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class MockUtils {

    public static void mockClient(OrangeDataClient client) {
        doAnswer((Answer<ResponseEntity<Response>>) invocation -> {
            Response response = new Response();
            response.setId("id");
            response.setFsNumber("FsNumber");
            response.setDeviceRN("DeviceRN");
            response.setFnsWebsite("FnsWebsite");
            response.setShiftNumber(1);
            response.setProcessedAt("ProcessedAt");
            return ResponseEntity
                    .status(org.apache.http.HttpStatus.SC_CREATED)
                    .body(response);
        }).when(client).debit(any());

        doAnswer((Answer<ResponseEntity<Response>>) invocation -> {
            Response response = new Response();
            response.setId("id");
            response.setFsNumber("FsNumber");
            response.setDeviceRN("DeviceRN");
            response.setFnsWebsite("FnsWebsite");
            response.setShiftNumber(1);
            response.setProcessedAt("ProcessedAt");
            return ResponseEntity
                    .status(org.apache.http.HttpStatus.SC_OK)
                    .body(response);
        }).when(client).status(any());
    }
}
