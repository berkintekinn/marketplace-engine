package com.marketplace.services;

import org.springframework.stereotype.Service;

import com.marketplace.models.Product;
import com.marketplace.models.User;

@Service
public class CommissionService {

    public double getCommissionAmount(Product product, User seller) {
        return product.getPrice() * seller.getCommissionRate();
    }
}