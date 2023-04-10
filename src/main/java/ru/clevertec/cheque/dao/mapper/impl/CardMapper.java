package ru.clevertec.cheque.dao.mapper.impl;

import ru.clevertec.cheque.dao.mapper.Mapper;
import ru.clevertec.cheque.entity.DiscountCard;
import ru.clevertec.cheque.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardMapper implements Mapper<DiscountCard> {

    private static CardMapper instance;

    public static CardMapper getInstance() {
        if (instance == null) {
            instance = new CardMapper();
        }
        return instance;
    }

    private CardMapper() {
    }

    @Override
    public DiscountCard rowMap(ResultSet resultSet) {
        try {
            return DiscountCard.builder()
                    .number(resultSet.getInt("number"))
                    .discount(resultSet.getDouble("discount"))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

}