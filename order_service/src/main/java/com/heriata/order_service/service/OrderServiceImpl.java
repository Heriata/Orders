package com.heriata.order_service.service;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.exceptions.EntityNotFoundException;
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
        details.setOrderId(save.getOrderId());

        Details detailsSaved = detailsRepository.save(details);
        if (detailsSaved == null) {
            throw new EntityNotSavedException(String.valueOf(order.getOrderId()));
        }

        return this.mapOrderToOrderDto(save);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailsDto getByID(Long id) {
        OrderDetailsDto orderDetailsDto = orderRepository.findByOrderId(id);
        if (orderDetailsDto == null) {
            throw new EntityNotFoundException(
                    String.format("Order with id %s not found", id));
        }

        return orderDetailsDto;
    }

    @Override
    @Cacheable(cacheNames = {"order_service"}, key = "#orderNumber")
    public OrderDetailsDto getOrderByNumber(String orderNumber) {
        try {
            // simulate heavy operation for cache demonstration
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        OrderDetailsDto orderDetailsDto = orderRepository.findByOrderNumber(orderNumber);
        if (orderDetailsDto == null) {
            throw new EntityNotFoundException(String.format("Order with id %s not found", orderNumber));
        }
        return orderDetailsDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsDto> getOrderByDateAndPrice(OrderDateAndPriceDto dto) {
        List<OrderDetailsDto> list = orderRepository.findByDateAndPrice(dto);
        if (list == null) {
            throw new EntityNotFoundException("No orders with such parameters found");
        }
        if (list.isEmpty()) {
            throw new EntityNotFoundException("No orders with such parameters found");
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailsDto> getOtherOrders(OrderOthersDto dto) {
        List<OrderDetailsDto> list = orderRepository.findByDateExcludingItemName(dto);
        if (list == null) {
            throw new EntityNotFoundException("No orders with such parameters found");
        }
        return list;
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
