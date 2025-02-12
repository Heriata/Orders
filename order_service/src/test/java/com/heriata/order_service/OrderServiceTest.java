//package com.heriata.order_service;
//
//import com.heriata.order_service.dto.OrderCreateDto;
//import com.heriata.order_service.dto.OrderDto;
//import com.heriata.order_service.enums.DeliveryType;
//import com.heriata.order_service.enums.PaymentType;
//import com.heriata.order_service.model.Details;
//import com.heriata.order_service.model.Order;
//import com.heriata.order_service.repository.DetailsRepository;
//import com.heriata.order_service.repository.OrderRepository;
//import com.heriata.order_service.repository.projection.OrderDetailsProjection;
//import com.heriata.order_service.rest_client.NumbersServiceClient;
//import com.heriata.order_service.service.OrderService;
//import com.heriata.order_service.service.OrderServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class OrderServiceTest {
//
//    public static final String NAME = "name";
//    public static final String ADDRESS = "address";
//    public static final long ARTICLE = 123L;
//    public static final String ITEM_NAME = "itemName";
//    public static final long QUANTITY = 10L;
//    public static final long PRICE = 5L;
//    public static final long TOTAL_AMOUNT = 100L;
//    public static final long ORDER_ID = 1L;
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private DetailsRepository detailsRepository;
//
//    @Mock
//    private NumbersServiceClient numbersServiceClient;
//
//
//    @BeforeEach
//    public void setUp() {
//    }
//
//    @Test
//    public void shouldCreateOrder() {
//        Order order = provideOrderObject();
//
//        Details details = new Details();
//        details.setOrderId(ORDER_ID);
//        details.setArticle(ARTICLE);
//
//        when(numbersServiceClient.getOrderNumber()).thenReturn("123");
//        when(orderRepository.save(any())).thenReturn(order);
//        when(detailsRepository.save(any())).thenReturn(details);
//
//        OrderCreateDto orderCreateDto = provideOrderCreateDto();
//
//        OrderDto result = orderService.createOrder(orderCreateDto);
//
//        assertNotNull(result);
//        assertEquals(order.getOrderId(), result.getOrderId());
//        assertEquals(order.getCustomerName(), result.getCustomerName());
//        verify(orderRepository).save(any(Order.class));
//        verify(detailsRepository).save(any(Details.class));
//    }
//
//    @Test
//    public void shouldGetOrderById() {
//        Order order = provideOrderObject();
//
//        //fixme
//        when(orderRepository.findByOrderId(any())).thenReturn(null);
//
//        OrderDto result = orderService.getOrderById(ORDER_ID);
//    }
//
//    private OrderCreateDto provideOrderCreateDto() {
//        return OrderCreateDto.builder()
//                .customerName(NAME)
//                .address(ADDRESS)
//                .paymentType(PaymentType.CARD)
//                .deliveryType(DeliveryType.DOOR)
//                .article(ARTICLE)
//                .itemName(ITEM_NAME)
//                .quantity(QUANTITY)
//                .price(PRICE)
//                .build();
//    }
//
//    private Order provideOrderObject() {
//        return Order.builder()
//                .orderId(ORDER_ID)
//                .totalAmount(TOTAL_AMOUNT)
//                .orderDate(LocalDateTime.now())
//                .customerName(NAME)
//                .address(ADDRESS)
//                .paymentType(PaymentType.CARD)
//                .deliveryType(DeliveryType.DOOR)
//                .build();
//    }
//}
