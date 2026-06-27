package com.marketplace.models;

import com.marketplace.services.Observer;

public class PriceAlert {
    private Product product;
    private double threshold;
    private Observer observer;

    public PriceAlert(Product product, double threshold, Observer observer) {
        this.product = product;
        this.threshold = threshold;
        this.observer = observer;
    }

    public void check(double currentPrice) {
        if (currentPrice <= threshold) {
            observer.update("🔔 Price Alert: " + product.getName() + " is now " + currentPrice + " TL (Threshold: " + threshold + " TL)");
        }
    }
    
    public Product getProduct() { return product; }
}