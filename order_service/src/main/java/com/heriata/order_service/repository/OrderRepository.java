package com.heriata.order_service.repository;

import com.heriata.order_service.model.Order;
import com.heriata.order_service.repository.projection.OrderDetailsProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>{

    @Query(value = """
            SELECT * FROM orders o
            JOIN public.order_details od on o.id = od.order_id
            WHERE o.order_date >= ?2 AND (od.price * od.quantity) >= ?1
            """, nativeQuery = true)
    List<Order> findByDateAndPrice(Long orderPrice, LocalDateTime orderDate);

    @Query(value = """
            SELECT * FROM orders o
            JOIN public.order_details od on o.id = od.order_id
            WHERE (order_date BETWEEN ?1 AND ?2)
            AND od.item_name != ?3;
            """, nativeQuery = true)
    List<Order> findByDateAndItemNameExcluded(LocalDateTime dateFrom, LocalDateTime dateTo, String itemName);

    @Query(value = """
            select o.id,
                   o.order_number,
                   o.total_amount,
                   o.order_date,
                   o.customer_name,
                   o.address,
                   o.payment_type,
                   o.delivery_type,
                   od.article,
                   od.item_name,
                   od.quantity,
                   od.price
            from orders o
            join public.order_details od on o.id = od.order_id
            where o.id = ?1;
            """, nativeQuery = true)
    OrderDetailsProjection findByOrderId(Long orderId);
}
