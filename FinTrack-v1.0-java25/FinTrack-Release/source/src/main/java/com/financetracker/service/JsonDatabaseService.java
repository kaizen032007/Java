package com.financetracker.service;

import com.financetracker.model.Transaction;
import com.financetracker.model.User;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class JsonDatabaseService {

    private static final String DATA_DIR;
    private static final String USERS_FILE;
    private static final String TRANSACTIONS_FILE;

    static {
        String appDir = System.getProperty("user.home") + File.separator + "FinanceTrackerData";
        DATA_DIR = appDir;
        USERS_FILE    = appDir + File.separator + "users.json";
        TRANSACTIONS_FILE = appDir + File.separator + "transactions.json";
    }

    public JsonDatabaseService() { initializeFiles(); }

    private void initializeFiles() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            if (!Files.exists(Paths.get(USERS_FILE)))        Files.write(Paths.get(USERS_FILE), "[]".getBytes());
            if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) Files.write(Paths.get(TRANSACTIONS_FILE), "[]".getBytes());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<User> loadAllUsers() {
        try {
            String raw = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
            List<Map<String,String>> rows = parseJsonArray(raw);
            List<User> users = new ArrayList<>();
            for (Map<String,String> m : rows) users.add(mapToUser(m));
            return users;
        } catch (Exception e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    public void saveUser(User u) {
        List<User> users = loadAllUsers();
        users.removeIf(x -> x.getId().equals(u.getId()));
        users.add(u);
        saveAllUsers(users);
    }

    private void saveAllUsers(List<User> users) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < users.size(); i++) {
            sb.append(userToJson(users.get(i)));
            if (i < users.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try { Files.write(Paths.get(USERS_FILE), sb.toString().getBytes()); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public Optional<User> findUserByUsername(String username) {
        return loadAllUsers().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        return loadAllUsers().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public boolean usernameExists(String username) { return findUserByUsername(username).isPresent(); }
    public boolean emailExists(String email)       { return findUserByEmail(email).isPresent(); }

    public List<Transaction> loadAllTransactions() {
        try {
            String raw = new String(Files.readAllBytes(Paths.get(TRANSACTIONS_FILE)));
            List<Map<String,String>> rows = parseJsonArray(raw);
            List<Transaction> list = new ArrayList<>();
            for (Map<String,String> m : rows) list.add(mapToTransaction(m));
            return list;
        } catch (Exception e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    public List<Transaction> loadTransactionsByUser(String userId) {
        return loadAllTransactions().stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public void saveTransaction(Transaction t) {
        List<Transaction> all = loadAllTransactions();
        all.removeIf(x -> x.getId().equals(t.getId()));
        all.add(t);
        saveAllTransactions(all);
    }

    public void deleteTransaction(String id) {
        List<Transaction> all = loadAllTransactions();
        all.removeIf(t -> t.getId().equals(id));
        saveAllTransactions(all);
    }

    private void saveAllTransactions(List<Transaction> list) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(transactionToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try { Files.write(Paths.get(TRANSACTIONS_FILE), sb.toString().getBytes()); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public String getDataDirectory() { return DATA_DIR; }

    private String userToJson(User u) {
        return "  {\"id\":" + jstr(u.getId()) +
               ",\"username\":" + jstr(u.getUsername()) +
               ",\"email\":" + jstr(u.getEmail()) +
               ",\"passwordHash\":" + jstr(u.getPasswordHash()) +
               ",\"fullName\":" + jstr(u.getFullName()) +
               ",\"createdAt\":" + jstr(u.getCreatedAt() != null ? u.getCreatedAt().toString() : LocalDateTime.now().toString()) +
               ",\"monthlyBudget\":" + u.getMonthlyBudget() +
               ",\"currency\":" + jstr(u.getCurrency() != null ? u.getCurrency() : "USD") + "}";
    }

    private User mapToUser(Map<String,String> m) {
        User u = new User();
        u.setId(m.get("id"));
        u.setUsername(m.get("username"));
        u.setEmail(m.get("email"));
        u.setPasswordHash(m.get("passwordHash"));
        u.setFullName(m.get("fullName"));
        u.setCreatedAt(LocalDateTime.parse(m.getOrDefault("createdAt", LocalDateTime.now().toString())));
        u.setMonthlyBudget(Double.parseDouble(m.getOrDefault("monthlyBudget", "0.0")));
        u.setCurrency(m.getOrDefault("currency", "USD"));
        return u;
    }

    private String transactionToJson(Transaction t) {
        return "  {\"id\":" + jstr(t.getId()) +
               ",\"userId\":" + jstr(t.getUserId()) +
               ",\"type\":" + jstr(t.getType().name()) +
               ",\"amount\":" + t.getAmount() +
               ",\"category\":" + jstr(t.getCategory()) +
               ",\"description\":" + jstr(t.getDescription()) +
               ",\"date\":" + jstr(t.getDate().toString()) +
               ",\"notes\":" + jstr(t.getNotes() != null ? t.getNotes() : "") + "}";
    }

    private Transaction mapToTransaction(Map<String,String> m) {
        Transaction t = new Transaction();
        t.setId(m.get("id"));
        t.setUserId(m.get("userId"));
        t.setType(Transaction.Type.valueOf(m.get("type")));
        t.setAmount(Double.parseDouble(m.get("amount")));
        t.setCategory(m.get("category"));
        t.setDescription(m.get("description"));
        t.setDate(LocalDate.parse(m.get("date")));
        t.setNotes(m.getOrDefault("notes", ""));
        return t;
    }

    private static String jstr(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                       .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
    }

    static List<Map<String,String>> parseJsonArray(String json) {
        List<Map<String,String>> result = new ArrayList<>();
        if (json == null) return result;
        json = json.trim();
        if (json.isEmpty() || json.equals("[]")) return result;
        int start = json.indexOf('[');
        int end   = json.lastIndexOf(']');
        if (start < 0 || end < 0) return result;
        json = json.substring(start + 1, end).trim();
        List<String> objects = splitObjects(json);
        for (String obj : objects) {
            Map<String,String> map = parseObject(obj.trim());
            if (!map.isEmpty()) result.add(map);
        }
        return result;
    }

    private static List<String> splitObjects(String s) {
        List<String> parts = new ArrayList<>();
        int depth = 0, start = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') { depth++; if (depth == 1) start = i; }
            else if (c == '}') { depth--; if (depth == 0 && start >= 0) { parts.add(s.substring(start, i + 1)); start = -1; } }
        }
        return parts;
    }

    private static Map<String,String> parseObject(String obj) {
        Map<String,String> map = new LinkedHashMap<>();
        obj = obj.trim();
        if (obj.startsWith("{")) obj = obj.substring(1);
        if (obj.endsWith("}"))   obj = obj.substring(0, obj.length() - 1);
        obj = obj.trim();
        int i = 0;
        while (i < obj.length()) {
            while (i < obj.length() && (obj.charAt(i) == ',' || Character.isWhitespace(obj.charAt(i)))) i++;
            if (i >= obj.length()) break;
            if (obj.charAt(i) != '"') { i++; continue; }
            int keyEnd = findClosingQuote(obj, i + 1);
            String key = unescape(obj.substring(i + 1, keyEnd));
            i = keyEnd + 1;
            while (i < obj.length() && (obj.charAt(i) == ':' || Character.isWhitespace(obj.charAt(i)))) i++;
            if (i >= obj.length()) break;
            String value;
            if (obj.charAt(i) == '"') {
                int valEnd = findClosingQuote(obj, i + 1);
                value = unescape(obj.substring(i + 1, valEnd));
                i = valEnd + 1;
            } else {
                int valEnd = i;
                while (valEnd < obj.length() && obj.charAt(valEnd) != ',' && obj.charAt(valEnd) != '}') valEnd++;
                value = obj.substring(i, valEnd).trim();
                i = valEnd;
            }
            map.put(key, value);
        }
        return map;
    }

    private static int findClosingQuote(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '\\') { i++; continue; }
            if (s.charAt(i) == '"')  return i;
        }
        return s.length();
    }

    private static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);
                switch (next) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    default -> { sb.append('\\'); sb.append(next); }
                }
            } else { sb.append(s.charAt(i)); }
        }
        return sb.toString();
    }
}
