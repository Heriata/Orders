package com.heriata.order_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "order_details", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order_details_id_seq")
    @SequenceGenerator(name = "order_details_id_seq", sequenceName = "order_details_id_seq", allocationSize = 1)
    private Long detailsId;

    @Column(name = "article")
    private Long article;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Long price;

    @Column(name = "order_id")
    private Long orderId;
}
