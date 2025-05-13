package com.pricecomparator.market.DTO.Request.Product;

public class UpdateProductMeasurementRequest {
    private int productId;
    private String measurement;

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

}
