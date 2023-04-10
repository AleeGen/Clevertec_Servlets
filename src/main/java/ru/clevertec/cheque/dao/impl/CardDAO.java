package ru.clevertec.cheque.dao.impl;

import ru.clevertec.cheque.dao.ConnectionPool;
import ru.clevertec.cheque.dao.DatabaseQuery;
import ru.clevertec.cheque.dao.EntityDAO;
import ru.clevertec.cheque.dao.mapper.impl.CardMapper;
import ru.clevertec.cheque.entity.DiscountCard;
import ru.clevertec.cheque.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO implements EntityDAO<DiscountCard> {

    private static final CardDAO instance = new CardDAO();

    public static CardDAO getInstance() {
        return instance;
    }

    private CardDAO() {
    }

    @Override
    public List<DiscountCard> getAll() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.GET_CARDS);
             ResultSet resultSet = statement.executeQuery()) {
            List<DiscountCard> cards = new ArrayList<>();
            while (resultSet.next()) {
                cards.add(CardMapper.getInstance().rowMap(resultSet));
            }
            return cards;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public List<DiscountCard> getAll(int numberPage, int pageSize) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.GET_CARDS_BY_PAGE)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, numberPage * pageSize);
            List<DiscountCard> cards = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cards.add(CardMapper.getInstance().rowMap(resultSet));
                }
            }
            return cards;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public DiscountCard getById(Integer number) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.GET_CARD)) {
            statement.setInt(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return CardMapper.getInstance().rowMap(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean save(DiscountCard discountCard) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SAVE_CARD)) {
            statement.setInt(1, discountCard.getNumber());
            statement.setDouble(2, discountCard.getDiscount());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Integer number) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.DELETE_CARD)) {
            statement.setInt(1, number);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public boolean update(DiscountCard discountCard) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DatabaseQuery.UPDATE_CARD)) {
            statement.setDouble(1, discountCard.getDiscount());
            statement.setDouble(2, discountCard.getNumber());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

}