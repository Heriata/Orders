package com.heriata.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(name = "order_details", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    @Id
    @Column(value = "id")
    private Long detailsId;

    @Column(value = "article")
    private Long article;

    @Column(value = "item_name")
    private String itemName;

    @Column(value = "quantity")
    private Long quantity;

    @Column(value = "price")
    private Long price;

    @Column(value = "order_id")
    private Long orderId;
}
