package com.heriata.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderOthersDto {
    // Order
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    // Details
    private String itemName;
}
