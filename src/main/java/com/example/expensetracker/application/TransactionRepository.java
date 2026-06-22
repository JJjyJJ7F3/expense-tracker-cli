package com.example.expensetracker.application;

import java.io.IOException;
import java.util.List;

import com.example.expensetracker.domain.Transaction;

public interface TransactionRepository {
    List<Transaction> findAll() throws IOException;

    void saveAll(List<Transaction> transactions) throws IOException;
}
