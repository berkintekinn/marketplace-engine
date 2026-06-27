package com.marketplace.services;

import com.marketplace.models.*;
import com.marketplace.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return cartRepository.findByUser(user)
            .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    @Transactional
    public void addToCart(String email, Long productId, int quantity) {
        Cart cart = getOrCreateCart(email);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        CartLogic logic = new CartLogic(cart);
        logic.addItem(product, quantity);

        cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(String email, Long productId) {
        Cart cart = getOrCreateCart(email);
        
        CartLogic logic = new CartLogic(cart);
        logic.removeItem(productId);
        
        cartRepository.save(cart);
    }

    @Transactional
    public void checkout(String email) {
        Cart cart = getOrCreateCart(email);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 1. Stok Kontrolü ve Düşümü
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }


        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
}