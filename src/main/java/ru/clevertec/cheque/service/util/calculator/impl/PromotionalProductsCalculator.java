package ru.clevertec.cheque.service.util.calculator.impl;

import ru.clevertec.cheque.entity.CashReceipt;
import ru.clevertec.cheque.entity.Position;
import ru.clevertec.cheque.service.util.calculator.CashReceiptCalculator;

public class PromotionalProductsCalculator implements CashReceiptCalculator {
    private static final int REQUIRED_NUMBER_PRODUCTS_TO_DISCOUNT = 5;
    private static final double DISCOUNT = 0.1;

    @Override
    public CashReceipt calculate(CashReceipt cashReceipt) {
        double totalCost = cashReceipt.getTotalCost();
        for (Position position : cashReceipt.getPositions()) {
            int quantity = position.getQuantity();
            if (position.getProduct().isPromotional() && quantity > REQUIRED_NUMBER_PRODUCTS_TO_DISCOUNT) {
                double cost = position.getCost();
                position.setTotalCost(cost - cost * DISCOUNT);
                totalCost += position.getTotalCost() - cost;
            }
        }
        cashReceipt.setTotalCost(totalCost);
        return cashReceipt;
    }
}
