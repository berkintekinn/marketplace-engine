package com.marketplace.services.strategies;
import com.marketplace.services.CommissionStrategy;

public class PlatinumStrategy implements CommissionStrategy {
    @Override
    public double getCommissionRate() { return 0.05; }
}