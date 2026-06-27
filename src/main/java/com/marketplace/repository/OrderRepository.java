package com.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketplace.models.Order;
import com.marketplace.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    List<Order> findByUser(User user);

    List<Order> findByUserOrderByOrderDateDesc(User user);
}