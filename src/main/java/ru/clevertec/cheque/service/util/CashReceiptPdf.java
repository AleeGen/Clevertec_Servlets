package ru.clevertec.cheque.service.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import ru.clevertec.cheque.entity.CashReceipt;
import ru.clevertec.cheque.entity.Organization;
import ru.clevertec.cheque.entity.Position;

import java.io.IOException;
import java.text.DecimalFormat;

public class CashReceiptPdf {

    private static final DecimalFormat FORMAT = new DecimalFormat("#0.00");
    private static final String HEADER = "CASH RECEIPT";
    private static final String DISCOUNT = "Discount: ";
    private static final String COST = "Cost: ";
    private static final String TOTAL_COST = "TOTAL COST: ";
    private static final String COLUMN_1 = "N";
    private static final String COLUMN_2 = "Product";
    private static final String COLUMN_3 = "Price";
    private static final String COLUMN_4 = "Cost";
    private static final float HEADER_SIZE = 20f;
    private static final float CLR_HEADER = 0.8f;
    private static final int NUMBER_COLUMNS = 40;
    private static final int WIDTH_N = (int) (NUMBER_COLUMNS * 0.05);
    private static final int WIDTH_PRODUCT = (int) (NUMBER_COLUMNS * 0.6);
    private static final int WIDTH_PRICE = (int) (NUMBER_COLUMNS * 0.1);
    private static final int WIDTH_COST = (int) (NUMBER_COLUMNS * 0.25);

    public IBlockElement addHeader(CashReceipt cashReceipt) throws IOException {
        Table table = new Table(NUMBER_COLUMNS, true);
        Cell header = new Cell(1, NUMBER_COLUMNS)
                .add(new Paragraph(HEADER))
                .setBorder(Border.NO_BORDER)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)).setFontSize(HEADER_SIZE);
        Organization org = cashReceipt.getOrganization();
        Cell body = new Cell(1, NUMBER_COLUMNS).add(new List().setListSymbol("")
                        .add(org.getName())
                        .add(org.getAddress())
                        .add(org.getTelephone())
                        .add(org.getEmail()))
                .setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
        Cell footer = new Cell(1, NUMBER_COLUMNS)
                .add(new Paragraph(cashReceipt.getDate().toString()))
                .setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
        return table.addCell(header).addCell(body).addCell(footer);
    }

    public IBlockElement addBody(CashReceipt cashReceipt) {
        Table innerTable = new Table(NUMBER_COLUMNS, true)
                .addCell(new Cell(1, WIDTH_N).setBackgroundColor(new DeviceGray(CLR_HEADER)).add(new Paragraph(COLUMN_1)))
                .addCell(new Cell(1, WIDTH_PRODUCT).setBackgroundColor(new DeviceGray(CLR_HEADER)).add(new Paragraph(COLUMN_2)))
                .addCell(new Cell(1, WIDTH_PRICE).setBackgroundColor(new DeviceGray(CLR_HEADER)).add(new Paragraph(COLUMN_3)))
                .addCell(new Cell(1, WIDTH_COST).setBackgroundColor(new DeviceGray(CLR_HEADER)).add(new Paragraph(COLUMN_4)));
        for (Position p : cashReceipt.getPositions()) {
            double cost = p.getCost();
            double totalCost = p.getTotalCost();
            Cell cellTotalCost = new Cell(1, WIDTH_COST).add(new Paragraph(FORMAT.format(totalCost)));
            if (cost != totalCost) {
                cellTotalCost.add(new Paragraph(DISCOUNT + FORMAT.format(cost - totalCost)));
            }
            innerTable.addCell(new Cell(1, WIDTH_N).add(new Paragraph(String.valueOf(p.getQuantity()))))
                    .addCell(new Cell(1, WIDTH_PRODUCT).add(new Paragraph(p.getProduct().getDescription())))
                    .addCell(new Cell(1, WIDTH_PRICE).add(new Paragraph(FORMAT.format(p.getProduct().getPrice()))))
                    .addCell(cellTotalCost);
        }
        return new Div().setBorderBottom(new SolidBorder(Border.SOLID)).add(innerTable);
    }

    public IBlockElement addFooter(CashReceipt cashReceipt) {
        double cost = cashReceipt.getCost();
        double totalCost = cashReceipt.getTotalCost();
        List list = new List().setListSymbol("")
                .setBorder(new SolidBorder(Border.SOLID))
                .setTextAlignment(TextAlignment.LEFT)
                .add(COST + FORMAT.format(cost));
        if (cost != totalCost) {
            list.add(DISCOUNT + FORMAT.format(cost - totalCost));
        }
        return list.add(TOTAL_COST + FORMAT.format(totalCost));
    }

}