package com.sora.util;

import java.io.*;
import java.util.List;

import com.sora.model.User;

import java.util.ArrayList;

public class FileHandler {

    public void saveUsers(List<User> users) throws IOException {
        FileWriter fileWriter = new FileWriter("data/users.txt");
        for (User user : users) {
            fileWriter.write(user.getFirstName() + " | " + user.getLastName() + " | " + user.getEmail() + " | " +
                    user.getPassword() + "\n");
        }
        fileWriter.close();
    }

    public List<User> loadUsers() throws IOException {
        File filepath = new File("data/users.txt");
        if (filepath.exists()) {
            System.out.println("The file exists");
        } else {
            System.out.println("The file does not exists");
            return new ArrayList<>();
        }

        List<User> users = new ArrayList();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(",");

                User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);

                users.add(user);
            }
        }
        return users;
    }

}
