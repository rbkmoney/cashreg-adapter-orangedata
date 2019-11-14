package com.rbkmoney.adapter.orangedata.service.orangedata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.converter.ip.ConverterIp;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.HeaderField;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Request;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.RequestWrapper;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.adapter.orangedata.service.orangedata.signer.Signer;
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


    public ResponseEntity<Response> create(RequestWrapper<Request> requestWrapper) {
        try {
            String body = objectMapper.writeValueAsString(requestWrapper.getRequest());
            log.info("create with request: {}", body);

            String url = prepareUrl(requestWrapper.getUrl(),"documents/");
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

    public ResponseEntity<Response> status(RequestWrapper<Request> requestWrapper) {
        String inn = requestWrapper.getRequest().getInn();
        String documentId = requestWrapper.getRequest().getId();
        log.info("status with request: inn {}, documentId {}", inn, documentId);

        String prepareUrl = prepareUrl(requestWrapper.getUrl(), "documents/" + inn + "/status/" + documentId);
        log.info("status with url: {}", prepareUrl);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                prepareUrl, HttpMethod.GET, HttpEntity.EMPTY, Response.class
        );

        log.info("status with response: {}", responseEntity);
        return responseEntity;
    }

    private String prepareUrl(String url, String fullPath) {
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
