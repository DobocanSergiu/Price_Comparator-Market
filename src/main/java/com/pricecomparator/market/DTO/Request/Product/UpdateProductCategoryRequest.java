package com.pricecomparator.market.DTO.Request.Product;

public class UpdateProductCategoryRequest {
    private int productId;
    private String categoryName;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
