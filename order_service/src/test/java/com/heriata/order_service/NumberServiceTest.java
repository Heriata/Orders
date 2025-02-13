package com.heriata.order_service;

import com.heriata.order_service.rest_client.NumbersServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NumberServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    NumbersServiceClient numberService;

    @Test
    void getNumber() {
        String expectedOrderNumber = "0000012345678";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(expectedOrderNumber);
        String url = "http://localhost:8081/api/numbers";

        when(restTemplate.getForEntity(url, String.class)).thenReturn(responseEntity);
        String actualOrderNumber = numberService.getOrderNumber();

        assertEquals(expectedOrderNumber, actualOrderNumber);
    }
}
