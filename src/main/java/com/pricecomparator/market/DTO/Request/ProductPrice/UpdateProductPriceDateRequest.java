package com.pricecomparator.market.DTO.Request.ProductPrice;

public class UpdateProductPriceDateRequest {
    int productPriceId;
    String date;

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
