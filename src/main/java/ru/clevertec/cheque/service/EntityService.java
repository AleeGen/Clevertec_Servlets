package ru.clevertec.cheque.service;

import java.util.List;

public interface EntityService<T> {
    List<T> getAll();

    List<T> getAll(String numberPage, String pageSize);

    T getById(int id);

    boolean save(T t);

    boolean delete(int id);

    boolean update(T t);
}