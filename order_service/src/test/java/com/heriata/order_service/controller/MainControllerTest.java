package com.heriata.order_service.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heriata.order_service.utils.TestUtils;
import com.heriata.order_service.controllers.MainControllerImpl;
import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import com.heriata.order_service.service.OrderService;
import com.heriata.order_service.service.OrderServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainControllerImpl.class)
@ContextConfiguration(classes = {MainControllerImpl.class, OrderServiceImpl.class})
public class MainControllerTest {

    public static final String ORDER_NUMBER_VALUE = "0000012345678";
    public static final long ORDER_ID = 1L;
    public static final long PRICE = 5L;
    public static final long QUANTITY = 10L;
    public static LocalDateTime DATE_NOW;
    public static LocalDateTime DATE_FROM;
    public static final String NAME = "Name";
    public static final String CUSTOMER_NAME = "customer name";
    public static final String ADDRESS = "address";
    public static final long ARTICLE = 123L;
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockitoBean
    private final OrderService orderService;

    @Autowired
    public MainControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                              OrderService orderService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @BeforeAll
    public static void beforeAll() {
        TestUtils.setLocalDateTimeNow();
        DATE_NOW = TestUtils.getLocalDateTimeNow();
        DATE_FROM = TestUtils.getLocalDateTimeNow().minusDays(2L);
    }

    @Test
    @SneakyThrows
    public void getOrderByOrderNumber_shouldReturnOrderDetailsDto() {

        OrderDetailsDto dto = TestUtils.providedOrderDetailsDto(TestUtils.getSeed());
        dto.setOrderNumber(ORDER_NUMBER_VALUE);

        when(orderService.getOrderByNumber(anyString())).thenReturn(dto);

        String asString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/by-number")
                        .param("orderNumber", ORDER_NUMBER_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        OrderDetailsDto orderDetailsDto = objectMapper.readValue(asString, OrderDetailsDto.class);

        assertEquals(ORDER_NUMBER_VALUE, orderDetailsDto.getOrderNumber());
    }

    @Test
    @SneakyThrows
    public void getByOrderId_shouldReturnOrderDetailsDto() {
        OrderDetailsDto dto = TestUtils.providedOrderDetailsDto(TestUtils.getSeed());
        dto.setOrderId(ORDER_ID);

        when(orderService.getByID(ORDER_ID)).thenReturn(dto);

        String asString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/by-id")
                        .param("orderId", String.valueOf(ORDER_ID)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        OrderDetailsDto orderDetailsDto = objectMapper.readValue(asString, OrderDetailsDto.class);

        assertEquals(ORDER_ID, orderDetailsDto.getOrderId());
    }

    @Test
    @SneakyThrows
    public void getByDateAndPrice_shouldReturnOrderDetailsDtoList() {
        List<OrderDetailsDto> dtoList = TestUtils.provideListOrderDetailsDto();

        when(orderService.getOrderByDateAndPrice(any(OrderDateAndPriceDto.class)))
                .thenReturn(dtoList);

        String asString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/date-price")
                        .param("dateFrom", DATE_NOW.minusDays(1L).toString())
                        .param("price", String.valueOf(PRICE)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<OrderDetailsDto> list = objectMapper.readValue(asString, new TypeReference<>() {
        });

        assertNotNull(list);
        list.forEach(x -> {
            assertTrue(x.getTotalAmount() >= PRICE);
            assertTrue(x.getOrderDate().isAfter(DATE_NOW.minusDays(1L)));
        });
    }

    @Test
    @SneakyThrows
    public void getOtherOrders_shouldReturnOrderDetailsDtoList() {
        List<OrderDetailsDto> dtoList = TestUtils.provideListOrderDetailsDto(NAME);
        when(orderService.getOtherOrders(any(OrderOthersDto.class))).thenReturn(dtoList);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/other")
                        .param("dateFrom", DATE_FROM.toString())
                        .param("dateTo", DATE_NOW.toString())
                        .param("itemName", dtoList.get(0).getItemName()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<OrderDetailsDto> list = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertNotNull(list);
        list.forEach(x -> {
            assertEquals(x.getItemName(), NAME);
            assertTrue(x.getOrderDate().isAfter(DATE_FROM));
            assertTrue(x.getOrderDate().isBefore(DATE_NOW));
        });
    }

    @Test
    @SneakyThrows
    public void createOrder_shouldCreateOrder() {
        OrderCreateDto orderCreateDto = TestUtils.provideOrderCreateDto();
        orderCreateDto.setItemName(NAME);
        orderCreateDto.setArticle(ARTICLE);
        orderCreateDto.setPrice(PRICE);
        orderCreateDto.setAddress(ADDRESS);
        orderCreateDto.setQuantity(QUANTITY);
        orderCreateDto.setDeliveryType(DeliveryType.SELF);
        orderCreateDto.setPaymentType(PaymentType.CARD);
        orderCreateDto.setCustomerName(CUSTOMER_NAME);

        OrderDto orderDto = TestUtils.provideOrderDto();
        orderDto.setOrderNumber(ORDER_NUMBER_VALUE);
        orderDto.setOrderDate(DATE_NOW);
        orderDto.setAddress(ADDRESS);
        orderDto.setDeliveryType(DeliveryType.SELF);
        orderDto.setPaymentType(PaymentType.CARD);
        orderDto.setCustomerName(CUSTOMER_NAME);

        String value = objectMapper.writeValueAsString(orderCreateDto);

        when(orderService.createOrder(any(OrderCreateDto.class))).thenReturn(orderDto);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .post("/add-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDto result = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(result.getCustomerName(), CUSTOMER_NAME);
        assertEquals(result.getOrderDate(), DATE_NOW);
        assertEquals(result.getAddress(), ADDRESS);
        assertEquals(result.getTotalAmount(), QUANTITY * PRICE);
        assertEquals(result.getDeliveryType(), DeliveryType.SELF);
        assertEquals(result.getPaymentType(), PaymentType.CARD);
    }
}
