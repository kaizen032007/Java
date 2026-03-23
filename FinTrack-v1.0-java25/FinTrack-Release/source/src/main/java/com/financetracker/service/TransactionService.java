package com.financetracker.service;

import com.financetracker.model.Transaction;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {

    private final JsonDatabaseService db;

    public TransactionService(JsonDatabaseService db) {
        this.db = db;
    }

    public Transaction addTransaction(String userId, Transaction.Type type, double amount,
                                      String category, String description, LocalDate date, String notes) {
        Transaction t = new Transaction(
                UUID.randomUUID().toString(), userId, type, amount, category, description, date
        );
        t.setNotes(notes);
        db.saveTransaction(t);
        return t;
    }

    public void deleteTransaction(String transactionId) {
        db.deleteTransaction(transactionId);
    }

    public List<Transaction> getUserTransactions(String userId) {
        return db.loadTransactionsByUser(userId)
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByMonth(String userId, int year, int month) {
        return getUserTransactions(userId).stream()
                .filter(t -> t.getDate().getYear() == year && t.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    public double getTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance(List<Transaction> transactions) {
        return getTotalIncome(transactions) - getTotalExpenses(transactions);
    }

    public Map<String, Double> getExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public List<String> getIncomeCategories() {
        return Arrays.asList(
                "Salary", "Freelance", "Business", "Investment",
                "Gift", "Bonus", "Rental", "Other Income"
        );
    }

    public List<String> getExpenseCategories() {
        return Arrays.asList(
                "Food & Dining", "Transportation", "Housing", "Utilities",
                "Healthcare", "Entertainment", "Shopping", "Education",
                "Travel", "Personal Care", "Insurance", "Savings",
                "Subscriptions", "Other Expense"
        );
    }
}
