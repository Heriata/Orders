package com.heriata.order_service.dto;

import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderDetailsDto implements Serializable {

    private Long orderId;
    private String orderNumber;
    private Long totalAmount;
    private LocalDateTime orderDate;
    private String customerName;
    private String address;
    private PaymentType paymentType;
    private DeliveryType deliveryType;

    private Long article;
    private String itemName;
    private Long quantity;
    private Long price;
}
