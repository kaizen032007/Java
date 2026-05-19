package com.sora.model;

/**
 * Model class representing a monthly spending budget limit for a specific category
 * (e.g. Food, Transport, Utilities).
 */
public class Budget {
    private String budgetName;
    private double monthlyLimit;

    /**
     * Constructs a new Budget with category name and monthly limit limit.
     */
    public Budget(String budgetName, double monthlyLimit) {
        this.budgetName = budgetName;
        this.monthlyLimit = monthlyLimit;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public double getMonthlyLimit() {
        return monthlyLimit;
    }

    /**
     * Serializes budget details to a comma-separated line for file storage.
     */
    public String toCSVLine() {
        return budgetName + " , " + monthlyLimit;
    }
}
