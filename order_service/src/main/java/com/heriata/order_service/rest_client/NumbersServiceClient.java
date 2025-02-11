package com.heriata.order_service.rest_client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NumbersServiceClient {

    public String getOrderNumber() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/numbers";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
//        return forEntity.getBody();
        return "11111";
    }
}
