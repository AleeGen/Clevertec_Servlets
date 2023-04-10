package ru.clevertec.cheque.service.impl;

import org.yaml.snakeyaml.Yaml;
import ru.clevertec.cheque.dao.impl.ProductDAO;
import ru.clevertec.cheque.entity.Product;
import ru.clevertec.cheque.service.EntityService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductService implements EntityService<Product> {
    private static final ProductService instance = new ProductService();
    private static final int defaultNumberPage;
    private static final int defaultPageSize;

    static {
        String path = CardService.class.getClassLoader().getResource("application.yml").getPath();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            Map<String, Object> data = new Yaml().load(fileInputStream);
            defaultPageSize = Integer.parseInt(((Map<?, ?>) data.get("pagination")).get("page_size").toString());
            defaultNumberPage = Integer.parseInt(((Map<?, ?>) data.get("pagination")).get("number_page").toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductService() {
    }

    public static ProductService getInstance() {
        return instance;
    }

    @Override
    public List<Product> getAll() {
        return ProductDAO.getInstance().getAll();
    }

    @Override
    public List<Product> getAll(String numberPage, String pageSize) {
        int pageSizeInt = Objects.isNull(pageSize) ? defaultPageSize : Integer.parseInt(pageSize);
        int numberPageInt = Objects.isNull(numberPage) ? defaultNumberPage : Integer.parseInt(numberPage);
        return ProductDAO.getInstance().getAll(numberPageInt, pageSizeInt);
    }

    @Override
    public Product getById(int id) {
        return ProductDAO.getInstance().getById(id);
    }

    @Override
    public boolean save(Product product) {
        return ProductDAO.getInstance().save(product);
    }

    @Override
    public boolean delete(int id) {
        return ProductDAO.getInstance().delete(id);
    }

    @Override
    public boolean update(Product product) {
        return ProductDAO.getInstance().update(product);
    }

}