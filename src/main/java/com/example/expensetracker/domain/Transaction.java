package com.example.expensetracker.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;

public record Transaction(
        String id,
        TransactionType type,
        BigDecimal amount,
        String category,
        LocalDate date,
        String note,
        Instant createdAt) {
    public Transaction {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("交易 ID 不能为空");
        }
        if (type == null) {
            throw new IllegalArgumentException("交易类型必须是 income 或 expense");
        }
        validateAmount(amount);
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("分类不能为空");
        }
        if (date == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("创建时间不能为空");
        }
        amount = amount.setScale(2, RoundingMode.UNNECESSARY);
        category = category.trim();
        note = note == null ? "" : note;
    }

    public String amountText() {
        return amount.toPlainString();
    }

    public static BigDecimal parseAmount(String rawAmount) {
        if (rawAmount == null || rawAmount.isBlank()) {
            throw new IllegalArgumentException("金额不能为空");
        }
        try {
            BigDecimal amount = new BigDecimal(rawAmount);
            validateAmount(amount);
            return amount.setScale(2, RoundingMode.UNNECESSARY);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("金额必须是有效数字", exception);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("金额最多只能有 2 位小数", exception);
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("金额不能为空");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("金额必须大于 0");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("金额最多只能有 2 位小数");
        }
    }
}
