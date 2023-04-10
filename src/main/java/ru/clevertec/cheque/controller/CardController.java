package ru.clevertec.cheque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.cheque.entity.DiscountCard;
import ru.clevertec.cheque.service.impl.CardService;

import java.io.IOException;
import java.util.stream.Collectors;

import static ru.clevertec.cheque.controller.Parameters.*;

@WebServlet(value = "/cards")
public class CardController extends HttpServlet {

    private final int STATUS_OK = HttpServletResponse.SC_OK;
    private final int STATUS_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String card = req.getParameter(CARD);
        String numberPage = req.getParameter(PAGE);
        String pageSize = req.getParameter(PAGE_SIZE);
        Object result = card == null ? CardService.getInstance().getAll(numberPage, pageSize) :
                CardService.getInstance().getById(Integer.parseInt(card));
        ObjectMapper mapper = new ObjectMapper();
        resp.getWriter().println(mapper.writeValueAsString(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        DiscountCard card = new ObjectMapper().readValue(body, DiscountCard.class);
        int codeStatus = CardService.getInstance().save(card) ? STATUS_OK : STATUS_ERROR;
        resp.setStatus(codeStatus);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        DiscountCard card = new ObjectMapper().readValue(body, DiscountCard.class);
        int codeStatus = CardService.getInstance().update(card) ? STATUS_OK : STATUS_ERROR;
        resp.setStatus(codeStatus);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int card = Integer.parseInt(req.getParameter(CARD));
        int codeStatus = CardService.getInstance().delete(card) ? STATUS_OK : STATUS_ERROR;
        resp.setStatus(codeStatus);
    }

}