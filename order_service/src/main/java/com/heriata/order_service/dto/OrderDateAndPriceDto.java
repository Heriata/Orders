package com.heriata.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Schema(description = "When order was created", type = "string", format = "date-time")
    private LocalDateTime orderDate;

    @NotBlank
    @Schema(description = "Total sum (price * quantity)", type = "int64")
    private Long orderTotalAmount;
}
