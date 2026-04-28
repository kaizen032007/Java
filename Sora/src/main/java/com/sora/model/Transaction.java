package com.sora.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Transaction {
    private String transactionId;
    private String description;
    private String category;
    private double amount;
    private String localDate;
    private String type;
    private String accountName;

    Transaction(String transactionId, String description, String category, double amount, String localDate,
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

    public LocalDate getParsedDate() {
        return LocalDate.parse(this.localDate);
    }

    public String getAccountName() {
        return accountName;
    }

    public String toCSVLine() {
        return transactionId + " , " + description + " , " + category + " , " + type + " , " + amount + " , "
                + accountName + " , " + localDate;
    }
}
