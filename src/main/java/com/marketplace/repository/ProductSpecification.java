package com.marketplace.repository;

import org.springframework.data.jpa.domain.Specification;

import com.marketplace.models.Category;
import com.marketplace.models.Product;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> 
            (name == null || name.isEmpty()) ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategory(Category category) {
        return (root, query, cb) -> 
            category == null ? null : cb.equal(root.get("category"), category);
    }
}