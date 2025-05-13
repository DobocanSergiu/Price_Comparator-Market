package com.pricecomparator.market.DTO.Request.Product;

public class UpdateProductBrandRequest {

    private int productId;
    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


}
