package com.marketplace.models;

import com.marketplace.services.auction.*;
import com.marketplace.services.Observer;
import java.util.*;

public class Auction {
    private AuctionState currentState;
    private Bid highestBid;
    private List<Observer> bidders = new ArrayList<>();
    private Product product;
    private Timer timer = new Timer();

    public Auction(Product product) {
        this.product = product;
        this.currentState = new OpenState(this);
    }

    public void startAuction(long openDurationMillis, long closingDurationMillis) {
        System.out.println("⏳ Auction initialized for: " + product.getName());
        System.out.println("⏱️ Duration set by seller: " + (openDurationMillis/1000) + "s Open, " + (closingDurationMillis/1000) + "s Closing.");
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentState.nextState();
            }
        }, openDurationMillis);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentState.nextState();
                timer.cancel();
            }
        }, openDurationMillis + closingDurationMillis);
    }

    public void placeBid(Bid bid) {
        currentState.placeBid(bid);
    }

    public void setState(AuctionState state) {
        this.currentState = state;
    }

    public void setHighestBid(Bid bid) {
        this.highestBid = bid;
        notifyBidders("New highest bid: " + bid.getAmount() + " TL by " + bid.getBidderName());
    }

    public void addBidder(Observer bidder) { bidders.add(bidder); }

    public void notifyBidders(String message) {
        for (Observer bidder : bidders) { bidder.update(message); }
    }

    public Bid getHighestBid() { return highestBid; }
    public AuctionState getCurrentState() { return currentState; }
}