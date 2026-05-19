package com.sora.util;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import com.sora.model.User;
import com.sora.model.Account;
import com.sora.model.Transaction;
import com.sora.model.Goal;
import com.sora.model.Subscription;

/**
 * FileHandler handles all raw read and write operations to the local file system.
 * It acts as the persistence layer, saving and loading data in a plain text CSV format
 * located under the 'data/' directory.
 */
public class FileHandler {

    /**
     * Overwrites data/users.txt with the current list of registered users.
     * Each line represents one user record, with fields joined by " , ".
     */
    public void saveUsers(List<User> users) throws IOException {
        FileWriter fileWriter = new FileWriter("data/users.txt");
        for (User user : users) {
            fileWriter.write(user.getUserId() + " , " + user.getFirstName() + " , " + user.getLastName() + " , "
                    + user.getEmail() + " , " +
                    user.getPassword() + "\n");
        }
        fileWriter.close();
    }

    /**
     * Reads all registered users from data/users.txt.
     * Parses the comma-separated values back into User objects.
     */
    public List<User> loadUsers() throws IOException {
        File filepath = new File("data/users.txt");
        if (!filepath.exists()) {
            return new ArrayList<>();
        }

        List<User> users = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" , ");
                User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Overwrites data/accounts_{userId}.txt with the user's financial accounts.
     * Each line contains account details (name, type, balance, account number).
     */
    public void saveAccounts(String userId, List<Account> accounts) throws IOException {
        new File("data").mkdirs();
        FileWriter fileWriter = new FileWriter("data/accounts_" + userId + ".txt");
        for (Account account : accounts) {
            fileWriter.write(account.getAccountName() + " , " + account.getAccountType() + " , "
                    + account.getBalance() + " , " + account.getAccountNumber() + "\n");
        }
        fileWriter.close();
    }

    /**
     * Loads all accounts belonging to the specified userId.
     */
    public List<Account> loadAccounts(String userId) throws IOException {
        File filepath = new File("data/accounts_" + userId + ".txt");
        if (!filepath.exists()) {
            return new ArrayList<>();
        }

        List<Account> accounts = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data/accounts_" + userId + ".txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(" , ", -1);
                Account account = new Account(parts[0], parts[1], Double.parseDouble(parts[2]), parts.length > 3 ? parts[3] : "");
                accounts.add(account);
            }
        }
        return accounts;
    }

    /**
     * Overwrites data/transactions_{userId}.txt with the user's transaction history logs.
     */
    public void saveTransactions(String userId, List<Transaction> transactions) throws IOException {
        new File("data").mkdirs();
        FileWriter fileWriter = new FileWriter("data/transactions_" + userId + ".txt");
        for (Transaction transaction : transactions) {
            fileWriter.write(transaction.toCSVLine() + "\n");
        }
        fileWriter.close();
    }

    /**
     * Loads transaction logs belonging to the specified userId.
     */
    public List<Transaction> loadTransactions(String userId) throws IOException {
        File filepath = new File("data/transactions_" + userId + ".txt");
        if (!filepath.exists()) {
            return new ArrayList<>();
        }

        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader("data/transactions_" + userId + ".txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(" , ", -1);
                if (parts.length < 7) continue; // Skip incomplete lines
                Transaction transaction = new Transaction(parts[0], parts[1], parts[2],
                        Double.parseDouble(parts[4]), parts[6], parts[3], parts[5]);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    /**
     * Overwrites data/goals_{userId}.txt with the user's current savings goals.
     */
    public void saveGoals(String userId, List<Goal> goals) throws IOException {
        new File("data").mkdirs();
        FileWriter fileWriter = new FileWriter("data/goals_" + userId + ".txt");
        for (Goal goal : goals) {
            fileWriter.write(goal.toCSVLine() + "\n");
        }
        fileWriter.close();
    }

    /**
     * Loads savings goals belonging to the specified userId.
     */
    public List<Goal> loadGoals(String userId) throws IOException {
        File filepath = new File("data/goals_" + userId + ".txt");
        if (!filepath.exists()) {
            return new ArrayList<>();
        }

        List<Goal> goals = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data/goals_" + userId + ".txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(" , ", -1);
                if (parts.length < 5) continue;
                Goal goal = new Goal(parts[0], parts[1], parts[2],
                        Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
                goals.add(goal);
            }
        }
        return goals;
    }

    /**
     * Overwrites data/subscriptions_{userId}.txt with the user's active recurring subscriptions.
     */
    public void saveSubscriptions(String userId, List<Subscription> subscriptions) throws IOException {
        new File("data").mkdirs();
        FileWriter fileWriter = new FileWriter("data/subscriptions_" + userId + ".txt");
        for (Subscription subscription : subscriptions) {
            fileWriter.write(subscription.toCSVLine() + "\n");
        }
        fileWriter.close();
    }

    /**
     * Loads subscriptions belonging to the specified userId.
     */
    public List<Subscription> loadSubscriptions(String userId) throws IOException {
        File filepath = new File("data/subscriptions_" + userId + ".txt");
        if (!filepath.exists()) {
            return new ArrayList<>();
        }

        List<Subscription> subscriptions = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader("data/subscriptions_" + userId + ".txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(" , ", -1);
                if (parts.length < 5) continue;
                Subscription subscription = new Subscription(parts[0], parts[1],
                        Double.parseDouble(parts[2]), parts[3], parts[4]);
                subscriptions.add(subscription);
            }
        }
        return subscriptions;
    }

    /**
     * Overwrites data/budgets_{userId}.txt with the user's dynamic budget limits.
     */
    public void saveBudgets(String userId, List<com.sora.model.Budget> budgets) throws IOException {
        new File("data").mkdirs();
        FileWriter fileWriter = new FileWriter("data/budgets_" + userId + ".txt");
        for (com.sora.model.Budget budget : budgets) {
            fileWriter.write(budget.toCSVLine() + "\n");
        }
        fileWriter.close();
    }

    /**
     * Loads budget limits belonging to the specified userId.
     */
    public List<com.sora.model.Budget> loadBudgets(String userId) throws IOException {
        File filepath = new File("data/budgets_" + userId + ".txt");
        if (!filepath.exists()) return new ArrayList<>();

        List<com.sora.model.Budget> budgets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" , ");
                budgets.add(new com.sora.model.Budget(parts[0], Double.parseDouble(parts[1])));
            }
        }
        return budgets;
    }

    /**
     * Persists the user's email and password payload to auto-login them on subsequent app starts.
     */
    public void saveRememberedUser(String info) throws IOException {
        new File("data").mkdirs();
        FileWriter fileWriter = new FileWriter("data/remember_me.txt");
        fileWriter.write(info);
        fileWriter.close();
    }

    /**
     * Reads the stored credentials payload for auto-login.
     */
    public String loadRememberedUser() {
        File filepath = new File("data/remember_me.txt");
        if (!filepath.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Removes the stored auto-login file on logout.
     */
    public void clearRememberedUser() {
        File filepath = new File("data/remember_me.txt");
        if (filepath.exists()) {
            filepath.delete();
        }
    }
}