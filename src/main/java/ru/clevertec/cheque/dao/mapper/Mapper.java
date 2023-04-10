package ru.clevertec.cheque.dao.mapper;

import java.sql.ResultSet;

public interface Mapper<T> {

    T rowMap(ResultSet resultSet);
}