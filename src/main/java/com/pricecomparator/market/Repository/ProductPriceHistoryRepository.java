package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory, Integer> {
    Optional<List<ProductPriceHistory>> getAllByProductid(Product productid);

    void deleteAllByProductid(Product productid);

    List<ProductPriceHistory> findByProductidAndDate(Product productid, Instant date);

    List<ProductPriceHistory> getAllByPricedecreasepercentage(BigDecimal pricedecreasepercentage);
}