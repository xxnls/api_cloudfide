package com.api.API.repository;

import com.api.API.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    /// looks for products with names containing the specified string, ignoring case
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /// finds all products associated with a specific producer, identified by the producer's unique identifier (UUID)
    Page<Product> findByProducerId(UUID producerId, Pageable pageable);
}
