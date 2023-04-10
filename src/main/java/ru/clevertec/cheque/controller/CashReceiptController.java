package ru.clevertec.cheque.controller;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import ru.clevertec.cheque.entity.CashReceipt;
import ru.clevertec.cheque.service.CashReceiptService;

import java.io.IOException;
import java.util.Arrays;

import static ru.clevertec.cheque.controller.Parameters.CARD;
import static ru.clevertec.cheque.controller.Parameters.ID;

@WebServlet(value = "/cashReceipt")
public class CashReceiptController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] itemIdStr = request.getParameterValues(ID);
        Integer[] itemId = Arrays.stream(itemIdStr).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        String cardStr = request.getParameter(CARD);
        Integer card = Integer.parseInt(cardStr);
        CashReceipt cashReceipt = CashReceiptService.getInstance().getCashReceipt(itemId, card);
        byte[] pdf = CashReceiptService.getInstance().getPdfCashReceipt(cashReceipt);
        response.getOutputStream().write(pdf);
    }

}