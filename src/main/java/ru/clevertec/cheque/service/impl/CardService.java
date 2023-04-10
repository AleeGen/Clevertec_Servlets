package ru.clevertec.cheque.service.impl;

import org.yaml.snakeyaml.Yaml;
import ru.clevertec.cheque.dao.impl.CardDAO;
import ru.clevertec.cheque.entity.DiscountCard;
import ru.clevertec.cheque.service.EntityService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CardService implements EntityService<DiscountCard> {

    private static final CardService instance = new CardService();
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

    private CardService() {
    }

    public static CardService getInstance() {
        return instance;
    }

    @Override
    public List<DiscountCard> getAll() {
        return CardDAO.getInstance().getAll();
    }

    public List<DiscountCard> getAll(String numberPage, String pageSize) {
        int pageSizeInt = Objects.isNull(pageSize) ? defaultPageSize : Integer.parseInt(pageSize);
        int numberPageInt = Objects.isNull(numberPage) ? defaultNumberPage : Integer.parseInt(numberPage);
        return CardDAO.getInstance().getAll(numberPageInt, pageSizeInt);
    }

    @Override
    public DiscountCard getById(int number) {
        return CardDAO.getInstance().getById(number);
    }

    @Override
    public boolean save(DiscountCard discountCard) {
        return CardDAO.getInstance().save(discountCard);
    }

    @Override
    public boolean delete(int number) {
        return CardDAO.getInstance().delete(number);
    }

    @Override
    public boolean update(DiscountCard discountCard) {
        return CardDAO.getInstance().update(discountCard);
    }

}