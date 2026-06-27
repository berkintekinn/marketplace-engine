package com.marketplace.dto;

import java.util.List;

import com.marketplace.models.OrderItem;

public class OrderRequest {
    private String customerId;
    private List<OrderItem> items;

    public OrderRequest() {}

    public OrderRequest(String customerId, List<OrderItem> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}