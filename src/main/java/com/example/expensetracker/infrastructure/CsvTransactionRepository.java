package com.example.expensetracker.infrastructure;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.example.expensetracker.application.TransactionRepository;
import com.example.expensetracker.domain.Transaction;
import com.example.expensetracker.domain.TransactionType;

public final class CsvTransactionRepository implements TransactionRepository {
    private static final String[] HEADERS = {"id", "type", "amount", "category", "date", "note", "createdAt"};
    private static final CSVFormat READ_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader(HEADERS)
            .setSkipHeaderRecord(true)
            .get();
    private static final CSVFormat WRITE_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader(HEADERS)
            .get();

    private final Path dataFile;

    public CsvTransactionRepository(Path dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public List<Transaction> findAll() throws IOException {
        if (!Files.exists(dataFile)) {
            return List.of();
        }

        List<Transaction> transactions = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8);
                CSVParser parser = READ_FORMAT.parse(reader)) {
            for (CSVRecord record : parser) {
                transactions.add(new Transaction(
                        record.get("id"),
                        TransactionType.parse(record.get("type")),
                        new BigDecimal(record.get("amount")),
                        record.get("category"),
                        LocalDate.parse(record.get("date")),
                        record.get("note"),
                        Instant.parse(record.get("createdAt"))));
            }
        }
        return transactions;
    }

    @Override
    public void saveAll(List<Transaction> transactions) throws IOException {
        Path parent = dataFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (Writer writer = Files.newBufferedWriter(dataFile, StandardCharsets.UTF_8);
                CSVPrinter printer = WRITE_FORMAT.print(writer)) {
            for (Transaction transaction : transactions) {
                printer.printRecord(
                        transaction.id(),
                        transaction.type().value(),
                        transaction.amountText(),
                        transaction.category(),
                        transaction.date(),
                        transaction.note(),
                        transaction.createdAt());
            }
        }
    }
}
