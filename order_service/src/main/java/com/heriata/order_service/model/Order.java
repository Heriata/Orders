package com.heriata.order_service.model;

import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table(name = "orders", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @Column(value = "id")
    private Long orderId;

    @Column(value = "order_number")
    private String orderNumber;

    @Column(value = "total_amount")
    private Long totalAmount;

    @Column(value = "order_date")
    private LocalDateTime orderDate;

    @Column(value = "customer_name")
    private String customerName;

    @Column(value = "address")
    private String address;

    @Column(value = "payment_type")
    private PaymentType paymentType;

    @Column(value = "delivery_type")
    private DeliveryType deliveryType;
}
