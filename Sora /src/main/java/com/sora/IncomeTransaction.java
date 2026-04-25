package com.sora;

public class IncomeTransaction extends Transaction {

    IncomeTransaction(String transactionId, String userId, String type, double amount,
            String category, String description, String date) {
        super(transactionId, userId, type, amount, category, description, date);
    }

    @Override
    public String getTransactionCategory() {
        return "This is an income transaction";
    }
}
