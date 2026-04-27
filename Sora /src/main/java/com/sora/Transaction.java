package com.sora.model;

public class Transaction {
    private String transactionId;
    private String description;
    private String category;
    private double amount;
    private String date;
    private String type;

    Transaction(String transactionId, String description, String category, double amount, String date, String type) {
        this.transactionId = transactionId;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

}
