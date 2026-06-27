package com.marketplace;

import com.marketplace.models.Product;
import com.marketplace.models.Role;
import com.marketplace.models.UnauthorizedAccessException;
import com.marketplace.models.User;
import java.util.ArrayList;
import java.util.List;

public class Market {
    private List<Product> products;

    public Market() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product, User requester) {
        if (requester.getRole() != Role.SELLER && requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("SECURITY ALERT: Unauthorized product creation attempt by: " + requester.getName());
        }
        
        products.add(product);
        System.out.println("✅ Product added by " + requester.getName() + ": " + product.getName());
    }

    public void searchProduct(String name) {
        System.out.println("\n--- Search Results: '" + name + "' ---");
        boolean found = false;
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                p.displayProductInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No results found.");
    }

    public List<Product> getProducts() { return products; }
}