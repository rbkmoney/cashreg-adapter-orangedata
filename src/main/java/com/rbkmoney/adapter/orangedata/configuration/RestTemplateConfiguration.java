package com.rbkmoney.adapter.orangedata.configuration;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Configuration
public class RestTemplateConfiguration {

    @Value("${restTemplate.connectTimeout}")
    private int connectTimeout;

    @Value("${restTemplate.connectionRequestTimeout}")
    private int requestTimeout;

    @Value("${restTemplate.readTimeout}")
    private int readTimeout;


    @Bean
    public RestTemplate RestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();
        return getRestTemplate(sslContext);
    }

    private RestTemplate getRestTemplate(SSLContext sslContext) {
        RestTemplate restTemplate = new RestTemplate(requestFactory(httpClient(sslContext)));
        restTemplate.setErrorHandler(getDefaultResponseErrorHandler());
        setMessageConverter(restTemplate);
        return restTemplate;
    }

    private CloseableHttpClient httpClient(SSLContext sslContext) {
        return HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(sslContext)
                .build();
    }

    private HttpComponentsClientHttpRequestFactory requestFactory(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setConnectionRequestTimeout(requestTimeout);
        requestFactory.setReadTimeout(readTimeout);
        return requestFactory;
    }

    private void setMessageConverter(RestTemplate restTemplate) {
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
    }

    private DefaultResponseErrorHandler getDefaultResponseErrorHandler() {
        return new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        };
    }

}
