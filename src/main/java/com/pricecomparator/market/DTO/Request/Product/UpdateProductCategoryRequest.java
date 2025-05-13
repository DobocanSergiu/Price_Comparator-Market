package com.pricecomparator.market.DTO.Request.Product;

public class UpdateProductCategoryRequest {
    private int productId;
    private String category;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return category;
    }

    public void setCategoryName(String categoryName) {
        this.category = categoryName;
    }

}
