package com.sora; 

import com.google.gson.Gson;           
import com.google.gson.GsonBuilder;    
import com.google.gson.reflect.TypeToken; 
import java.io.*;                     
import java.lang.reflect.Type;         
import java.util.ArrayList;            
import java.util.List;                 

public class JsonHandler {
    private static final String DATA_FOLDER = "data/";
    private Gson gson;

    public JsonHandler() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveUser(List<User> users) throws IOException {
        String filePath = DATA_FOLDER + "users.json";
        FileWriter fileWriter = new FileWriter(filePath);
        gson.toJson(users, fileWriter);
        fileWriter.close();
    }

    public List<User> loadUsers() throws IOException {
        String filePath = DATA_FOLDER + "users.json";
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        FileReader fileReader = new FileReader(filePath);
        Type userListType = new TypeToken<List<User>>(){}.getType();
        List<User> users = gson.fromJson(fileReader, userListType);
        fileReader.close();
        return users;
    }

    public void saveTransactions(List<Transaction> transactions) throws IOException {
        String filePath = DATA_FOLDER + "Transactions.json";
        FileWriter fileWriter = new FileWriter(filePath);
        gson.toJson(transactions, fileWriter);
        fileWriter.close();
    }

    public List<Transaction> loadTransactions() throws IOException {
        String filePath = DATA_FOLDER + "Transactions.json";
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        FileReader fileReader = new FileReader(filePath);
        Type transactionListType = new TypeToken<List<Transaction>>(){}.getType();
        List<Transaction> transactions = gson.fromJson(fileReader, transactionListType);
        fileReader.close();
        return transactions;
    }
}