package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.WatchList;

import java.util.Optional;

public interface WatchListService {

    Optional<WatchList> getWatchListById(int watchListId);
    Optional<WatchList> getWatchListByUserId(int userId);
    HttpCode addWatchList(int UserId);
    HttpCode deleteWatchListById(int watchListId);
    HttpCode deleteWatchlistByUserId(int userId);



}
