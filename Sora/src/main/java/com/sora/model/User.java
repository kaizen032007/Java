package com.sora.model;

/**
 * Model class representing a registered user profile.
 * Stores core details: identification token, name, email, and password.
 */
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * Constructs a new User.
     */
    public User(String userId, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Verifies if the provided input matches the registered account password.
     */
    public boolean validatePassword(String input) {
        return this.password.equals(input);
    }

    @Override
    public String toString() {
        return "User ID: " + userId +
                ", Name: " + firstName + " " + lastName +
                ", Email: " + email;
    }
}