package com.heriata.order_service.rest_client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class NumbersServiceClient {

    @Value("${numbers.host}")
    private String HOST;

    @Value("${numbers.port}")
    private String PORT;

    @Autowired
    private RestTemplate restTemplate;

    public String getOrderNumber() {
        log.info("Host: {}, Port: {}", HOST, PORT);
        String url = String.format("http://%s:%s/api/numbers", HOST, PORT);
        log.info("URL: {}", url);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        return forEntity.getBody();
    }
}
