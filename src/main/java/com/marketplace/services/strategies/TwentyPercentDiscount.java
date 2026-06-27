package com.marketplace.services.strategies;

public class TwentyPercentDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        return price * 0.80;    
    }
}