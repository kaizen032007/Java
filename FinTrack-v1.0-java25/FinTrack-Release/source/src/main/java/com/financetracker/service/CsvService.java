package com.financetracker.service;

import com.financetracker.model.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvService {

    public void exportTransactions(List<Transaction> transactions, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Type", "Amount", "Category", "Description", "Date", "Notes"))) {

            for (Transaction t : transactions) {
                printer.printRecord(
                        t.getId(),
                        t.getType().name(),
                        String.format("%.2f", t.getAmount()),
                        t.getCategory(),
                        t.getDescription(),
                        t.getDate().toString(),
                        t.getNotes()
                );
            }
        }
    }

    public List<Transaction> importTransactions(String filePath, String userId) throws IOException {
        List<Transaction> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord record : records) {
                try {
                    Transaction t = new Transaction();
                    t.setId(java.util.UUID.randomUUID().toString());
                    t.setUserId(userId);
                    t.setType(Transaction.Type.valueOf(record.get("Type").toUpperCase()));
                    t.setAmount(Double.parseDouble(record.get("Amount")));
                    t.setCategory(record.get("Category"));
                    t.setDescription(record.get("Description"));
                    t.setDate(LocalDate.parse(record.get("Date")));
                    t.setNotes(record.isMapped("Notes") ? record.get("Notes") : "");
                    list.add(t);
                } catch (Exception e) {
                    // Skip malformed rows
                }
            }
        }
        return list;
    }
}
