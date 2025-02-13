package com.heriata.order_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderDto;
import com.heriata.order_service.rest_client.NumbersServiceClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(classes = TestContainerConfiguration.class)
public class MainControllerIntegrationTest {

    private static final String ORDER_NUMBER = "0000320250211";
    private static final long ORDER_ID = 1L;
    private static final long TOTAL_PRICE = 600L;
    private static final int ORDERS_FOR_DATE_PRICE = 5;
    private static final String DATE_FROM = "2025-02-11T00:00:00";
    private static final String DATE_TO = "2025-02-12T00:00:00";
    private static final String ITEM_NAME_EXCLUDE = "chicken";
    private static final int ORDERS_FOR_NAME_EXCLUDED = 3;
    private static final String MOCK_ORDER_NUMBER = "8888812345678";

    @MockitoBean
    private final NumbersServiceClient numbersServiceClient;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    MainControllerIntegrationTest(NumbersServiceClient numbersServiceClient,
                                  MockMvc mockMvc,
                                  ObjectMapper objectMapper) {
        this.numbersServiceClient = numbersServiceClient;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setUp() {
        when(numbersServiceClient.getOrderNumber()).thenReturn(MOCK_ORDER_NUMBER);
    }

    @Test
    @SneakyThrows
    public void getByOrderNumber_shouldReturnOrderDetails() {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/by-number")
                        .param("orderNumber", ORDER_NUMBER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDetailsDto dto = objectMapper.readValue(contentAsString,
                new TypeReference<>() {
                });

        assertNotNull(dto);
        assertEquals(dto.getOrderNumber(), ORDER_NUMBER);
    }

    @Test
    @SneakyThrows
    public void getByOrderId_shouldReturnOrderDetails() {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/by-id")
                        .param("orderId", String.valueOf(ORDER_ID)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDetailsDto dto = objectMapper.readValue(contentAsString,
                new TypeReference<>() {
                });

        assertNotNull(dto);
        assertEquals(dto.getOrderId(), ORDER_ID);
    }

    @Test
    @SneakyThrows
    public void getByDateAndPrice_shouldReturnOrderDetailsList() {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/date-price")
                        .param("dateFrom", DATE_FROM)
                        .param("price", String.valueOf(TOTAL_PRICE)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<OrderDetailsDto> list = objectMapper.readValue(contentAsString,
                new TypeReference<>() {
                });

        assertNotNull(list);
        assertEquals(ORDERS_FOR_DATE_PRICE, list.size());
        list.forEach(x -> {
            assertTrue(x.getOrderDate().isAfter(LocalDateTime.parse(DATE_FROM)));
            assertTrue(x.getTotalAmount() >= TOTAL_PRICE);
        });
    }

    @Test
    @SneakyThrows
    public void getOtherOrders_shouldReturnOrderDetailsList() {
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/other")
                        .param("dateFrom", DATE_FROM)
                        .param("dateTo", DATE_TO)
                        .param("itemName", ITEM_NAME_EXCLUDE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<OrderDetailsDto> list = objectMapper.readValue(contentAsString,
                new TypeReference<>() {
                });

        assertNotNull(list);
        assertEquals(ORDERS_FOR_NAME_EXCLUDED, list.size());
        list.forEach(x -> {
            assertTrue(x.getOrderDate().isAfter(LocalDateTime.parse(DATE_FROM)));
            assertTrue(x.getOrderDate().isBefore(LocalDateTime.parse(DATE_TO)));
            assertNotEquals(x.getItemName(), ITEM_NAME_EXCLUDE);
        });
    }

    @Test
    @SneakyThrows
    public void createOrder_shouldReturnOrderDto() {
        OrderCreateDto dto = TestUtils.provideOrderCreateDto();
        String value = objectMapper.writeValueAsString(dto);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .post("/add-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDto order = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertNotNull(order);
        assertEquals(dto.getAddress(), order.getAddress());
        assertEquals(dto.getPaymentType(), order.getPaymentType());
        assertEquals(dto.getDeliveryType(), order.getDeliveryType());
        assertEquals(dto.getCustomerName(), order.getCustomerName());
    }
}
