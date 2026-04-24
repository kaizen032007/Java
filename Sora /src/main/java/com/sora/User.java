package com.sora;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    User(String userId, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    
    public boolean validatePassword(String input) {
        return this.password.equals(input);
    }
}