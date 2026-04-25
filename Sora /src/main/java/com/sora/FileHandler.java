package com.sora;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class FileHandler {

    public void saveUsers(List<User> users) throws IOException {
        FileWriter fileWriter = new FileWriter("data/users.txt");
        for (User user : users) {
            fileWriter.write(user.getUserId() + "," + user.getFirstName() + "," + user.getLastName()
                    + "," + user.getEmail() + "," + user.getPassword() + "\n");
        }
        fileWriter.close();
    }

    public List<User> loadUsers() throws IOException {
        File filepath = new File("data/users.txt");
        if (filepath.exists()) {
            System.out.println("File exist"); // debugging purposes
        } else {
            System.out.println("File Does not exist"); // debugging process
            return new ArrayList<>();
        }

        List<User> users = new ArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(",");

                User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);

                users.add(user);
            }
        }
        return users;
    }

    public void saveTransactions(List<Transaction> transactions) throws IOException {
        FileWriter fileWriter = new FileWriter("data/transactions.txt");
        for (Transaction transaction : transactions) {
            fileWriter.write(transaction.getTransactionId() + "," + transaction.getUserId() + ","
                    + transaction.getType() + "," + transaction.getAmount() + ","
                    + transaction.getCategory() + "," + transaction.getDescription() + ","
                    + transaction.getDate() + "\n");
        }
        fileWriter.close();
    }

    public List<Transaction> loadTransactions() throws IOException {
        File filePath = new File("data/transactions.txt");
        if (filePath.exists()) {
            System.out.println("File exists");
        } else {
            System.out.println("File does not exists");
            return new ArrayList<>();
        }

        List<Transaction> transactions = new ArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(",");
                String type = parts[2];
                Transaction transaction;
                if (type.equals("INCOME")) {
                    transaction = new IncomeTransaction(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]), parts[4], parts[5], parts[6]);
                } else {
                    transaction = new ExpenseTransaction(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]), parts[4], parts[5], parts[6]);
                }

                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
