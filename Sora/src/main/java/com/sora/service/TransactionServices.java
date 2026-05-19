package com.sora.service;

import com.sora.model.Transaction;
import com.sora.util.FileHandler;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class handling financial transaction records operations, including logging additions,
 * calculating total income/expense aggregates, categories distribution metrics, and storage persistence.
 */
public class TransactionServices {
    private List<Transaction> transactions;
    private FileHandler fileHandler;
    private String userId;

    /**
     * Initializes the transactions service for the specified user and loads transaction logs.
     */
    public TransactionServices(String userId) {
        this.userId = userId;
        this.fileHandler = new FileHandler();
        try {
            this.transactions = fileHandler.loadTransactions(userId);
        } catch (IOException e) {
            System.out.println("Could not load transactions");
            this.transactions = new ArrayList<>();
        }
    }

    /**
     * Registers a new financial transaction and saves the change to local storage.
     */
    public boolean addTransaction(Transaction transaction) {
        boolean added = transactions.add(transaction);
        saveToFile();
        return added;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    /**
     * Calculates the sum of all transaction logs flagged as 'Income'.
     */
    public double getTotalIncome() {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Calculates the sum of all transaction logs flagged as 'Expense'.
     */
    public double getTotalSpent() {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Expense")) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Calculates the sum of expense transactions matching the specified budget category.
     */
    public double getSpendingByCategory(String category) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Expense")
                    && transaction.getCategory().equalsIgnoreCase(category)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    /**
     * Synchronizes and saves the transaction logs list to local disk storage.
     */
    public void saveToFile() {
        try {
            fileHandler.saveTransactions(userId, transactions);
        } catch (IOException e) {
            System.out.println("Could not save transactions");
        }
    }
}