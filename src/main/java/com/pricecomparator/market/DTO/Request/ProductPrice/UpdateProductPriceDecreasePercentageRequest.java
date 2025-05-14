package com.pricecomparator.market.DTO.Request.ProductPrice;

public class UpdateProductPriceDecreasePercentageRequest {
    int productPriceId;
    int percentage;

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
