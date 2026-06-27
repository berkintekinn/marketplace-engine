package com.marketplace.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.marketplace.models.Product;
import com.marketplace.models.ProductStatus;
import com.marketplace.models.Role;
import com.marketplace.models.User;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.UserRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> searchProducts(Specification<Product> spec) {
        return repository.findAll(spec);
    }

    public List<Product> getProductsBySeller(String email) {
        User seller = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Seller not found"));
        return repository.findBySellerId(seller.getId());
    }

    public Optional<Product> findProductById(Long id) {
        return repository.findById(id);
    }

    public Product addProduct(Product product) {
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.PENDING);
        }
        if (product.getPrice() < 0) {
            product.setPrice(0.0);
        }
        return repository.save(product);
    }

    public Product updateStock(Long id, int quantity) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(product.getStock() + quantity);
        return repository.save(product);
    }

    public Product createProduct(Product product, String sellerEmail) {
        User seller = userRepository.findByEmail(sellerEmail)
            .orElseThrow(() -> new RuntimeException("Seller not found"));

        product.setSellerId(seller.getId()); 
        
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.PENDING);
        }
        if (product.getPrice() < 0) {
            product.setPrice(0.0);
        }
        
        return repository.save(product);
    }

    public Product updateProduct(Long productId, Product productDetails, String email) {
        User requester = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Product existingProduct = repository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!existingProduct.getSellerId().equals(requester.getId()) && !requester.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("You do not have permission for this action!");
        }

        existingProduct.setName(productDetails.getName());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStock(productDetails.getStock());
        
        return repository.save(existingProduct);
    }

    public void updateProduct(Product product) {
        repository.save(product);
    }

    public void deleteProduct(Long productId, String email) {
        User requester = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Product existingProduct = repository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!existingProduct.getSellerId().equals(requester.getId()) && !requester.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("You do not have permission to delete this product!");
        }

        repository.deleteById(productId);
    }
}