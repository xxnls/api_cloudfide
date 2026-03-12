package com.api.API.service;

import com.api.API.dto.Producer.ProducerResponse;
import com.api.API.dto.Product.*;
import com.api.API.model.Producer;
import com.api.API.model.Product;
import com.api.API.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(String search, UUID producerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Product> productPage;

        if (search != null && !search.isBlank()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (producerId != null) {
            productPage = productRepository.findByProducerId(producerId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.getContent().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Producer producer = producerRepository.findById(request.producerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        Product product = new Product();
        product.setName(request.name());
        product.setProducer(producer);
        product.setAttributes(request.attributes());

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Producer producer = producerRepository.findById(request.producerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        product.setName(request.name());
        product.setProducer(producer);

        product.getAttributes().clear();
        if (request.attributes() != null) {
            product.getAttributes().putAll(request.attributes());
        }

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                new ProducerResponse(
                        product.getProducer().getId(),
                        product.getProducer().getName()
                ),
                product.getAttributes()
        );
    }
}
