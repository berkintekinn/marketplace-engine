package com.marketplace.services.strategies;
import com.marketplace.services.CommissionStrategy;

public class SilverStrategy implements CommissionStrategy {
    @Override
    public double getCommissionRate() { return 0.12; }
}