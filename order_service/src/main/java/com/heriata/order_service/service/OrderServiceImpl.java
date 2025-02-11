package com.heriata.order_service.service;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.model.Details;
import com.heriata.order_service.model.Order;
import com.heriata.order_service.repository.DetailsRepository;
import com.heriata.order_service.repository.OrderRepository;
import com.heriata.order_service.repository.projection.OrderDetailsProjection;
import com.heriata.order_service.rest_client.NumbersServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DetailsRepository detailsRepository;
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

        Details details = mapOrderCreateDtoToDetails(dto);
        details.setOrderId(save.getOrderId());

        detailsRepository.save(details);
        return this.mapOrderToOrderDto(save);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailsDto getByID(Long id) {
        OrderDetailsProjection byOrderId = orderRepository.findByOrderId(id);
        return mapDetailsOrderProjectionToOrderDetailsDto(byOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return mapOrderToOrderDto(order);
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrderByDateAndPrice(OrderDateAndPriceDto dto) {

        Long orderPrice = dto.getOrderPrice();
        LocalDateTime orderDate = dto.getOrderDate();

        List<Order> orders = orderRepository.findByDateAndPrice(orderPrice, orderDate);
        return orders.stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOtherOrders(OrderOthersDto dto) {
        LocalDateTime dateFrom = dto.getDateFrom();
        LocalDateTime dateTo = dto.getDateTo();
        String itemName = dto.getItemName();

        List<Order> orders = orderRepository.findByDateAndItemNameExcluded(dateFrom, dateTo, itemName);
        return orders.stream()
                .map(this::mapOrderToOrderDto)
                .toList();
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

    private OrderDetailsDto mapDetailsOrderProjectionToOrderDetailsDto(OrderDetailsProjection projection) {
        return OrderDetailsDto.builder()
                .orderId(projection.getId())
                .orderNumber(projection.getOrderNumber())
                .totalAmount(projection.getTotalAmount())
                .orderDate(projection.getOrderDate())
                .customerName(projection.getCustomerName())
                .address(projection.getAddress())
                .paymentType(projection.getPaymentType())
                .deliveryType(projection.getDeliveryType())
                .article(projection.getArticle())
                .itemName(projection.getItemName())
                .quantity(projection.getQuantity())
                .price(projection.getPrice())
                .build();
    }
}
