package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.ProductPrice.ProductDiscountResponse;
import com.pricecomparator.market.DTO.Response.ProductPrice.ProductPriceResponse;
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
    List<ProductDiscountResponse> getAllDiscounts();
    List<ProductDiscountResponse> getAllPresentOrFutureDiscounts();
    List<ProductDiscountResponse> getAllDiscountsByStore(String store);
    List<ProductDiscountResponse> getAllPresentOrFutureDiscountsByStore(String store);
    List<ProductDiscountResponse> getLast24hDiscounts();
    List<List<ProductDiscountResponse>>  getPricesOfGivenProductAtStores(String productName);
    List<ProductDiscountResponse>  getPricesOfGivenProductAtSpecificStore(String productName,String store);


}
