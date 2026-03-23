package com.financetracker.service;

import com.financetracker.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final JsonDatabaseService db;
    private User currentUser;

    public AuthService(JsonDatabaseService db) {
        this.db = db;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public enum RegisterResult {
        SUCCESS, USERNAME_TAKEN, EMAIL_TAKEN, INVALID_INPUT
    }

    public RegisterResult register(String username, String email, String password, String fullName) {
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            return RegisterResult.INVALID_INPUT;
        }
        if (db.usernameExists(username.trim())) return RegisterResult.USERNAME_TAKEN;
        if (db.emailExists(email.trim())) return RegisterResult.EMAIL_TAKEN;

        User user = new User(
                UUID.randomUUID().toString(),
                username.trim().toLowerCase(),
                email.trim().toLowerCase(),
                hashPassword(password),
                fullName.trim()
        );
        db.saveUser(user);
        return RegisterResult.SUCCESS;
    }

    public enum LoginResult {
        SUCCESS, INVALID_CREDENTIALS
    }

    public LoginResult login(String usernameOrEmail, String password) {
        Optional<User> opt = db.findUserByUsername(usernameOrEmail.trim().toLowerCase());
        if (opt.isEmpty()) opt = db.findUserByEmail(usernameOrEmail.trim().toLowerCase());

        if (opt.isEmpty()) return LoginResult.INVALID_CREDENTIALS;

        User user = opt.get();
        if (!user.getPasswordHash().equals(hashPassword(password))) {
            return LoginResult.INVALID_CREDENTIALS;
        }

        this.currentUser = user;
        return LoginResult.SUCCESS;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }

    public void updateUserBudget(double budget) {
        if (currentUser != null) {
            currentUser.setMonthlyBudget(budget);
            db.saveUser(currentUser);
        }
    }
}
