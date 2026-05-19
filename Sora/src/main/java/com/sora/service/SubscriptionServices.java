package com.sora.service;

import com.sora.model.Subscription;
import com.sora.util.FileHandler;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class handling user subscriptions.
 * Allows adding new plans, listing details, and saving/loading data from disk.
 */
public class SubscriptionServices {
    private List<Subscription> subscriptions;
    private FileHandler fileHandler;
    private String userId;

    /**
     * Initializes the subscription service for the specified user and loads subscriptions.
     */
    public SubscriptionServices(String userId) {
        this.userId = userId;
        this.fileHandler = new FileHandler();
        try {
            this.subscriptions = fileHandler.loadSubscriptions(userId);
        } catch (IOException e) {
            this.subscriptions = new ArrayList<>();
        }
    }

    /**
     * Adds a new active recurring subscription and saves changes.
     */
    public void addSubscription(Subscription s) {
        subscriptions.add(s);
        saveToFile();
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptions;
    }

    /**
     * Synchronizes and saves the current state of subscriptions to local disk storage.
     */
    public void saveToFile() {
        try {
            fileHandler.saveSubscriptions(userId, subscriptions);
        } catch (IOException e) {
            System.out.println("Could not save subscriptions");
        }
    }
}
