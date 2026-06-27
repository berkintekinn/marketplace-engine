package com.marketplace.models;

import com.marketplace.services.Observer;
import com.marketplace.services.strategies.DiscountStrategy;
import java.util.*;

public class GroupBuy {
    private List<Observer> members = new ArrayList<>();
    private int quota;
    private Product product;
    private DiscountStrategy discountStrategy;
    private Timer timer = new Timer();
    private boolean isClosed = false;

    public GroupBuy(Product product, int quota, DiscountStrategy discountStrategy) {
        this.product = product;
        this.quota = quota;
        this.discountStrategy = discountStrategy;
    }

    public void joinGroup(Observer customer) {
        if (isClosed) return;
        members.add(customer);
        System.out.println("👥 New member joined! Current size: " + members.size());
        
        if (members.size() >= quota) {
            completeGroup();
        }
    }

    public void addMember(Observer member) {
        this.joinGroup(member);
    }

    private void completeGroup() {
        if (isClosed) return;
        isClosed = true;
        timer.cancel();
        double discountedPrice = discountStrategy.applyDiscount(product.getPrice());
        notifyAllMembers("🎉 Goal reached! Discount applied. New price: " + discountedPrice + " TL");
    }

    public void startTimer(long durationMillis) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isClosed) {
                    isClosed = true;
                    notifyAllMembers("❌ Time is up! Group shopping cancelled.");
                    timer.cancel();
                }
            }
        }, durationMillis);
    }

    private void notifyAllMembers(String message) {
        for (Observer member : members) {
            member.update(message);
        }
    }
}