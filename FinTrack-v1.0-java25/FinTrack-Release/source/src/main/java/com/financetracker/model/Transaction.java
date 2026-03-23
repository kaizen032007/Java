package com.financetracker.model;

import java.time.LocalDate;

public class Transaction {
    public enum Type { INCOME, EXPENSE }

    private String id;
    private String userId;
    private Type type;
    private double amount;
    private String category;
    private String description;
    private LocalDate date;
    private String notes;

    public Transaction() {}

    public Transaction(String id, String userId, Type type, double amount,
                       String category, String description, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.notes = "";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isExpense() { return type == Type.EXPENSE; }
    public boolean isIncome() { return type == Type.INCOME; }
}
