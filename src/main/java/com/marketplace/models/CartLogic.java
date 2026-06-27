package com.marketplace.models;

import java.util.Optional;

public class CartLogic {
    
    private final Cart cart;

    public CartLogic(Cart cart) {
        this.cart = cart;
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this.cart, product, quantity);
            cart.getItems().add(newItem);
        }
        
        cart.calculateTotalPrice();
    }

    public void removeItem(Long productId) {
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cart.calculateTotalPrice();
    }

    public void clearCart() {
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
    }
}