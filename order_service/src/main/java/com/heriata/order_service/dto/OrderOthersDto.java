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
public class OrderOthersDto {
    // Order

    @NotBlank
    @Schema(description = "Orders created after this Date", type = "string", format = "date-time")
    private LocalDateTime dateFrom;

    @NotBlank
    @Schema(description = "Orders created before this Date", type = "string", format = "date-time")
    private LocalDateTime dateTo;

    // Details
    @NotBlank
    @Schema(description = "Item Name that will be excluded from search", minLength = 1, maxLength = 255)
    private String itemName;
}
