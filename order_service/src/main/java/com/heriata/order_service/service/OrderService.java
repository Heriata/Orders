package com.heriata.order_service.service;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderDto createOrder(OrderCreateDto order);
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrderByDateAndPrice(OrderDateAndPriceDto dto);
    List<OrderDto> getOtherOrders(OrderOthersDto dto);
    OrderDetailsDto getByID(Long id);
}
