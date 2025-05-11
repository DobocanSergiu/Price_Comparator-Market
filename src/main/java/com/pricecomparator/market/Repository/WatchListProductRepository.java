package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.WatchListProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListProductRepository extends JpaRepository<WatchListProduct, Integer> {
  }