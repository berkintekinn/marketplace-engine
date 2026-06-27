package com.marketplace.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketplaceData {
    @JsonProperty("products")
    public List<Product> products;
}