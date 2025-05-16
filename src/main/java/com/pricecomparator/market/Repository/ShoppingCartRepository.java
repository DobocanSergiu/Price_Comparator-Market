package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.ShoppingCart;
import com.pricecomparator.market.Domain.ShoppingCartProduct;
import com.pricecomparator.market.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    Optional<ShoppingCart> findByUserid(User userid);

    void deleteByUserid(User userid);
}