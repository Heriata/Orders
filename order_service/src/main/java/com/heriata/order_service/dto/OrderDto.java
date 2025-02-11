package com.heriata.order_service.dto;

import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderNumber;
    private Long totalAmount;
    private LocalDateTime orderDate;
    private String customerName;
    private String address;
    private PaymentType paymentType;
    private DeliveryType deliveryType;
}
