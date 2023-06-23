package com.demo.cart.service;

import com.demo.cart.model.CartItem;
import com.demo.cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    public CartItemService(CartItemRepository cartItemRepository){
        this.cartItemRepository = cartItemRepository;
    }
    public CartItem save(CartItem cartItem){return cartItemRepository.save(cartItem);}
    public CartItem getByProductIdAndCartId(Long productId, Long cartId){
        return cartItemRepository.findCartItemByProduct_IdAndCart_Id(productId,cartId);
    }

    public void delete(CartItem cartItem){
        cartItemRepository.delete(cartItem);
    }
}
