package ru.clevertec.cheque.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.properties.TextAlignment;
import ru.clevertec.cheque.dao.impl.CardDAO;
import ru.clevertec.cheque.dao.impl.ProductDAO;
import ru.clevertec.cheque.entity.*;
import ru.clevertec.cheque.exception.CashReceiptException;
import ru.clevertec.cheque.service.util.CashReceiptPdf;
import ru.clevertec.cheque.service.util.calculator.impl.DiscountCardCalculator;
import ru.clevertec.cheque.service.util.calculator.impl.PromotionalProductsCalculator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class CashReceiptService {

    private static final CashReceiptService instance = new CashReceiptService();
    private final String BACKGROUND =
            CashReceiptService.class.getClassLoader().getResource("Clevertec_Template.pdf").getPath();
    private final Organization organization;

    {
        organization = Organization.builder()
                .name("Clevertec")
                .address("90 Dubninskaya Street, Moscow, Russia")
                .email("info@clevertec.ru")
                .telephone("+7 (499) 653 94 51")
                .build();
    }

    private CashReceiptService() {
    }

    public static CashReceiptService getInstance() {
        return instance;
    }

    public CashReceipt getCashReceipt(Integer[] itemId, Integer numberCard) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer id : itemId) {
            if (map.containsKey(id)) {
                map.put(id, map.get(id) + 1);
            } else {
                map.put(id, 1);
            }
        }
        List<Position> positions = new ArrayList<>();
        for (Integer id : map.keySet()) {
            Product product = ProductDAO.getInstance().getById(id);
            if (product == null) {
                throw new CashReceiptException(String.format("Product with id = %d not found", id));
            }
            positions.add(new Position(map.get(id), product));
        }
        Optional<DiscountCard> card;
        if (numberCard == null) {
            card = Optional.empty();
        } else {
            DiscountCard c = CardDAO.getInstance().getById(numberCard);
            if (c == null) {
                throw new CashReceiptException(String.format("Discount card with number = %d not found", numberCard));
            }
            card = Optional.of(c);
        }
        CashReceipt cashReceipt = new CashReceipt.CashReceiptBuilder(positions)
                .addDiscountCard(card)
                .addOrganization(organization)
                .addDate(new Date())
                .build();
        new PromotionalProductsCalculator().calculate(cashReceipt);
        new DiscountCardCalculator().calculate(cashReceipt);
        return cashReceipt;
    }

    public byte[] getPdfCashReceipt(CashReceipt cashReceipt) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PdfDocument pdfDocument = new PdfDocument(new PdfReader(BACKGROUND), new PdfWriter(baos));
             Document document = new Document(pdfDocument, PageSize.A4.rotate())) {
            CashReceiptPdf cashReceiptPdf = new CashReceiptPdf();
            Div div = new Div()
                    .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setRelativePosition(0, 180, 0, 50)
                    .setBorderBottom(new SolidBorder(Border.SOLID))
                    .add(cashReceiptPdf.addHeader(cashReceipt))
                    .add(cashReceiptPdf.addBody(cashReceipt))
                    .add(cashReceiptPdf.addFooter(cashReceipt));
            document.add(div);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new CashReceiptException("Error when creating pdf");
        }
    }

}