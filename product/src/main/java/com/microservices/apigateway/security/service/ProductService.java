package com.microservices.apigateway.security.service;

import com.microservices.apigateway.security.model.Product;
import com.microservices.apigateway.security.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public ProductService( ProductRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Product findOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        product.setCreated(LocalDateTime.now());
        return repository.save(product);
    }

    public Product update(Product product) {
        Product old = repository.findById(product.getId()).get();

        product.setCreated(old.getCreated());
        product.setId(old.getId());

        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
