package com.rbkmoney.adapter.orangedata.service.orangedata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.orangedata.configuration.properties.OrangeDataProperties;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.HeaderField;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Request;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.adapter.orangedata.service.orangedata.signer.Signer;
import com.rbkmoney.adapter.orangedata.utils.ConverterIp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class OrangeDataApi {

    private String url = "https://apip.orangedata.ru:2443/api/v2/";
    private String privateKey;

    private final Signer signer;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConverterIp converterIp;
    private final OrangeDataProperties orangeDataProperties;

    public ResponseEntity<Response> create(Request request) {
        try {
            String body = objectMapper.writeValueAsString(request);
            log.info("create with request: {}", body);

            String url = prepareUrl("documents/");
            log.info("create with url: {}", url);
            String signature = signer.sign(body, privateKey);
            HttpEntity httpEntity = new HttpEntity(body, getHttpHeaders(signature));

            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, httpEntity, Response.class
            );
            log.info("create with response: {}", responseEntity);
            return responseEntity;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResponseEntity<Response> status(String inn, String documentId) {
        log.info("status with request: inn {}, documentId {}", inn, documentId);

        String prepareUrl = prepareUrl("documents/" + inn + "/status/" + documentId);
        log.info("status with url: {}", prepareUrl);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                prepareUrl, HttpMethod.GET, HttpEntity.EMPTY, Response.class
        );

        log.info("status with response: {}", responseEntity);
        return responseEntity;
    }

    private String prepareUrl(String fullPath) {
        return converterIp.replaceIpv4ToIpv6(url + fullPath);
    }

    private HttpHeaders getHttpHeaders(String signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HeaderField.SIGNATURE, signature);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

}
