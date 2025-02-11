package com.heriata.order_service.controllers;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController("/orders")
public class MainControllerImpl implements MainController {

    private OrderService orderService;

    @Override
    public ResponseEntity<OrderDto> getOrderById(@RequestParam("orderId") Long orderId) {
        OrderDto orderById = orderService.getOrderById(orderId);
        log.info("getOrderById: {}", orderById);
        return new ResponseEntity<>(orderById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDetailsDto> getById(@RequestParam("orderId") Long orderId) {
        OrderDetailsDto orderById = orderService.getByID(orderId);
        log.info("getById: {}", orderById);
        return new ResponseEntity<>(orderById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderDto>> getByDateAndPrice(@RequestBody OrderDateAndPriceDto orderDateAndPrice){
        List<OrderDto> dto = orderService.getOrderByDateAndPrice(orderDateAndPrice);
        log.info("getByDateAndPrice: {}", dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderDto>> getOtherOrders(@RequestBody OrderOthersDto orderOthersDto){
        List<OrderDto> otherOrders = orderService.getOtherOrders(orderOthersDto);
        log.info("getOtherOrders: {}", otherOrders.size());
        return new ResponseEntity<>(otherOrders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderCreateDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        log.info("createOrder: {}", createdOrder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
}
