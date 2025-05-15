package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImplementation implements ShoppingCartService{

    private final ShoppingCartRepository shoppingCartRepository;
    public ShoppingCartServiceImplementation(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;

    }
    @Override
    public HttpCode addShoppingCart(int UserId) {
        return null;
    }

    @Override
    public HttpCode deleteShoppingCartById(int shoppingCartId) {
        return null;
    }

    @Override
    public HttpCode deleteShoppingCartByUserId(int userId) {
        return null;
    }
}
