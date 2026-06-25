package com.example.expensetracker.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record TransactionSummary(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        Map<String, BigDecimal> expenseByCategory) {
    public TransactionSummary {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("汇总日期范围不能为空");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("汇总结束日期不能早于开始日期");
        }
        totalIncome = amount(totalIncome);
        totalExpense = amount(totalExpense);
        expenseByCategory = copyAmounts(expenseByCategory);
    }

    public static TransactionSummary forIsoWeek(List<Transaction> transactions, LocalDate dateInWeek) {
        if (dateInWeek == null) {
            throw new IllegalArgumentException("周汇总日期不能为空");
        }
        LocalDate startDate = dateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return forRange(transactions, startDate, startDate.plusDays(6));
    }

    public BigDecimal netIncome() {
        return totalIncome.subtract(totalExpense).setScale(2, RoundingMode.UNNECESSARY);
    }

    public String totalIncomeText() {
        return amountText(totalIncome);
    }

    public String totalExpenseText() {
        return amountText(totalExpense);
    }

    public String netIncomeText() {
        return amountText(netIncome());
    }

    public static String amountText(BigDecimal value) {
        return amount(value).toPlainString();
    }

    private static TransactionSummary forRange(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = BigDecimal.ZERO.setScale(2);
        BigDecimal totalExpense = BigDecimal.ZERO.setScale(2);
        Map<String, BigDecimal> expenseByCategory = new TreeMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.date().isBefore(startDate) || transaction.date().isAfter(endDate)) {
                continue;
            }

            if (transaction.type() == TransactionType.INCOME) {
                totalIncome = totalIncome.add(transaction.amount());
            } else {
                totalExpense = totalExpense.add(transaction.amount());
                expenseByCategory.merge(transaction.category(), transaction.amount(), BigDecimal::add);
            }
        }

        return new TransactionSummary(startDate, endDate, totalIncome, totalExpense, expenseByCategory);
    }

    private static BigDecimal amount(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2);
        }
        return value.setScale(2, RoundingMode.UNNECESSARY);
    }

    private static Map<String, BigDecimal> copyAmounts(Map<String, BigDecimal> source) {
        Map<String, BigDecimal> copy = new LinkedHashMap<>();
        if (source != null) {
            for (Map.Entry<String, BigDecimal> entry : source.entrySet()) {
                copy.put(entry.getKey(), amount(entry.getValue()));
            }
        }
        return Collections.unmodifiableMap(copy);
    }
}
