package com.heriata.order_service.repository.projection;

import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;

import java.time.LocalDateTime;

public interface OrderDetailsProjection {

    Long getId();
    String getOrderNumber();
    Long getTotalAmount();
    LocalDateTime getOrderDate();
    String getCustomerName();
    String getAddress();
    PaymentType getPaymentType();
    DeliveryType getDeliveryType();

    Long getArticle();
    String getItemName();
    Long getQuantity();
    Long getPrice();

}
