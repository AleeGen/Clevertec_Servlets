package ru.clevertec.cheque.service.util.printing;

public interface Printer<T> {
    String print(T t);
}
