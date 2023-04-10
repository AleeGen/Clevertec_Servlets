package ru.clevertec.cheque.dao;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger();
    private static ConnectionPool instance;
    private static final String PATH_PROPERTIES = "database.properties";
    private static final String SIZE_POOL = "size_pool";
    private static final String URL = "url";
    private static final Properties property = new Properties();
    private static final int CAPACITY_POOL;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final AtomicBoolean isCreated = new AtomicBoolean(false);
    private final BlockingQueue<Connection> free = new LinkedBlockingQueue<>(CAPACITY_POOL);
    private final BlockingQueue<Connection> used = new LinkedBlockingQueue<>(CAPACITY_POOL);

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            logger.fatal("Not register driver", e);
            throw new ExceptionInInitializerError(e.getMessage());
        }
        try {
            InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(PATH_PROPERTIES);
            property.load(inputStream);
            CAPACITY_POOL = Integer.parseInt(property.getProperty(SIZE_POOL));
        } catch (IOException e) {
            logger.fatal("Properties not load", e);
            throw new ExceptionInInitializerError(e.getMessage());
        }
    }

    private ConnectionPool() {
        for (int i = 0; i < CAPACITY_POOL; i++) {
            try {
                free.add(DriverManager.getConnection((String) property.get(URL), property));
            } catch (SQLException e) {
                logger.log(Level.WARN, "Exception when creating connection", e);
            }
        }
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            lock.lock();
            if (instance == null) {
                try {
                    instance = new ConnectionPool();
                    isCreated.set(true);
                    logger.log(Level.INFO, "ConnectionPool initialized");
                } finally {
                    lock.unlock();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = free.take();
            used.put(connection);
        } catch (InterruptedException e) {
            logger.error("Connection not release, thread interrupt", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        try {
            used.remove(connection);
            free.put(connection);
        } catch (InterruptedException e) {
            logger.error("Connection not release, thread interrupt", e);
            Thread.currentThread().interrupt();
        }
    }

    public void destroyPool() {
        for (int i = 0; i < CAPACITY_POOL; i++) {
            try {
                free.take().close();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Error when destroy pool", e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                logger.log(Level.ERROR, e);
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
                logger.log(Level.INFO, "Deregister driver");
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Error when deregister driver", e);
            }
        });
    }

}