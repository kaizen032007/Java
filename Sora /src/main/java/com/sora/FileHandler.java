package com.sora;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class FileHandler {

    public void saveUsers(List<User> users) throws IOException {
        FileWriter fileWriter = new FileWriter("data/users.txt");
        for (User user : users) {
            fileWriter.write(user.getUserId() + "," + user.getFirstName() + "," + user.getLastName()
                    + "," + user.getEmail() + "\n");
        }
        fileWriter.close();
    }

    public List<User> loadUsers() throws IOException {
        File filepath = new File("data/users.txt");
        if (filepath.exists()) {
            System.out.println("File exist"); // debugging purposes
        } else {
            System.out.println("File Does not exist"); // debugging process
            return null;
        }

        List<User> users = new ArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] parts = line.split(",");

                User user = new User(parts[0], parts[1], parts[2], parts[3], "password");

                users.add(user);
            }
        }
        return users;
    }
}
