package ru.clevertec.cheque.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CashReceipt {
    private final List<Position> positions;
    private final double cost;
    private Organization organization;
    private Date date;
    private Optional<DiscountCard> discountCard;
    private double totalCost;

    private CashReceipt(List<Position> positions) {
        this.positions = positions;
        this.cost = calculate();
        this.totalCost = cost;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Date getDate() {
        return date;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public double getCost() {
        return cost;
    }

    public Optional<DiscountCard> getDiscountCard() {
        return discountCard;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDiscountCard(Optional<DiscountCard> discountCard) {
        this.discountCard = discountCard;
    }

    @Override
    public String toString() {
        return "CashReceipt{" +
                "positions=" + positions +
                ", cost=" + cost +
                ", organization=" + organization +
                ", date=" + date +
                ", discountCard=" + discountCard +
                ", totalCost=" + totalCost +
                '}';
    }

    private double calculate() {
        double result = 0;
        for (Position position : positions) {
            result += position.getTotalCost();
        }
        return result;
    }


    public static class CashReceiptBuilder {

        private Organization organization;
        private Date date;
        private List<Position> positions;
        private Optional<DiscountCard> discountCard;

        public CashReceiptBuilder(List<Position> positions) {
            this.positions = positions;
        }

        public CashReceiptBuilder addOrganization(Organization organization) {
            this.organization = organization;
            return this;
        }

        public CashReceiptBuilder addDate(Date date) {
            this.date = date;
            return this;
        }

        public CashReceiptBuilder addDiscountCard(Optional<DiscountCard> discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public CashReceipt build() {
            CashReceipt cashReceipt = new CashReceipt(positions);
            cashReceipt.setOrganization(organization);
            cashReceipt.setDate(date);
            cashReceipt.setDiscountCard(discountCard);
            return cashReceipt;
        }

    }

}

