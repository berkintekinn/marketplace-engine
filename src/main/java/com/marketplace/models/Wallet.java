package com.marketplace.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double balance = 0.0;

    // Eşzamanlılık (Concurrency) kalkanımız
    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Wallet() {}

    public Wallet(User user, double initialBalance) {
        this.user = user;
        this.balance = initialBalance;
    }

    public boolean withdraw(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}