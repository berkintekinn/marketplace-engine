package com.marketplace.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.marketplace.models.Product;
import com.marketplace.models.Role;
import com.marketplace.models.User;

@Service
public class SecurityService {
    
    private final List<TransactionRecord> history = new ArrayList<>();

    public void authorizeProductCreation(User user) {
        if (user.getRole() == Role.CUSTOMER) {
            throw new SecurityException("Customers cannot create products.");
        }
    }

    public void validateTransaction(User user, List<Product> cart) throws SecurityException {
        double total = cart.stream().mapToDouble(Product::getPrice).sum();

        if (total > 50000.0) {
            throw new SecurityException("FRAUD ALERT: Transaction flagged for high value: " + total + " TL");
        }

        LocalDateTime now = LocalDateTime.now();
        for (Product p : cart) {
            long count = history.stream()
                .filter(t -> t.customerId.equals(user.getId()) && 
                             t.productId.equals(p.getId()) &&
                             t.timestamp.isAfter(now.minusMinutes(2)))
                .count();
            
            if (count >= 5) {
                throw new SecurityException("FRAUD ALERT: Suspicious activity detected (Bot pattern).");
            }
        }

        for (Product p : cart) {
            history.add(new TransactionRecord(user.getId(), p.getId(), now));
        }
    }

    private static class TransactionRecord {
        Long customerId;
        Long productId;
        LocalDateTime timestamp;

        TransactionRecord(Long customerId, Long productId, LocalDateTime timestamp) {
            this.customerId = customerId;
            this.productId = productId;
            this.timestamp = timestamp;
        }
    }
}