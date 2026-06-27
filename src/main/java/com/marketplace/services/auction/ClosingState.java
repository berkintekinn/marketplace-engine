package com.marketplace.services.auction;

import com.marketplace.models.Auction;
import com.marketplace.models.Bid;

public class ClosingState implements AuctionState {
    private Auction auction;

    public ClosingState(Auction auction) {
        this.auction = auction;
    }

    @Override
    public void placeBid(Bid bid) {
        System.out.println("⚠️ Auction is closing! No new bids accepted.");
    }

    @Override
    public void nextState() {
        auction.setState(new ClosedState(auction));
        System.out.println("📢 Auction status changed to: CLOSED");
    }

    @Override
    public String getStatus() {
        return "CLOSING";
    }
}