package com.marketplace.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketplace.models.Cart;
import com.marketplace.models.Coupon;
import com.marketplace.models.Order;
import com.marketplace.models.OrderItem;
import com.marketplace.models.OrderStatus;
import com.marketplace.models.Product;
import com.marketplace.models.User;
import com.marketplace.models.Wallet;
import com.marketplace.repository.CartRepository;
import com.marketplace.repository.CouponRepository;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.WalletRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository; 
    
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Order createOrder(User user, String couponCode) {
        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        for (var cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (!product.reduceStock(cartItem.getQuantity())) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            productRepository.save(product);
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setUser(user);
        
        double finalPrice = cart.getTotalPrice();
        if (couponCode != null && !couponCode.isEmpty()) {
            Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Invalid coupon code"));

            if (!coupon.isActive() || coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Coupon is expired or inactive");
            }
            
            double discountAmount = (finalPrice * coupon.getDiscountPercentage()) / 100;
            finalPrice -= discountAmount;
        }
        
        Wallet wallet = walletRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Wallet not found for the user."));

        if (!wallet.withdraw(finalPrice)) {
            throw new RuntimeException("Insufficient balance! Current balance: " + wallet.getBalance() + " TL, Required: " + finalPrice + " TL.");
        }
        
        walletRepository.save(wallet);

        order.setTotalAmount(finalPrice);
        order.setStatus(OrderStatus.PREPARING); 
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = cart.getItems().stream()
            .map(item -> new OrderItem(order, item.getProduct(), item.getQuantity(), item.getProduct().getPrice()))
            .collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
}