package com.sora.model;

/**
 * Model class representing recurring subscription plans (e.g. Netflix, Spotify).
 * Tracks periodic cost, monthly bill cycles, and current subscription plan status (Active/Inactive).
 */
public class Subscription {
    private String subscriptionId;
    private String subscriptionName;
    private double monthlyCost;
    private String billingDate;
    private String status;

    /**
     * Constructs a new recurring Subscription.
     */
    public Subscription(String subscriptionId, String subscriptionName, double monthlyCost,
            String billingDate, String status) {
        this.subscriptionId = subscriptionId;
        this.subscriptionName = subscriptionName;
        this.monthlyCost = monthlyCost;
        this.billingDate = billingDate;
        this.status = status;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Serializes subscription details to a comma-separated line for file storage.
     */
    public String toCSVLine() {
        return subscriptionId + " , " + subscriptionName + " , " + monthlyCost
                + " , " + billingDate + " , " + status;
    }
}