package com.heriata.order_service.service;

import com.heriata.order_service.rest_client.NumbersServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class NumberServiceTest {

    @Value("${redis.host}")
    String host;

    @Value("${redis.port}")
    Integer port;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    NumbersServiceClient numberService;

    @Test
    void getNumber() {

        log.info(host + ":" + port);
        // for some reason config values are not fetched
        String expectedOrderNumber = "0000012345678";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(expectedOrderNumber);
        String url = String.format("http://%s:%d/api/numbers", host, port);

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);

        String actualOrderNumber = numberService.getOrderNumber();

        assertEquals(expectedOrderNumber, actualOrderNumber);
    }
}
