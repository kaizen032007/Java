package com.sora.service;

import com.sora.model.Goal;
import com.sora.util.FileHandler;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class handling user savings goals, including creating new goals,
 * listing existing goals, and managing goal progress persistence.
 */
public class GoalsServices {
    private List<Goal> goals;
    private FileHandler fileHandler;
    private String userId;

    /**
     * Initializes the goals service for the specified user and loads their goals.
     */
    public GoalsServices(String userId) {
        this.userId = userId;
        this.fileHandler = new FileHandler();
        try {
            this.goals = fileHandler.loadGoals(userId);
        } catch (IOException e) {
            System.out.println("Could not load goals");
            this.goals = new ArrayList<>();
        }
    }

    /**
     * Creates/adds a new savings goal and saves the updated list.
     */
    public boolean addGoal(Goal goal) {
        boolean added = goals.add(goal);
        saveToFile();
        return added;
    }

    public List<Goal> getAllGoals() {
        return goals;
    }

    /**
     * Synchronizes and saves the current state of goals to local disk storage.
     */
    public void saveToFile() {
        try {
            fileHandler.saveGoals(userId, goals);
        } catch (IOException e) {
            System.out.println("Could not save goals");
        }
    }
}
