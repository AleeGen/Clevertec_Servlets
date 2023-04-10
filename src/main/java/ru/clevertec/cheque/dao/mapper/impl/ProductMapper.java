package ru.clevertec.cheque.dao.mapper.impl;

import ru.clevertec.cheque.dao.mapper.Mapper;
import ru.clevertec.cheque.entity.Product;
import ru.clevertec.cheque.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements Mapper<Product> {

    private static ProductMapper instance;

    public static ProductMapper getInstance() {
        if (instance == null) {
            instance = new ProductMapper();
        }
        return instance;
    }

    private ProductMapper() {
    }

    @Override
    public Product rowMap(ResultSet resultSet) {
        try {
            return Product.builder()
                    .id(resultSet.getInt("id"))
                    .description(resultSet.getString("description"))
                    .price(resultSet.getDouble("price"))
                    .promotional(resultSet.getBoolean("promotional"))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

}