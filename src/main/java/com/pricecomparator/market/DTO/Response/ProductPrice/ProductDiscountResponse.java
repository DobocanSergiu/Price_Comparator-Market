package com.pricecomparator.market.DTO.Response.ProductPrice;

import com.pricecomparator.market.Domain.ProductPriceHistory;

import java.math.BigDecimal;

public class ProductDiscountResponse {
    private int productPriceHistoryId;
    private String currency;
    private String date;
    private String price;
    private BigDecimal priceDecreasePercentage;
    private int productId;
    private String productName;
    private String productBrand;
    private String Store;

    public ProductDiscountResponse(ProductPriceHistory productPriceHistory) {

        productPriceHistoryId = productPriceHistory.getId();
        currency = productPriceHistory.getCurrency();
        date = productPriceHistory.getDate().toString();
        price = productPriceHistory.getPrice().toString();
        priceDecreasePercentage = productPriceHistory.getPricedecreasepercentage();
        productId = productPriceHistory.getProductid().getId();
        productName = "";
        productBrand = "";
        Store = "";
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getProductPriceHistoryId() {
        return productPriceHistoryId;
    }

    public void setProductPriceHistoryId(int productPriceHistoryId) {
        this.productPriceHistoryId = productPriceHistoryId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getPriceDecreasePercentage() {
        return priceDecreasePercentage;
    }

    public void setPriceDecreasePercentage(BigDecimal priceDecreasePercentage) {
        this.priceDecreasePercentage = priceDecreasePercentage;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }
}
