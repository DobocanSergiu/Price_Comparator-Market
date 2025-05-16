package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Domain.WatchListProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListProductRepository extends JpaRepository<WatchListProduct, Integer> {
    boolean findByProductidAndWatchlistid(Product productid, WatchList watchlistid);

    boolean existsByProductidAndWatchlistid(Product productid, WatchList watchlistid);

    void removeAllByProductidAndWatchlistid(Product productid, WatchList watchlistid);

    void deleteAllByWatchlistid(WatchList watchlistid);

    List<WatchListProduct> getAllByProductid(Product productid);

    List<WatchListProduct> getAllByWatchlistid(WatchList watchlistid);
}