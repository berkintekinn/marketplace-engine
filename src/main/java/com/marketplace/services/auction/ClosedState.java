package com.marketplace.services.auction;

import com.marketplace.models.Auction;
import com.marketplace.models.Bid;

public class ClosedState implements AuctionState {
    private Auction auction;

    public ClosedState(Auction auction) {
        this.auction = auction;
    }

    @Override
    public void placeBid(Bid bid) {
        System.out.println("❌ Auction is closed. Bid rejected.");
    }

    @Override
    public void nextState() {
        System.out.println("ℹ️ Auction is already closed.");
    }

    @Override
    public String getStatus() {
        if (auction.getHighestBid() != null) {
            return "CLOSED - Winner: " + auction.getHighestBid().getBidderName();
        }
        return "CLOSED - No winner";
    }
}