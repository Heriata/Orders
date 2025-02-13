package com.heriata.order_service;

import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.repository.DetailsJDBCRepository;
import com.heriata.order_service.repository.OrderJDBCRepository;
import com.heriata.order_service.rest_client.NumbersServiceClient;
import com.heriata.order_service.service.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RedisCacheTest {

    private static final String ORDER_NUMBER = "0000012345678";

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private OrderJDBCRepository orderRepository;

    @MockitoBean
    private DetailsJDBCRepository detailsRepository;

    @MockitoBean
    private NumbersServiceClient serviceClient;

    @Test
    @DisplayName("Should invoke repository method only once")
    void shouldCacheOrder() {
        OrderDetailsDto orderDetailsDto = TestUtils.providedOrderDetailsDto(TestUtils.getSeed());
        orderDetailsDto.setOrderNumber(ORDER_NUMBER);

        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(orderDetailsDto);

        OrderDetailsDto resultCacheMiss = orderService.getOrderByNumber(ORDER_NUMBER);
        OrderDetailsDto resultCacheHit = orderService.getOrderByNumber(ORDER_NUMBER);

        verify(orderRepository, times(1)).findByOrderNumber(ORDER_NUMBER);

        assertEquals(orderDetailsDto.getOrderNumber(), resultCacheMiss.getOrderNumber());
        assertEquals(orderDetailsDto.getOrderId(), resultCacheMiss.getOrderId());

        assertEquals(resultCacheMiss.getOrderNumber(), resultCacheHit.getOrderNumber());
    }
}
