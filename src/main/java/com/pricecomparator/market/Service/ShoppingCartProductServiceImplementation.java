package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ShoppingCartProduct.CreateShoppingCartProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShoppingCartProductServiceImplementation implements ShoppingCartProductService {
    @Override
    public HttpCode addShoppingCartProduct(CreateShoppingCartProductRequest request) {
        return null;
    }

    @Override
    public HttpCode removeShoppingCartProduct(int shoppingCartProductId) {
        return null;
    }

    @Override
    public HttpCode clearProductFromUserShoppingCart(int productID, int userId) {
        return null;
    }

    @Override
    public HttpCode clearShoppingCartList(int userId) {
        return null;
    }

    @Override
    public List<?> getUserShoppingCart(int userId) {
        return List.of();
    }
}
