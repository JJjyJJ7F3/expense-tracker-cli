package com.example.expensetracker.application;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.expensetracker.domain.TransactionType;

public record AddTransactionCommand(
        TransactionType type,
        BigDecimal amount,
        String category,
        LocalDate date,
        String note) {
}
