package com.marketplace.controller;

import com.marketplace.models.Cart;
import com.marketplace.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(cartService.getOrCreateCart(email));
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(
            @PathVariable Long productId,
            @RequestParam int quantity,
            Authentication authentication) {
        
        String email = authentication.getName();
        cartService.addToCart(email, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(
            @PathVariable Long productId,
            Authentication authentication) {
        
        String email = authentication.getName();
        cartService.removeFromCart(email, productId);
        return ResponseEntity.ok("Product removed from cart.");
    }

    @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN')")
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Authentication authentication) {
        String email = authentication.getName();
        cartService.checkout(email);
        return ResponseEntity.ok("Order placed successfully. Cart cleared.");
    }
}