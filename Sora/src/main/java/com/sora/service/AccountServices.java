package com.sora.service;

import com.sora.model.Account;
import com.sora.util.FileHandler;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class handling account business logic, including adding new accounts,
 * calculating total cumulative balances across all accounts, and persisting accounts to disk.
 */
public class AccountServices {
    private List<Account> accounts;
    private FileHandler fileHandler;
    private String userId;

    /**
     * Initializes the account service for the specified user and loads their accounts.
     */
    public AccountServices(String userId) {
        this.userId = userId;
        this.fileHandler = new FileHandler();
        try {
            this.accounts = fileHandler.loadAccounts(userId);
        } catch (IOException e) {
            System.out.println("Could not load accounts");
            this.accounts = new ArrayList<>();
        }
    }

    /**
     * Adds a new financial account and saves the updated list to the file system.
     */
    public boolean addAccount(Account account) {
        boolean added = accounts.add(account);
        saveToFile();
        return added;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }

    /**
     * Calculates the sum total balance across all active accounts of this user.
     */
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    /**
     * Synchronizes and saves the current state of accounts to local disk storage.
     */
    public void saveToFile() {
        try {
            fileHandler.saveAccounts(userId, accounts);
        } catch (IOException e) {
            System.out.println("Could not save accounts");
        }
    }
}