package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;

public interface ShoppingCartService {
    HttpCode addShoppingCart(int UserId);
    HttpCode deleteShoppingCartById(int shoppingCartId);
    HttpCode deleteShoppingCartByUserId(int userId);

}
