package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByName(String name);

    Product findByNameAndStore(String productName, String store);
}