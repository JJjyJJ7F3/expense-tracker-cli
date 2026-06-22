package com.example.expensetracker.application;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.example.expensetracker.domain.Transaction;

public final class TransactionService {
    private final TransactionRepository repository;
    private final Clock clock;

    public TransactionService(TransactionRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Transaction add(AddTransactionCommand command) throws IOException {
        Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                command.type(),
                command.amount(),
                command.category(),
                command.date(),
                command.note(),
                clock.instant());
        List<Transaction> transactions = new ArrayList<>(repository.findAll());
        transactions.add(transaction);
        repository.saveAll(transactions);
        return transaction;
    }

    public List<Transaction> listRecent() throws IOException {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::createdAt).reversed())
                .toList();
    }
}
