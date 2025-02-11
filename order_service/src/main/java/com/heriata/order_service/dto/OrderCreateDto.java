package com.heriata.order_service.dto;

import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
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
    private String customerName;
    private String address;
    private PaymentType paymentType;
    private DeliveryType deliveryType;

    // DetailsEntity
    private Long article;
    private String itemName;
    private Long quantity;
    private Long price;
}

