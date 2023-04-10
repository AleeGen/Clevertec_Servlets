package ru.clevertec.cheque.dao;

import java.util.List;

public interface EntityDAO<T> {

    List<T> getAll();

    List<T> getAll(int numberPage, int pageSize);

    T getById(Integer id);

    boolean save(T t);

    boolean delete(Integer id);

    boolean update(T t);
}