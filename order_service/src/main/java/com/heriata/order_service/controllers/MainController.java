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

    @Operation(summary = "Get Order by OrderNumber", description = "Get Order By OrderNumber. For Redis cache demonstration")
    @GetMapping("/by-number")
    ResponseEntity<OrderDetailsDto> getOrderByOrderNumber(
            @RequestParam("orderNumber") @Parameter(description = "(exmpl: 0001320250211)") String orderNumber);

    @Operation(summary = "Get Order By Id", description = "Get info about order by its id")
    @GetMapping("/by-id")
    ResponseEntity<OrderDetailsDto> getByOrderId(
            @RequestParam("orderId") @Parameter(description = "Get OrderDetails by OrderNumber. Cache TTL is 5sec") Long orderId);

    @Operation(summary = "Get Order By Date and Price", description = "Get orders that was created after the specified date and has bigger total sum than specified")
    @GetMapping(value = "/date-price")
    ResponseEntity<List<OrderDetailsDto>> getByDateAndPrice(
            @RequestParam(value = "dateFrom", defaultValue = "2025-02-11T00:00:00") @Parameter(description = "yyyy-MM-ddThh:mm:ss (exmpl: 2025-02-11T00:00:00)") LocalDateTime dateFrom,
            @RequestParam("price") @Parameter(description = "(exmpl: 600)") Long price);

    @Operation(summary = "Get Order By Date Excluding Name", description = "Get orders that was created in specified time interval and does not contain specified item. (use example values)" )
    @GetMapping("/other")
    ResponseEntity<List<OrderDetailsDto>> getOtherOrders(@RequestParam("dateFrom") @Parameter(description = "yyyy-MM-ddThh:mm:ss (2025-02-11T00:00:00)") LocalDateTime dateFrom,
                                                         @RequestParam("dateTo") @Parameter(description = "yyyy-MM-ddThh:mm:ss (2025-02-12T00:00:00)") LocalDateTime dateTo,
                                                         @RequestParam("itemName") @Parameter(description = "Item name to be excluded (chicken)") String itemName);

    @Operation(summary = "Create Order", description = "Create order by providing all of the necessary information")
    @PostMapping("/add-order")
    ResponseEntity<OrderDto> createOrder(@RequestBody OrderCreateDto orderDto);
}
