package ru.clevertec.cheque.controller;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.cheque.entity.Product;
import ru.clevertec.cheque.service.impl.ProductService;

import static ru.clevertec.cheque.controller.Parameters.*;

@WebServlet(value = "/products")
public class ProductController extends HttpServlet {

    private final int STATUS_OK = HttpServletResponse.SC_OK;
    private final int STATUS_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(ID);
        String numberPage = req.getParameter(PAGE);
        String pageSize = req.getParameter(PAGE_SIZE);
        Object result = Objects.isNull(id) ? ProductService.getInstance().getAll(numberPage, pageSize) :
                ProductService.getInstance().getById(Integer.parseInt(id));
        ObjectMapper mapper = new ObjectMapper();
        resp.getWriter().println(mapper.writeValueAsString(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        Product product = new ObjectMapper().readValue(body, Product.class);
        int codeStatus = ProductService.getInstance().save(product) ? STATUS_OK : STATUS_ERROR;
        resp.setStatus(codeStatus);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        Product product = new ObjectMapper().readValue(body, Product.class);
        int codeStatus = ProductService.getInstance().update(product) ? STATUS_OK : STATUS_ERROR;
        resp.setStatus(codeStatus);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter(ID));
        int codeStatus = ProductService.getInstance().delete(id) ? STATUS_OK : STATUS_ERROR;
        resp.setStatus(codeStatus);
    }

}