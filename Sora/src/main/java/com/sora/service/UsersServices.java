package com.sora.service;

import java.util.List;
import com.sora.model.User;
import com.sora.util.FileHandler;
import java.util.ArrayList;
import java.io.*;

public class UsersServices {
    private static int counterUser = 1;
    private List<User> users;
    private FileHandler fileHandler;
    private String errorMessage = "";

    UsersServices() { // CONSTRUCTOR FOR THIS CLASS
        try {
            this.fileHandler = new FileHandler();
            this.users = fileHandler.loadUsers();
        } catch (IOException e) {
            System.out.println("Could not load Users"); // DEBUGGING PURPOSES
            this.users = new ArrayList<>();
        }
    }

    // METHOD TO VALIDATE THE USERS INPUT
    public boolean usersValidation(String firstName, String lastName, String email, String password) {

        // FIRST NAME SECTION VALIDATION
        if (firstName == null || firstName.length() < 2) {
            errorMessage = "First Name must be atleast more than 2 characters";
            return false;
        }

        if (!firstName.matches("^[a-zA-z0-9@_.-]+$")) {
            errorMessage = "First Name contains invalid special characters";
            return false;
        }

        if (firstName.matches(".*\\d.*")) {
            errorMessage = "First Name cannot contains numbers";
            return false;
        }

        // LAST NAME SECTION VALIDATION

        if (lastName == null || lastName.length() < 2) {
            errorMessage = "Last Name must be atleast more than 2 characters";
            return false;
        }

        if (!lastName.matches("^[a-zA-z0-9@_.-]+$")) {
            errorMessage = "Last Name contains invalid special characters";
            return false;

        }

        if (lastName.matches(".*\\d.*")) {
            errorMessage = "Last Name cannot contain numbers";
            return false;
        }

        // EMAIL SECTION VALIDATION

        if (!email.contains("@") || !email.contains(".")) {
            errorMessage = "Email must contain @ and .";
            return false;
        }

        if (password.length() < 2) {
            errorMessage = "password must be atleast more than 2 characters";
            return false;
        }

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                errorMessage = "A user with this email has already registered";
                return false;
            }
        }

        String userId = String.format("SRA-%04d", counterUser);
        counterUser++;

        User newUser = new User(userId, firstName, lastName, email, password);
        users.add(newUser);

        try {
            fileHandler.saveUsers(users);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save this file");
            return false;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}