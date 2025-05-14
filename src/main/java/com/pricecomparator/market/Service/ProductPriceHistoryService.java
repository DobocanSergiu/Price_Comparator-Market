package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.ProductPriceHistory;

import java.util.List;
import java.util.Optional;

public interface ProductPriceHistoryService {

    Optional<ProductPriceHistory> getProductPriceById(int productPriceId);
    Optional<List<ProductPriceHistory>> getAllProductPrices(int productId);
    HttpCode removeProductPriceById(int productPriceId);
    HttpCode clearPriceHistoryOfProductById(int productId);
    HttpCode addProductPrice(CreateProductPriceRequest request);
    HttpCode addSalePeriod(CreateSaleRequest request);
    HttpCode updateCurrency(UpdateProductPriceCurencyRequest request);
    HttpCode updateDate(UpdateProductPriceDateRequest request);
    HttpCode updatePrice(UpdateProductPriceRequest request);
    HttpCode updateProductPriceDecreasePercentage(UpdateProductPriceDecreasePercentageRequest request);


}
