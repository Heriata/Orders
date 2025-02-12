package com.heriata.order_service;

import com.heriata.order_service.dto.OrderCreateDto;
import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import com.heriata.order_service.model.Details;
import com.heriata.order_service.model.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {

    public static final String ITEM_NAME_EXCLUDE = "exclude";
    private static final String CUSTOMER_NAME = "name";
    private static final String ADDRESS = "address";
    private static final String ITEM_NAME = "itemName";
    private static final String ORDER_NUMBER = "111112025";

    private static final String numbers = "1234567890";
    private static final long QUANTITY = 10L;
    private static final long PRICE = 5L;
    private static final long TOTAL_AMOUNT = 100L;

    private static final long DETAILS_ID = 1L;
    private static final long ORDER_ID = 1L;
    private static final long ARTICLE = 123L;
    private static final long seed = 100L;


    static OrderCreateDto provideOrderCreateDto() {
        return OrderCreateDto.builder()
                .customerName(CUSTOMER_NAME)
                .address(ADDRESS)
                .paymentType(PaymentType.CARD)
                .deliveryType(DeliveryType.DOOR)
                .article(ARTICLE)
                .itemName(ITEM_NAME_EXCLUDE)
                .quantity(QUANTITY)
                .price(PRICE)
                .build();
    }

    static Order provideOrder() {
        return Order.builder()
                .orderId(ORDER_ID)
                .totalAmount(TOTAL_AMOUNT)
                .orderDate(LocalDateTime.now())
                .customerName(CUSTOMER_NAME)
                .address(ADDRESS)
                .paymentType(PaymentType.CARD)
                .deliveryType(DeliveryType.DOOR)
                .build();
    }

    static Details provideDetails() {
        return Details.builder()
                .detailsId(DETAILS_ID)
                .orderId(ORDER_ID)
                .article(ARTICLE)
                .itemName(ITEM_NAME_EXCLUDE)
                .price(PRICE)
                .quantity(QUANTITY)
                .build();
    }

    static OrderDetailsDto providedOrderDetailsDto(long seed) {
        return OrderDetailsDto.builder()
                .orderId(ORDER_ID)
                .orderNumber(getOrderNumber(seed))
                .totalAmount(PRICE * QUANTITY)
                .orderDate(LocalDateTime.now().minusHours(12))
                .customerName(CUSTOMER_NAME)
                .address(ADDRESS)
                .paymentType(PaymentType.CARD)
                .deliveryType(DeliveryType.SELF)
                .article(ARTICLE)
                .itemName(ITEM_NAME_EXCLUDE)
                .quantity(QUANTITY)
                .price(PRICE)
                .build();
    }

    private static OrderDetailsDto providedOrderDetailsDto(long seed, String itemName) {
        OrderDetailsDto orderDetailsDto = providedOrderDetailsDto(seed);
        orderDetailsDto.setItemName(itemName);
        return orderDetailsDto;
    }

    static OrderDateAndPriceDto providedOrderDateAndPriceDto(Long total) {
        return OrderDateAndPriceDto.builder()
                .orderDate(LocalDateTime.now().minusDays(1))
                .orderTotalAmount(total)
                .build();
    }

    static long getSeed() {
        return seed;
    }

    static String getOrderNumber(long seed) {
        Random random = new Random(seed);
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            number.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = LocalDateTime.now().format(formatter);
        number.append(now);
        return number.toString();
    }
    public static List<OrderDetailsDto> provideListOrderDetailsDto() {
        List<OrderDetailsDto> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(TestUtils.providedOrderDetailsDto(TestUtils.getSeed()));
        }
        return list;
    }

    public static List<OrderDetailsDto> provideListOrderDetailsDto(String itemName) {
        List<OrderDetailsDto> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(TestUtils.providedOrderDetailsDto(TestUtils.getSeed(), itemName));
        }
        return list;
    }

    public static OrderOthersDto provideOrderOthersDto(String excluded) {
        return OrderOthersDto.builder()
                .itemName(excluded)
                .dateFrom(LocalDateTime.now().minusDays(1L))
                .dateTo(LocalDateTime.now().minusHours(1L))
                .build();
    }

}
