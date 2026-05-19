package com.sora.model;

import java.time.LocalDate;

/**
 * Model class representing a financial transaction.
 * Records the transaction's identifier, description, category, amount, timestamp, type (Expense/Income),
 * and associated payment source account.
 */
public class Transaction {
    private String transactionId;
    private String description;
    private String category;
    private double amount;
    private String localDate;
    private String type;
    private String accountName;

    /**
     * Constructs a new Transaction.
     */
    public Transaction(String transactionId, String description, String category, double amount, String localDate,
            String type, String accountName) {
        this.transactionId = transactionId;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.localDate = localDate;
        this.type = type;
        this.accountName = accountName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return localDate;
    }

    public String getType() {
        return type;
    }

    /**
     * Parses the string date representation into a LocalDate instance.
     */
    public LocalDate getParsedDate() {
        return LocalDate.parse(this.localDate);
    }

    public String getAccountName() {
        return accountName;
    }

    /**
     * Serializes transaction details to a comma-separated line for file storage.
     */
    public String toCSVLine() {
        return transactionId + " , " + description + " , " + category + " , " + type + " , " + amount + " , "
                + accountName + " , " + localDate;
    }
}
