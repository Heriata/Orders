package com.heriata.order_service.controllers;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Orders Controller", description = "Orders management operations")
public interface MainController extends GlobalController {

    @Operation(summary = "Get Order", description = "Get info about Order and its details by its id")
    @GetMapping
    ResponseEntity<OrderDto> getOrderById(@RequestParam("OrderId") Long orderId);

    @Operation(summary = "Get Order", description = "Get info about order by its id")
    @GetMapping("/by-id")
    ResponseEntity<OrderDetailsDto> getById(@RequestParam("orderId") Long orderId);

    @Operation(summary = "Get Order", description = "Get orders that was created after the specified date and has bigger total sum than specified")
    @GetMapping("/date-price")
    ResponseEntity<List<OrderDto>> getByDateAndPrice(@RequestBody OrderDateAndPriceDto orderDateAndPrice);

    @Operation(summary = "Get Order", description = "Get orders that was created in specified time interval and does not contain specified item")
    @GetMapping("/other")
    ResponseEntity<List<OrderDto>> getOtherOrders(@RequestBody OrderOthersDto orderOthersDto);

    @Operation(summary = "Create Order", description = "Create order by providing all of the necessary information")
    @PostMapping
    ResponseEntity<OrderDto> createOrder(@RequestBody OrderCreateDto orderDto);
}
