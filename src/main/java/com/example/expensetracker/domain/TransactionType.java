package com.example.expensetracker.domain;

public enum TransactionType {
    INCOME("income"),
    EXPENSE("expense");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public static TransactionType parse(String rawValue) {
        if (rawValue == null) {
            throw new IllegalArgumentException("交易类型必须是 income 或 expense");
        }
        for (TransactionType type : values()) {
            if (type.value.equals(rawValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("交易类型必须是 income 或 expense");
    }

    public String value() {
        return value;
    }
}
