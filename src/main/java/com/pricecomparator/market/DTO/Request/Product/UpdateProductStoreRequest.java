package com.pricecomparator.market.DTO.Request.Product;

public class UpdateProductStoreRequest {
    private int productId;
    private String storeName;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
