package com.marketplace.services;

import com.marketplace.models.PriceAlert;
import com.marketplace.models.Product;
import java.util.*;

public class PriceAlertService {
    private List<PriceAlert> alerts = new ArrayList<>();

    public void addAlert(Product product, double threshold, Observer observer) {
        alerts.add(new PriceAlert(product, threshold, observer));
        System.out.println("✅ Alert set: " + product.getName() + " below " + threshold + " TL");
    }

    public void checkAlerts(Product product, double newPrice) {
        for (PriceAlert alert : alerts) {
            if (alert.getProduct().equals(product)) {
                alert.check(newPrice);
            }
        }
    }
}