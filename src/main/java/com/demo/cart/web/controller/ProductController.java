package com.demo.cart.web.controller;

import com.demo.cart.model.Product;
import com.demo.cart.service.ProductService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Md Mahmud Hasan
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("")
    public Product createOrUpdate(@RequestBody Product product) {
        return productService.save(product);
    }
}
