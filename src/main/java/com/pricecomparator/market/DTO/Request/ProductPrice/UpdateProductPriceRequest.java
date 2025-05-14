package com.pricecomparator.market.DTO.Request.ProductPrice;

import java.math.BigDecimal;

public class UpdateProductPriceRequest {
     int productPriceId;
     String price;

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
