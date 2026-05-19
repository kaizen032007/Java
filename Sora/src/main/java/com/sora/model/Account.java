package com.sora.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a financial account (e.g. Bank Account,GCASH, Personal Wallet).
 * Stores account specifications, current balance, and a list of transactions associated with it.
 */
public class Account {
    // List of transactions processed on this account instance
    private List<Transaction> transactions;
    private String accountName;
    private String accountType;
    private double balance;
    private String accountNumber;

    /**
     * Constructs a new Account object with the specified details.
     */
    public Account(String accountName, String accountType, double balance, String accountNumber) {
        this.transactions = new ArrayList<>();
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Updates the account balance based on a transaction's type (Expense vs Income).
     * Adds the transaction to the account's internal transaction log history.
     */
    public void updateBalance(Transaction transaction) {
        if (transaction.getType().equalsIgnoreCase("Expense")) {
            this.balance -= transaction.getAmount();
        }

        if (transaction.getType().equalsIgnoreCase("Income")) {
            this.balance += transaction.getAmount();
        }

        this.transactions.add(transaction);
    }
}
