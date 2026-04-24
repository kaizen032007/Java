package com.sora;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class UserServices {
    private FileHandler filehandler;
    private List<User> users;

    UserServices() {
        try {
            this.filehandler = new FileHandler();
            this.users = filehandler.loadUsers();
        } catch (IOException e) {
            System.out.println("Could not load users. ");
            this.users = new ArrayList<>();
        }
    }

    public boolean registerUsers(String userId, String firstName, String lastName, String email,
            String password) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                System.out.println("User exist already");
                return false;
            }
        }

        User newUser = new User(userId, firstName, lastName, email, password);
        users.add(newUser);

        try {
            filehandler.saveUsers(users);
            return true;
        } catch (IOException e) {
            System.out.println("There is something wrong with the file");
            return false;
        }
    }

    public boolean loginUsers(String userId, String password) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                if (user.validatePassword(password)) {
                    return true;
                }
            }
        }
        return false;
    }
}
