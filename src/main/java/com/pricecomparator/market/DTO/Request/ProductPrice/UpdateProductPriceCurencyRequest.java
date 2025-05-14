package com.pricecomparator.market.DTO.Request.ProductPrice;

public class UpdateProductPriceCurencyRequest {
    int productId;
    String curency;

    public String getCurency() {
        return curency;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
