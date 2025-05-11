package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}