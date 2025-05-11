package com.pricecomparator.market.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;


@Entity
public class ShoppingCart {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer shoppingCartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "shoppingCart")
    private Set<ShoppingCartProduct> shoppingCartShoppingCartProducts;

    public Integer getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(final Integer shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Set<ShoppingCartProduct> getShoppingCartShoppingCartProducts() {
        return shoppingCartShoppingCartProducts;
    }

    public void setShoppingCartShoppingCartProducts(
            final Set<ShoppingCartProduct> shoppingCartShoppingCartProducts) {
        this.shoppingCartShoppingCartProducts = shoppingCartShoppingCartProducts;
    }

}
