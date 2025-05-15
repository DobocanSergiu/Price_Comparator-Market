package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.WatchList;

import java.util.Optional;

public interface WatchListService {


    HttpCode addWatchList(int UserId);
    HttpCode deleteWatchListById(int watchListId);
    HttpCode deleteWatchlistByUserId(int userId);



}
