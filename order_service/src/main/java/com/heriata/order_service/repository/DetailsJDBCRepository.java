package com.heriata.order_service.repository;

import com.heriata.order_service.model.Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DetailsJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DetailsJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Details save(Details details) {
        String query = """
                insert into order_details(article, item_name, quantity, price, order_id)
                values (?,?,?,?,?)
                """;

        try {
            jdbcTemplate.update(query,
                    details.getArticle(),
                    details.getItemName(),
                    details.getQuantity(),
                    details.getPrice(),
                    details.getOrderId());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return details;
    }

}
