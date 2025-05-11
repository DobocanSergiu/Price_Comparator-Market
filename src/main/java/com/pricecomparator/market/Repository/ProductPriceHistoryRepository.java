package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory, Integer> {
  }