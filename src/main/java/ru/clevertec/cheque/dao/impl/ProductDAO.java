package ru.clevertec.cheque.dao.impl;

import ru.clevertec.cheque.dao.ConnectionPool;
import ru.clevertec.cheque.dao.DatabaseQuery;
import ru.clevertec.cheque.dao.EntityDAO;
import ru.clevertec.cheque.dao.mapper.impl.ProductMapper;
import ru.clevertec.cheque.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import ru.clevertec.cheque.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO implements EntityDAO<Product> {

    private static final ProductDAO instance = new ProductDAO();

    public static ProductDAO getInstance() {
        return instance;
    }

    private ProductDAO() {
    }

    @Override
    public List<Product> getAll() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.GET_PRODUCTS);
             ResultSet resultSet = statement.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(ProductMapper.getInstance().rowMap(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public List<Product> getAll(int numberPage, int pageSize) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.GET_PRODUCTS_BY_PAGE)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, numberPage * pageSize);
            List<Product> products = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(ProductMapper.getInstance().rowMap(resultSet));
                }
            }
            return products;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Product getById(Integer id) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.GET_PRODUCT)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return ProductMapper.getInstance().rowMap(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean save(Product product) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SAVE_PRODUCT)) {
            statement.setString(1, product.getDescription());
            statement.setDouble(2, product.getPrice());
            statement.setBoolean(3, product.isPromotional());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Integer id) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.DELETE_PRODUCT)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean update(Product product) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.UPDATE_PRODUCT)) {
            statement.setString(1, product.getDescription());
            statement.setDouble(2, product.getPrice());
            statement.setBoolean(3, product.isPromotional());
            statement.setInt(4, product.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

}