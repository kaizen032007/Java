package com.sora;

public class Transaction {
    private String transactionId;
    private String userId;
    private String type;
    private double amount;
    private String category;
    private String description;
    private String date;

    Transaction(String transactionId, String userId, String type, double amount, String category, String description, String date) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDate() { return date; }


}