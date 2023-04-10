package ru.clevertec.cheque.entity;

import lombok.Data;

@Data
public class Position {
    private final int quantity;
    private final Product product;
    private final double cost;
    private double totalCost;

    public Position(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
        this.cost = calculate();
        this.totalCost = this.cost;
    }

    private double calculate() {
        return quantity * product.getPrice();
    }
}