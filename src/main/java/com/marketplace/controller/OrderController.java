package com.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.models.Order;
import com.marketplace.models.User;
import com.marketplace.repository.UserRepository;
import com.marketplace.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(Authentication authentication, 
                                             @RequestParam(required = false) String couponCode) {
        User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Order createdOrder = orderService.createOrder(user, couponCode);
        return ResponseEntity.ok(createdOrder);
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @GetMapping("/history")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        List<Order> history = orderService.getOrderHistory(user);
        return ResponseEntity.ok(history);
    }
}