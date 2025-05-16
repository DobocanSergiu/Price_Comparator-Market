package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Domain.WatchListProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListProductRepository extends JpaRepository<WatchListProduct, Integer> {
    boolean findByProductidAndWatchlistid(Product productid, WatchList watchlistid);

    boolean existsByProductidAndWatchlistid(Product productid, WatchList watchlistid);
}