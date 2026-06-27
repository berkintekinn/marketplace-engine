package com.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marketplace.models.Order;
import com.marketplace.models.Product;
import com.marketplace.models.Coupon;
import com.marketplace.services.ProductService;
import com.marketplace.services.CouponService;
import com.marketplace.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private ProductService productService;
    @Autowired private CouponService couponService;
    @Autowired private OrderService orderService;

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PutMapping("/products/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(productService.updateStock(id, quantity));
    }

    @PostMapping("/coupons")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}