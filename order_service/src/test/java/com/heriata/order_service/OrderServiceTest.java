package com.heriata.order_service;

import com.heriata.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static OrderService orderService;

    @BeforeAll
    static void setUp() {
    }
}
