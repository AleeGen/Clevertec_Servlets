package ru.clevertec.cheque.listener;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.yaml.snakeyaml.Yaml;
import ru.clevertec.cheque.dao.ConnectionPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger();
    private final String PATH_PROPERTY =
            ServletContextListenerImpl.class.getClassLoader().getResource("application.yml").getPath();
    private final String PATH_INIT_DB = "changelog/init_db.yml";
    private final String IS_INIT_DB = "is_init_db";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initPool();
        try (FileInputStream fileInputStream = new FileInputStream(PATH_PROPERTY)) {
            Map<String, Object> data = new Yaml().load(fileInputStream);
            if ((boolean) data.get(IS_INIT_DB)) {
                initBD();
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().destroyPool();
        logger.log(Level.INFO, "Servlet context destroyed");
    }

    private void initPool() {
        ConnectionPool.getInstance();
        logger.log(Level.INFO, "Servlet context initialized");
    }

    private void initBD() {
        try (Liquibase liquibase = new Liquibase(PATH_INIT_DB,
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(ConnectionPool.getInstance().getConnection()))) {
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            logger.fatal("Database initialization error", e);
            throw new ExceptionInInitializerError(e.getMessage());
        }
    }

}