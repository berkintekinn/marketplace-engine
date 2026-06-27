package com.marketplace.services.auction;

import com.marketplace.models.Auction;
import com.marketplace.models.Bid;

public class OpenState implements AuctionState {
    private Auction auction;

    public OpenState(Auction auction) {
        this.auction = auction;
    }

    @Override
    public void placeBid(Bid bid) {
        if (auction.getHighestBid() == null || bid.getAmount() > auction.getHighestBid().getAmount()) {
            auction.setHighestBid(bid);
        } else {
            System.out.println("⚠️ Bid rejected: Amount must be higher than current highest bid.");
        }
    }

    @Override
    public void nextState() {
        auction.setState(new ClosingState(auction));
        System.out.println("📢 Auction status changed to: CLOSING");
    }

    @Override
    public String getStatus() {
        return "OPEN";
    }
}