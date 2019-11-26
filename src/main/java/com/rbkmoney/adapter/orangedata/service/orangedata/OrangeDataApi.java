package com.rbkmoney.adapter.orangedata.service.orangedata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.converter.ip.ConverterIp;
import com.rbkmoney.adapter.orangedata.service.orangedata.constant.HeaderField;
import com.rbkmoney.adapter.orangedata.service.orangedata.exception.OrangeDataApiException;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Request;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.RequestWrapper;
import com.rbkmoney.adapter.orangedata.service.orangedata.model.Response;
import com.rbkmoney.adapter.orangedata.service.orangedata.signer.Signer;
import io.micrometer.shaded.io.netty.handler.timeout.ReadTimeoutException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Data
@RequiredArgsConstructor
public class OrangeDataApi {

    public static final String STATUS_PATTERN = "documents/%s/status/%s";

    private final Signer signer;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConverterIp converterIp;

    public ResponseEntity<Response> create(RequestWrapper<Request> requestWrapper) {
        try {
            String body = objectMapper.writeValueAsString(requestWrapper.getRequest());
            log.info("Create with request: {}", body);

            String prepareUrl = prepareUrl(requestWrapper.getUrl(), "documents/");
            log.info("Create with url: {}", prepareUrl);
            String signature = signer.sign(body, requestWrapper.getPrivateKey());
            HttpEntity httpEntity = new HttpEntity<>(body, getHttpHeaders(signature));

            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    prepareUrl, HttpMethod.POST, httpEntity, Response.class
            );
            log.info("Create with response: {}", responseEntity);
            return responseEntity;
        } catch (ResourceAccessException | ReadTimeoutException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OrangeDataApiException(ex);
        }
    }

    public ResponseEntity<Response> status(RequestWrapper<Request> requestWrapper) {
        String inn = requestWrapper.getRequest().getInn();
        String documentId = requestWrapper.getRequest().getId();
        log.info("Status with request: inn {}, documentId {}", inn, documentId);

        String prepareUrl = prepareUrl(requestWrapper.getUrl(), String.format(STATUS_PATTERN, inn, documentId));
        log.info("Status with url: {}", prepareUrl);

        try {
            ResponseEntity<Response> responseEntity = restTemplate.exchange(
                    prepareUrl, HttpMethod.GET, HttpEntity.EMPTY, Response.class
            );
            log.info("Status with response: {}", responseEntity);
            return responseEntity;
        } catch (ResourceAccessException | ReadTimeoutException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OrangeDataApiException(ex);
        }
    }

    private String prepareUrl(String url, String fullPath) {
        return converterIp.replaceIpv4ToIpv6(url + fullPath);
    }

    private HttpHeaders getHttpHeaders(String signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HeaderField.SIGNATURE.getValue(), signature);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

}
