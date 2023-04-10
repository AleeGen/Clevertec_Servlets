package ru.clevertec.cheque.dao;

public final class DatabaseQuery {

    private DatabaseQuery() {
    }

    public static final String GET_PRODUCTS = "select * from custom.product";
    public static final String GET_PRODUCTS_BY_PAGE = "select * from custom.product limit ? offset ?";
    public static final String GET_PRODUCT = "select * from custom.product where id = ?";
    public static final String SAVE_PRODUCT = "insert into custom.product (description, price, promotional) values (?,?,?)";
    public static final String DELETE_PRODUCT = "delete from custom.product where id = ?";
    public static final String UPDATE_PRODUCT = "update custom.product set description = ?, price = ?, promotional = ? where id = ?";
    public static final String GET_CARDS = "select * from custom.discount_card";
    public static final String GET_CARDS_BY_PAGE = "select * from custom.discount_card limit ? offset ?";
    public static final String GET_CARD = "select * from custom.discount_card where number = ?";
    public static final String SAVE_CARD = "insert into custom.discount_card (number, discount) values (?,?)";
    public static final String DELETE_CARD = "delete from custom.discount_card where number = ?";
    public static final String UPDATE_CARD = "update custom.discount_card set discount = ? where number = ?";
}