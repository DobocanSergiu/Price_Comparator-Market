package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.WatchListProduct.CreateWatchListProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.WatchListProduct.MostWantedRequest;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface WatchListProductService {

    HttpCode addWatchListProduct(@RequestBody CreateWatchListProductRequest request);
    HttpCode removeWatchListProduct(int watchListProductId);
    HttpCode clearProductFromUserWatchList(int productID, int userId);
    HttpCode clearUserWatchList(int userId);
    List<?> getUserWatchList(int userId);

    @Transactional
    List<?> getUserWatchListAtTargetOrLower(int userId);

    @Transactional
    MostWantedRequest getMostWantedProduct();
}
