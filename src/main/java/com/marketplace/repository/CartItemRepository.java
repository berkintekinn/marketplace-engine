package com.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}