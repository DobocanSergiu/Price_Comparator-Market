package com.pricecomparator.market.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart_products")
public class ShoppingCartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shoppingcartproductid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shoppingcartid", nullable = false)
    private ShoppingCart shoppingcartid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productid", nullable = false)
    private Product productid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ShoppingCart getShoppingcartid() {
        return shoppingcartid;
    }

    public void setShoppingcartid(ShoppingCart shoppingcartid) {
        this.shoppingcartid = shoppingcartid;
    }

    public Product getProductid() {
        return productid;
    }

    public void setProductid(Product productid) {
        this.productid = productid;
    }

}