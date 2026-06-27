package com.marketplace.models;

public class Bid {
    private String bidderName;
    private double amount;

    public Bid(String bidderName, double amount) {
        this.bidderName = bidderName;
        this.amount = amount;
    }

    public String getBidderName() { return bidderName; }
    public double getAmount() { return amount; }
}