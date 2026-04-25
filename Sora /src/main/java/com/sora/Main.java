package com.sora;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1. Create services
        UserServices userService = new UserServices();
        TransactionService transactionService = new TransactionService();

        // 2. Register a user
        boolean registered =
                userService.registerUsers("U001", "Juan", "Dela Cruz", "juan@email.com", "pass123");
        System.out.println("User registered: " + registered);

        // 3. Try duplicate
        boolean registered2 =
                userService.registerUsers("U001", "Juan", "Dela Cruz", "juan@email.com", "pass123");
        System.out.println("User registered: " + registered2);

        // 4-5. Login tests
        userService.loginUsers("U001", "pass123");
        userService.loginUsers("U001", "pass124");

        // 6-8. Add transactions
        boolean added1 = transactionService.addTransaction("U001", // userId
                "EXPENSE", // type
                150.00, // amount
                "Food", // category
                "Jollibee", // description
                "2024-01-15" // date
        );
        System.out.println("Transaction added: " + added1);

        boolean added2 = transactionService.addTransaction("U001", "INCOME", // type is INCOME
                5000.00, "Salary", "Monthly pay", "2024-01-15");
        System.out.println("Transaction added: " + added2);

        // 9. Display
        List<Transaction> userTransactions = transactionService.getTransactionsByUser("U001");

        System.out.println("\nTransactions for U001:");
        for (Transaction t : userTransactions) {
            System.out.println(t.getType() + " | " + t.getAmount() + " | " + t.getCategory() + " | "
                    + t.getDescription());
        }
    }
}
