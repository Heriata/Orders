package com.heriata.order_service.rest_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NumbersServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public String getOrderNumber() {
        String url = "http://localhost:8081/api/numbers";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        return forEntity.getBody();
    }
}
