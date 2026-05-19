package com.sora.model;

/**
 * Model class representing a user's savings goal (e.g. Travel, Laptop purchase).
 * Tracks target amounts, currently saved amounts, progress, and visual icon identifiers.
 */
public class Goal {
    private String goalId;
    private String goalName;
    private String iconPath;
    private double targetAmount;
    private double savedAmount;

    /**
     * Constructs a new savings Goal.
     */
    public Goal(String goalId, String goalName, String iconPath, double targetAmount, double savedAmount) {
        this.goalId = goalId;
        this.goalName = goalName;
        this.iconPath = iconPath;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
    }

    public String getGoalId() {
        return goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getSavedAmount() {
        return savedAmount;
    }

    /**
     * Calculates the savings completion progress ratio (between 0.0 and 1.0).
     */
    public double getProgress() {
        if (targetAmount == 0)
            return 0;
        return savedAmount / targetAmount;
    }

    /**
     * Increases the accumulated savings allocated to this goal.
     */
    public void addSavings(double amount) {
        this.savedAmount += amount;
    }

    /**
     * Serializes goal details to a comma-separated line for file storage.
     */
    public String toCSVLine() {
        return goalId + " , " + goalName + " , " + iconPath + " , " + targetAmount + " , " + savedAmount;
    }
}