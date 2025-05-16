package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ShoppingCart;
import com.pricecomparator.market.Domain.ShoppingCartProduct;
import com.pricecomparator.market.Domain.WatchListProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Integer> {
    void removeAllByProductidAndShoppingcartid(Product productid, ShoppingCart shoppingcartid);

    void deleteAllByShoppingcartid(ShoppingCart shoppingcartid);

    List<ShoppingCartProduct> getAllByShoppingcartid(ShoppingCart shoppingcartid);
}