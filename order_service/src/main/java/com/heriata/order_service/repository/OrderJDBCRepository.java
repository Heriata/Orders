package com.heriata.order_service.repository;

import com.heriata.order_service.dto.OrderDateAndPriceDto;
import com.heriata.order_service.dto.OrderDetailsDto;
import com.heriata.order_service.dto.OrderOthersDto;
import com.heriata.order_service.enums.DeliveryType;
import com.heriata.order_service.enums.PaymentType;
import com.heriata.order_service.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order save(Order order) {
        String query = """
                                insert into orders (order_number, total_amount, order_date,
                                                    customer_name, address,payment_type,delivery_type)
                                values (?,?,?,?,?,?,?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, order.getOrderNumber());
                ps.setLong(2, order.getTotalAmount());
                ps.setObject(3, order.getOrderDate());
                ps.setString(4, order.getCustomerName());
                ps.setString(5, order.getAddress());
                ps.setString(6, order.getPaymentType().name());
                ps.setString(7, order.getDeliveryType().name());
                return ps;
            }, keyHolder);

            order.setOrderId((Long) keyHolder.getKeyList().get(0).get("id"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    //todo refactor to remove list workaround
    public OrderDetailsDto findByOrderId(Long id) {
        String query = """
                select * from orders o
                join public.order_details od on o.id = od.order_id
                where o.id = ?
                """;

        OrderDetailsDto orderDetailsDto;
        try {
            RowMapper<OrderDetailsDto> rowMapper = (rs, rowNum) -> provideOrderDetailsDto(rs);
            orderDetailsDto = jdbcTemplate.query(query, rowMapper, id).get(0);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return orderDetailsDto;
    }

    public List<OrderDetailsDto> findByDateAndPrice(OrderDateAndPriceDto dto) {
        String query = """
                select * from orders o
                join public.order_details od on o.id = od.order_id
                where o.order_date >= ? AND ((od.price * od.quantity) >= ?)
                """;

        List<OrderDetailsDto> orders;
        try {
            RowMapper<OrderDetailsDto> rowMapper = (rs, rowNum) -> provideOrderDetailsDto(rs);
            orders = jdbcTemplate.query(query, rowMapper, dto.getOrderDate(), dto.getOrderPrice());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

    public List<OrderDetailsDto> findByDateExcludingItemName(OrderOthersDto dto) {
        String query = """
                select * from orders o
                join public.order_details od on o.id = od.order_id
                where (o.order_date between ? and ?) and (od.item_name != ?)
                """;

        List<OrderDetailsDto> orders;
        try {
            RowMapper<OrderDetailsDto> rowMapper = (rs, rowNum) -> provideOrderDetailsDto(rs);
            orders = jdbcTemplate.query(query,  rowMapper, dto.getDateFrom(), dto.getDateTo(), dto.getItemName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    private static OrderDetailsDto provideOrderDetailsDto(ResultSet rs) throws SQLException {
        OrderDetailsDto order = new OrderDetailsDto();
        order.setOrderId(rs.getLong("id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setTotalAmount(rs.getLong("total_amount"));
        order.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
        order.setCustomerName(rs.getString("customer_name"));
        order.setAddress(rs.getString("address"));
        order.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
        order.setDeliveryType(DeliveryType.valueOf(rs.getString("delivery_type")));

        order.setArticle(rs.getLong("article"));
        order.setItemName(rs.getString("item_name"));
        order.setQuantity(rs.getLong("quantity"));
        order.setPrice(rs.getLong("price"));
        return order;
    }
}
