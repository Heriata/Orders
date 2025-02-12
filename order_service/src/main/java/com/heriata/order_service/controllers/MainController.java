package com.heriata.order_service.controllers;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Orders Controller", description = "Orders management operations")
public interface MainController extends GlobalController {

    @Operation(summary = "Get Order", description = "Get info about order by its id")
    @GetMapping("/by-number")
    ResponseEntity<OrderDetailsDto> getOrderById(@RequestParam String orderNumber);

    @Operation(summary = "Get Order", description = "Get info about order by its id")
    @GetMapping("/by-id")
    ResponseEntity<OrderDetailsDto> getById(@RequestParam("orderId") Long orderId);

    @Operation(summary = "Get Order", description = "Get orders that was created after the specified date and has bigger total sum than specified")
    @GetMapping(value = "/date-price")
    ResponseEntity<List<OrderDetailsDto>> getByDateAndPrice(@RequestParam("dateFrom") @Parameter(description = "yyyy-MM-ddThh:mm:ss") LocalDateTime dateFrom,
                                                            @RequestParam("price") Long price);

    @Operation(summary = "Get Order", description = "Get orders that was created in specified time interval and does not contain specified item")
    @GetMapping("/other")
    ResponseEntity<List<OrderDetailsDto>> getOtherOrders(@RequestParam("dateFrom") @Parameter(description = "yyyy-MM-ddThh:mm:ss") LocalDateTime dateFrom,
                                                         @RequestParam("dateTo") @Parameter(description = "yyyy-MM-ddThh:mm:ss") LocalDateTime dateTo,
                                                         @RequestParam("itemName") @Parameter(description = "Item name to be excluded") String itemName);

    @Operation(summary = "Create Order", description = "Create order by providing all of the necessary information")
    @PostMapping("/add-order")
    ResponseEntity<OrderDto> createOrder(@RequestBody OrderCreateDto orderDto);
}
