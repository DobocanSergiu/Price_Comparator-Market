package com.pricecomparator.market.DTO.Request.ProductPrice;

public class UpdateProductPriceCurencyRequest {
    int productId;
    String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String curency) {
        this.currency = curency;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
