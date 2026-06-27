package com.marketplace.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.services.Observer;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "wishlistObservers"})
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @JsonProperty("price")
    @Min(value = 0, message = "Price must be 0 or greater")
    private double price;

    @JsonProperty("originalPrice")
    private double originalPrice;

    @JsonProperty("stock")
    @Min(value = 0, message = "Stock amount cannot be negative")
    private int stock;

    @JsonProperty("category")
    @NotNull(message = "Category field is required")
    @Enumerated(EnumType.STRING)
    private Category category;

    @JsonProperty("sellerId") 
    private Long sellerId;

    @JsonProperty("status") 
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @JsonIgnore
    @Transient
    private List<Observer> wishlistObservers = new ArrayList<>();

    public Product() { }

    public Product(String name, double price, int stock, Category category, Long sellerId) {
        this.name = name;
        this.price = Math.max(price, 0.0);
        this.originalPrice = this.price;
        this.stock = Math.max(stock, 0);
        this.category = category;
        this.sellerId = sellerId;
        this.status = ProductStatus.PENDING;
    }

    public void addWishlistSubscriber(Observer o) { wishlistObservers.add(o); }
    public void removeWishlistSubscriber(Observer o) { wishlistObservers.remove(o); }

    private void notifyWishlistSubscribers(String message) {
        for (Observer o : wishlistObservers) {
            o.update(message);
        }
    }

    public boolean reduceStock(int amount) {
        if (amount > 0 && this.stock >= amount) {
            this.stock -= amount;
            return true;
        }
        return false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public int getStock() { return stock; }
    public void setStock(int newStock) {
        if (this.stock == 0 && newStock > 0) {
            notifyWishlistSubscribers("Product " + name + " is back in stock!");
        }
        this.stock = Math.max(newStock, 0);
    }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public void displayProductInfo() {
        System.out.printf("📦 Product: %s | Category: %s | Status: %s | 💰 Price: %.2f | 📊 Stock: %d%n", 
                          name, category, status, price, stock);
    }
}