package com.sora.service;

import java.util.List;
import com.sora.model.User;
import java.util.ArrayList;
import java.io.*;

public class UsersServices {
    private static int counterUser = 1;
    private List<User> users;
    public boolean validInput = false;
    private String errorMessage = "";

    public User usersValidation(String firstName, String lastName, String email, String password) {
        while (!validInput) {
            try {
                // FIRST NAME SECTION VALIDATION
                if (firstName == null || firstName.length() < 2) {
                    errorMessage = "First Name must be atleast more than 2 characters";
                    break;
                }

                if (!firstName.matches("^[a-zA-z0-9@_.-]+$")) {
                    errorMessage = "First Name contains invalid special characters";
                    break;
                }

                if (firstName.matches(".*\\d.*")) {
                    errorMessage = "First Name cannot contains numbers";
                    break;
                }

                // LAST NAME SECTION VALIDATION

                if (lastName == null || lastName.length() < 2) {
                    errorMessage = "Last Name must be atleast more than 2 characters";
                    break;
                }

                if (!lastName.matches("^[a-zA-z0-9@_.-]+$")) {
                    errorMessage = "Last Name contains invalid special characters";
                    break;
                }

                if (lastName.matches(".*\\d.*")) {
                    errorMessage = "Last Name cannot contain numbers";
                    break;
                }

                // EMAIL SECTION VALIDATION

                if (!email.contains("@") || !email.contains(".")) {
                    errorMessage = "Email must contain @ and .";
                    break;
                }

                if (password.length() < 2) {
                    errorMessage = "password must be atleast more than 2 characters";
                    break;
                }

                validInput = true;

                String userId = String.format("SRA-%04d", counterUser);
                counterUser++;

                return new User(userId, firstName, lastName, email, password);

            } catch (Exception e) {
                System.out.println("Error: Invalid Users Input " + e.getMessage()); // DEBUGGING PURPOSES
                break;
            }
        }
        return null;
    }
}
