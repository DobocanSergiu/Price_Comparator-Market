package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Integer> {
}