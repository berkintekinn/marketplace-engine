package com.marketplace.services;

import com.marketplace.models.Product;
import java.util.HashMap;
import java.util.Map;

public class FlashSaleService 
{
    private Map<String, Long> activeSales = new HashMap<>();

    public void startSale(Product p, double discountRate) {
        p.setOriginalPrice(p.getPrice());
        double salePrice = p.getPrice() * (1 - discountRate);
        p.setPrice(salePrice);
        
        System.out.println("🔥 FLASH SALE STARTED for " + p.getName() + "! New Price: " + salePrice + " TL");
    }

    public void endSale(Product p) {
        if (p.getOriginalPrice() > 0) {
            p.setPrice(p.getOriginalPrice());
            System.out.println("⏰ Flash Sale Ended for " + p.getName() + ". Price restored to: " + p.getPrice() + " TL");
        }
    }
}