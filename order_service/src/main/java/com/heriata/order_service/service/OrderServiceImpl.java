package com.heriata.order_service.service;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.exceptions.EntityNotSavedException;
import com.heriata.order_service.model.Details;
import com.heriata.order_service.model.Order;
import com.heriata.order_service.repository.DetailsJDBCRepository;
import com.heriata.order_service.repository.OrderJDBCRepository;
import com.heriata.order_service.rest_client.NumbersServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderJDBCRepository orderRepository;
    private final DetailsJDBCRepository detailsRepository;
    private final NumbersServiceClient numbersServiceClient;

    @Override
    @Transactional
    public OrderDto createOrder(OrderCreateDto dto) {
        String orderNumber = numbersServiceClient.getOrderNumber();

        Order order = mapCreateOrderDtoToOrderEntity(dto);
        order.setOrderNumber(orderNumber);
        order.setTotalAmount(dto.getQuantity() * dto.getPrice());
        order.setOrderDate(LocalDateTime.now());

        Order save = orderRepository.save(order);

        if (save == null) {
            throw new EntityNotSavedException(order.getOrderNumber());
        }

        Details details = mapOrderCreateDtoToDetails(dto);

        if (details == null) {
            throw new EntityNotSavedException(String.valueOf(order.getOrderId()));
        }

        details.setOrderId(save.getOrderId());

        detailsRepository.save(details);
        return this.mapOrderToOrderDto(save);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailsDto getByID(Long id) {
        return orderRepository.findByOrderId(id);
    }

    @Cacheable(cacheNames = {"order_service"}, key = "#orderNumber")
    @Override
    public OrderDetailsDto getOrderByNumber(String orderNumber) {
        try{
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsDto> getOrderByDateAndPrice(OrderDateAndPriceDto dto) {
        return orderRepository.findByDateAndPrice(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsDto> getOtherOrders(OrderOthersDto dto) {
        return orderRepository.findByDateExcludingItemName(dto);
    }


    /* MAPPERS */

    private OrderDto mapOrderToOrderDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .customerName(order.getCustomerName())
                .address(order.getAddress())
                .paymentType(order.getPaymentType())
                .deliveryType(order.getDeliveryType())
                .build();
    }

    private Details mapOrderCreateDtoToDetails(OrderCreateDto dto) {
        return Details.builder()
                .article(dto.getArticle())
                .itemName(dto.getItemName())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .build();
    }

    private Order mapCreateOrderDtoToOrderEntity(OrderCreateDto dto) {
        return Order.builder()
                .address(dto.getAddress())
                .deliveryType(dto.getDeliveryType())
                .customerName(dto.getCustomerName())
                .paymentType(dto.getPaymentType())
                .build();
    }
}
