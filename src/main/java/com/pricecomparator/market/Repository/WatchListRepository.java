package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Domain.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchListRepository extends JpaRepository<WatchList, Integer> {
    Optional<WatchList> findByUserid(User userid);

    void deleteByUserid(User userid);
}