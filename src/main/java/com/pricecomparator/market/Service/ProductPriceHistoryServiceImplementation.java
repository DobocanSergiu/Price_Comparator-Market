package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.ProductPriceHistory;

import java.util.List;
import java.util.Optional;

public class ProductPriceHistoryServiceImplementation implements ProductPriceHistoryService{
    @Override
    public Optional<ProductPriceHistory> getProductPriceById(int productPriceId) {
        return Optional.empty();
    }

    @Override
    public Optional<List<ProductPriceHistory>> getAllProductPrices(int productId) {
        return Optional.empty();
    }

    @Override
    public HttpCode removeProductPriceById(int productPriceId) {
        return null;
    }

    @Override
    public HttpCode clearPriceHistoryOfProductById(int productId) {
        return null;
    }

    @Override
    public HttpCode addProductPrice(CreateProductPriceRequest request) {
        return null;
    }

    @Override
    public HttpCode addSalePeriod(CreateSaleRequest request) {
        return null;
    }

    @Override
    public HttpCode updateCurrency(UpdateProductPriceCurencyRequest request) {
        return null;
    }

    @Override
    public HttpCode updateDate(UpdateProductPriceDateRequest request) {
        return null;
    }

    @Override
    public HttpCode updatePrice(UpdateProductPriceRequest request) {
        return null;
    }

    @Override
    public HttpCode updateProductPriceDecreasePercentage(UpdateProductPriceDecreasePercentageRequest request) {
        return null;
    }
}
