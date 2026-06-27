package com.marketplace.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionRecord {
    private String transactionId;
    private String customerName;
    private String sellerName;
    private double amount;
    private LocalDateTime timestamp;

    public TransactionRecord(String customerName, String sellerName, double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.sellerName = sellerName;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | %s -> %s | Amount: %.2f TL", 
            timestamp, transactionId, customerName, sellerName, amount);
    }
}