package com.demo.cart.repository;

import com.demo.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    public CartItem findCartItemByProduct_IdAndCart_Id(Long productId,Long cartId );
}
