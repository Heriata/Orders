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
public class OrderDateAndPriceDto {
    private LocalDateTime orderDate;
    private Long orderPrice;
}
