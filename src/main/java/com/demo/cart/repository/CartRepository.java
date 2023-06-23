package com.demo.cart.repository;

import com.demo.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findCartByUid(String uid);
    Cart findCartByCustomer_Id(Long customerId);
}
