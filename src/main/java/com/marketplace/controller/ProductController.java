package com.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.models.Category;
import com.marketplace.models.Product;
import com.marketplace.repository.ProductSpecification;
import com.marketplace.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ARAMA METODU EKLENDİ
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category) {
        
        Specification<Product> spec = Specification.where(ProductSpecification.hasName(name))
                                                   .and(ProductSpecification.hasCategory(category));
        
        return ResponseEntity.ok(productService.searchProducts(spec));
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product, Authentication authentication) {
        String sellerEmail = authentication.getName(); 
        Product createdProduct = productService.createProduct(product, sellerEmail);
        return ResponseEntity.ok(createdProduct);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails, Authentication authentication) {
        String email = authentication.getName();
        Product updatedProduct = productService.updateProduct(id, productDetails, email);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        productService.deleteProduct(id, email);
        return ResponseEntity.noContent().build();
    }
}