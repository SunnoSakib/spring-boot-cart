package com.demo.cart.service;

import com.demo.cart.model.Product;
import com.demo.cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product save(Product product){
        return productRepository.save(product);
    }
    public Optional<Product> getById(Long id){return productRepository.findById(id);}
}
