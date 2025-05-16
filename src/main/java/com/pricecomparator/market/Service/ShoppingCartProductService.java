package com.pricecomparator.market.Service;



import com.pricecomparator.market.DTO.Request.ShoppingCartProduct.CreateShoppingCartProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ShoppingCartProductService {
    HttpCode addShoppingCartProduct(@RequestBody CreateShoppingCartProductRequest request);
    HttpCode removeShoppingCartProduct(int shoppingCartProductId);
    HttpCode clearProductFromUserShoppingCart(int productID, int userId);
    HttpCode clearShoppingCartList(int userId);
    List<?> getUserShoppingCart(int userId);

}
