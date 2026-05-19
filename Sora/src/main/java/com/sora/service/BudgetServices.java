package com.sora.service;

import com.sora.model.Budget;
import com.sora.util.FileHandler;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class handling monthly budget limits operations, such as adding budgets,
 * retrieving budget objects for specific categories, deleting budgets, and saving data.
 */
public class BudgetServices {
    private List<Budget> budgets;
    private FileHandler fileHandler;
    private String userId;

    /**
     * Initializes the budget service for the specified user and loads their budgets.
     */
    public BudgetServices(String userId) {
        this.userId = userId;
        this.fileHandler = new FileHandler();
        try {
            this.budgets = fileHandler.loadBudgets(userId);
        } catch (IOException e) {
            this.budgets = new ArrayList<>();
        }
    }

    /**
     * Adds a new category budget limit. If a limit for that category already exists,
     * it overwrites it.
     */
    public void addBudget(Budget budget) {
        budgets.removeIf(b -> b.getBudgetName().equalsIgnoreCase(budget.getBudgetName()));
        budgets.add(budget);
        saveToFile();
    }

    public List<Budget> getAllBudgets() {
        return budgets;
    }

    /**
     * Looks up and returns the Budget limit configured for a specific category name.
     */
    public Budget getBudgetForCategory(String category) {
        for (Budget b : budgets) {
            if (b.getBudgetName().equalsIgnoreCase(category)) return b;
        }
        return null;
    }

    /**
     * Deletes a category budget limit and saves the change.
     */
    public void deleteBudget(Budget budget) {
        budgets.remove(budget);
        saveToFile();
    }

    /**
     * Synchronizes and saves the current state of budgets to local disk storage.
     */
    private void saveToFile() {
        try {
            fileHandler.saveBudgets(userId, budgets);
        } catch (IOException e) {
            System.out.println("Could not save budgets");
        }
    }
}
