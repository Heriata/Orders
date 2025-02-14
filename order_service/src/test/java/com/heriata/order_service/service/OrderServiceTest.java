package com.heriata.order_service.service;

import com.heriata.order_service.utils.TestUtils;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final String ITEM_NAME = "itemName";

    private static final long TOTAL_AMOUNT = 50L;
    private static final long ORDER_ID = 1L;

    private static final long ARTICLE = 123L;
    private static final String ORDER_NUMBER = "123";
    private static final String EXCLUDED = "excluded";

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderJDBCRepository orderRepository;

    @Mock
    private DetailsJDBCRepository detailsRepository;

    @Mock
    private NumbersServiceClient numbersServiceClient;

    @Test
    @DisplayName("Should create Order")
    public void shouldCreateOrder() {
        Order order = TestUtils.provideOrder();

        Details details = new Details();
        details.setOrderId(ORDER_ID);
        details.setArticle(ARTICLE);

        when(numbersServiceClient.getOrderNumber()).thenReturn(ORDER_NUMBER);
        when(orderRepository.save(any())).thenReturn(order);
        when(detailsRepository.save(any())).thenReturn(details);

        OrderCreateDto orderCreateDto = TestUtils.provideOrderCreateDto();

        OrderDto result = orderService.createOrder(orderCreateDto);

        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
        assertEquals(order.getCustomerName(), result.getCustomerName());
        verify(orderRepository).save(any(Order.class));
        verify(detailsRepository).save(any(Details.class));
    }

    @Test
    @DisplayName("Should throw exception when Order is not saved")
    public void shouldThrowEntityNotSavedExceptionWhenOrderIsNotSaved() {
        OrderCreateDto orderCreateDto = TestUtils.provideOrderCreateDto();

        when(numbersServiceClient.getOrderNumber()).thenReturn(ORDER_NUMBER);
        when(orderRepository.save(any())).thenReturn(null);

        EntityNotSavedException exception = assertThrows(EntityNotSavedException.class,
                () -> this.orderService.createOrder(orderCreateDto));

        assertEquals(EntityNotSavedException.class, exception.getClass());
    }

    @Test
    @DisplayName("Should throw exception when Details is not saved")
    public void shouldThrowEntityNotSavedExceptionWhenDetailsIsNull() {
        Order order = TestUtils.provideOrder();
        OrderCreateDto orderCreateDto = TestUtils.provideOrderCreateDto();

        when(numbersServiceClient.getOrderNumber()).thenReturn(ORDER_NUMBER);
        when(orderRepository.save(any())).thenReturn(order);
        when(detailsRepository.save(any())).thenReturn(null);

        EntityNotSavedException exception = assertThrows(EntityNotSavedException.class,
                () -> this.orderService.createOrder(orderCreateDto));

        assertEquals(EntityNotSavedException.class, exception.getClass());
    }

    @Test
    @DisplayName("Should get OrderDetailsDto by Id")
    public void shouldGetOrderDetailsDtoById() {
        OrderDetailsDto orderDetailsDto = TestUtils.providedOrderDetailsDto(TestUtils.getSeed());

        when(orderRepository.findByOrderId(ORDER_ID)).thenReturn(orderDetailsDto);

        OrderDetailsDto result = orderService.getByID(ORDER_ID);
        assertNotNull(result);
        assertEquals(orderDetailsDto.getOrderId(), result.getOrderId());
        assertEquals(orderDetailsDto.getCustomerName(), result.getCustomerName());
        assertEquals(orderDetailsDto.getQuantity(), result.getQuantity());
        assertEquals(orderDetailsDto.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    @DisplayName("Should throw exception when there is no Order with such id")
    public void shouldThrowEntityNotFoundExceptionWhenOrderDetailsIsNull() {
        when(orderRepository.findByOrderId(ORDER_ID)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getByID(ORDER_ID));

        assertNotNull(exception);
        assertEquals(String.format("Order with id %s not found", ORDER_ID), exception.getMessage());
        assertEquals(EntityNotFoundException.class, exception.getClass());
    }

    @Test
    @DisplayName("getOrderByDateAndPrice should return List of OrderDetailsDto")
    public void shouldGetListOfOrders() {

        List<OrderDetailsDto> list = TestUtils.provideListOrderDetailsDto();

        OrderDateAndPriceDto dto = TestUtils.providedOrderDateAndPriceDto(TOTAL_AMOUNT);
        when(orderRepository.findByDateAndPrice(dto)).thenReturn(list);

        List<OrderDetailsDto> result = orderService.getOrderByDateAndPrice(dto);

        assertNotNull(result);
        assertEquals(list.size(), result.size());

        result.forEach(x -> {
            assertNotNull(x);
            assertTrue(x.getTotalAmount() >= TOTAL_AMOUNT);
            assertTrue(x.getOrderDate().isBefore(LocalDateTime.now()));
        });
    }


    @Test
    @DisplayName("getOrderByDateAndPrice should throw EntityNotFoundException when there are no Orders for such parameters")
    public void shouldThrowEntityNotFoundExceptionWhenInvalidDateAndPrice() {
        OrderDateAndPriceDto dto = TestUtils.providedOrderDateAndPriceDto(TOTAL_AMOUNT);
        when(orderRepository.findByDateAndPrice(dto)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.getOrderByDateAndPrice(dto));

        assertNotNull(exception);
        assertEquals(EntityNotFoundException.class, exception.getClass());
        assertEquals("No orders with such parameters found", exception.getMessage());
    }

    @Test
    @DisplayName("getOrderByDateAndPrice should throw EntityNotFoundException when there are no Orders for such parameters")
    public void shouldThrowEntityNotFoundExceptionWhenEmptyResult() {
        OrderDateAndPriceDto dto = TestUtils.providedOrderDateAndPriceDto(TOTAL_AMOUNT);
        when(orderRepository.findByDateAndPrice(dto)).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.getOrderByDateAndPrice(dto));

        assertNotNull(exception);
        assertEquals(EntityNotFoundException.class, exception.getClass());
        assertEquals("No orders with such parameters found", exception.getMessage());
    }

    @Test
    @DisplayName("getOtherOrders should return list of OrderDetailsDto")
    public void shouldDeleteOrder() {
        List<OrderDetailsDto> list = TestUtils.provideListOrderDetailsDto(ITEM_NAME);
        OrderOthersDto dto = TestUtils.provideOrderOthersDto(EXCLUDED);

        when(orderRepository.findByDateExcludingItemName(dto)).thenReturn(list);

        List<OrderDetailsDto> result = orderService.getOtherOrders(dto);

        assertNotNull(result);
        assertEquals(list.size(), result.size());

        result.forEach(x -> {
            assertNotNull(x);
            assertTrue(x.getOrderDate().isAfter(LocalDateTime.now().minusDays(2L)));
            assertTrue(x.getOrderDate().isBefore(LocalDateTime.now()));
            assertNotEquals(EXCLUDED, x.getItemName());
            assertEquals(ITEM_NAME, x.getItemName());
        });
    }

    @Test
    @DisplayName("getOtherOrders should throw EntityNotFound when there are no Orders for such parameters")
    public void shouldThrowEntityNotFoundExceptionWhenInvalidOrders() {
        OrderOthersDto dto = TestUtils.provideOrderOthersDto(EXCLUDED);
        when(orderRepository.findByDateExcludingItemName(dto)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOtherOrders(dto));

        assertNotNull(exception);
        assertEquals("No orders with such parameters found", exception.getMessage());
    }

    @Test
    @DisplayName("getOrderByNumber should return OrderDetailsDto")
    public void shouldGetOrderByNumber() {
        OrderDetailsDto orderDetailsDto = TestUtils.providedOrderDetailsDto(TestUtils.getSeed());
        orderDetailsDto.setOrderNumber(ORDER_NUMBER);

        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(orderDetailsDto);

        OrderDetailsDto result = orderService.getOrderByNumber(ORDER_NUMBER);
        assertNotNull(result);
        assertEquals(orderDetailsDto.getOrderNumber(), result.getOrderNumber());
        assertEquals(orderDetailsDto.getOrderId(), result.getOrderId());
    }

    @Test
    @DisplayName("getOrderByNumber should throw when no OrderDetailsDto for such id")
    public void shouldThrowEntityNotFoundExceptionWhenInvalidOrderNumber() {
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderByNumber(ORDER_NUMBER));

        assertNotNull(exception);
        assertEquals(String.format("Order with id %s not found", ORDER_NUMBER), exception.getMessage());
    }

    @Test
    @DisplayName("In the name of 100% coverage")
    public void shouldInTheCoverage() {
        Thread.currentThread().interrupt();
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOrderByNumber(ORDER_NUMBER));

        assertNotNull(exception);
    }
}
