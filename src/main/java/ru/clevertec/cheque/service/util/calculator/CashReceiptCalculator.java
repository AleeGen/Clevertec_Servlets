package ru.clevertec.cheque.service.util.calculator;

import ru.clevertec.cheque.entity.CashReceipt;

public interface CashReceiptCalculator {
    CashReceipt calculate(CashReceipt cashReceipt);
}
