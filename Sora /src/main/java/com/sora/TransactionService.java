package com.sora;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class TransactionService {
    private FileHandler filehandler;
    private List<Transaction> transactions;

    TransactionService() {
        try {
            this.filehandler = new FileHandler();
            this.transactions = filehandler.loadTransactions();
        } catch (IOException e) {
            System.out.println("Could not load transaction");
            this.transactions = new ArrayList<>();
        }
    }

    public boolean addTransaction(String userId, String type, double amount, String category,
            String description, String date) {
        String transactionID = "TXN-" + System.currentTimeMillis();
        Transaction transaction;
        if (type.equals("INCOME")) {
            transaction = new IncomeTransaction(transactionID, userId, type, amount, category,
                    description, date);
        } else {
            transaction = new ExpenseTransaction(transactionID, userId, type, amount, category,
                    description, date);
        }

        this.transactions.add(transaction);

        try {
            filehandler.saveTransactions(transactions);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving a file: " + e.getMessage());
            return false;
        }
    }

    public List<Transaction> getTransactionsByUser(String userId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getUserId().equals(userId)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }
}
