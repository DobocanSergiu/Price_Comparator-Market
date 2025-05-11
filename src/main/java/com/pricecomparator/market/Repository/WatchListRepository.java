package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Integer> {
  }