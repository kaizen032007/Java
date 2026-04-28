package com.sora.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sora.model.Transaction;

public class Account {
    private List<Transaction> transaction;
    private String accountName;
    private String accountType;
    private double balance;
    private String accountNumber;

    Account(String accountName, String accountType, double balance, String accountNumber) {
        this.transaction = new ArrayList<>();
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
    };

    public void updateBalance(Transaction transaction) {
        if (transaction.getType().equals("Expense")) {
            this.balance -= transaction.getAmount();
        }

        if (transaction.getType().equals("Income")) {
            this.balance += transaction.getAmount();
        }

        this.transaction.add(transaction);
    }
}
