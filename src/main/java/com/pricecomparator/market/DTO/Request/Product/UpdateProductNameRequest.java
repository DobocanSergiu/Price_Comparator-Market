package com.pricecomparator.market.DTO.Request.Product;

public class UpdateProductNameRequest {

    private int productId;
    private String name;

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }



}
