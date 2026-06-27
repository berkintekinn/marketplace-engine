package com.marketplace.services.auction;

import com.marketplace.models.Bid;

public interface AuctionState {
    void placeBid(Bid bid);
    void nextState();
    String getStatus();
}