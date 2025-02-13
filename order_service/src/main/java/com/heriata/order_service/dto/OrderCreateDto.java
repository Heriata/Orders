package com.heriata.order_service.dto;

import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderCreateDto {
    // OrderEntity
    @NotBlank
    @Schema(description = "Name of a customer", type = "string", minLength = 5, maxLength = 255)
    private String customerName;

    @NotBlank
    @Schema(description = "Address", type = "string", minLength = 5, maxLength = 255)
    private String address;

    @NotBlank
    @Schema(description = "Payment Type", type = "enum", maxLength = 10)
    private PaymentType paymentType;

    @NotBlank
    @Schema(description = "Delivery Type", type = "enum", maxLength = 10)
    private DeliveryType deliveryType;

    // DetailsEntity
    @NotBlank
    @Schema(description = "Description", type = "int64", minimum = "1")
    private Long article;

    @NotBlank
    @Schema(description = "Name of an Item", type = "string", minLength = 1, maxLength = 255)
    private String itemName;

    @NotBlank
    @Schema(description = "Quantity", type = "int64", minimum = "1")
    private Long quantity;

    @NotBlank
    @Schema(description = "Price", type = "int64", minimum = "1")
    private Long price;
}

