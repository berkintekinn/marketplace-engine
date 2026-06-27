package com.marketplace.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.models.Order;
import com.marketplace.models.OrderItem;
import com.marketplace.models.OrderStatus;
import com.marketplace.models.Product;
import com.marketplace.models.ProductStatus;
import com.marketplace.models.TransactionRecord;
import com.marketplace.models.User;

@Service
public class PaymentService {
    
    private final List<TransactionRecord> transactionHistory = new ArrayList<>();
    
    @Autowired private CommissionService commissionService;
    @Autowired private SecurityService securityService;
    @Autowired private ProductService productService;

    public void processPayment(User buyer, Order order, User seller) {
        if (order.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        List<Product> products = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            // DÜZELTİLEN KISIM: Artık doğrudan Product nesnesi üzerinden ID alıyoruz
            Long productId = item.getProduct().getId();
            Product product = productService.findProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            products.add(product);
        }

        securityService.validateTransaction(buyer, products);

        double totalCommission = 0;
        double netSellerAmount = 0;

        for (Product p : products) {
            if (p.getStatus() != ProductStatus.APPROVED) {
                throw new RuntimeException("Transaction blocked: Product not approved");
            }

            if (p.getStock() < 1) {
                throw new RuntimeException("Insufficient stock for product: " + p.getName());
            }
            p.setStock(p.getStock() - 1);
            productService.updateProduct(p);

            double commission = commissionService.getCommissionAmount(p, seller);
            totalCommission += commission;
            netSellerAmount += (p.getPrice() - commission);
        }

        order.setStatus(OrderStatus.PROCESSING);

        TransactionRecord record = new TransactionRecord(buyer.getName(), seller.getName(), order.getTotalAmount());
        transactionHistory.add(record);
    }

    public List<TransactionRecord> getTransactionHistory() {
        return transactionHistory;
    }
}