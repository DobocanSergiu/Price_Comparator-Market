package com.pricecomparator.market.DTO.Request.ShoppingCartProduct;

public class CreateShoppingCartProductRequest {
    private int shoppingCartId;
    private int productId;

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
