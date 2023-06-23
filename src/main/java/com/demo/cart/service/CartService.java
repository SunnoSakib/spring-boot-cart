package com.demo.cart.service;

import com.demo.cart.model.Cart;
import com.demo.cart.repository.CartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    public Cart save(Cart cart){ return cartRepository.save(cart);}

    public Page<Cart> getAll(Pageable pageable){
        return cartRepository.findAll(pageable);
    }

    public Optional<Cart> getById(Long id){
        return cartRepository.findById(id);
    }

    public Cart getByUId(String uid){
        return cartRepository.findCartByUid(uid);
    }
    public Cart getByCustomerId(Long customerId){
        return cartRepository.findCartByCustomer_Id(customerId);
    }

}
