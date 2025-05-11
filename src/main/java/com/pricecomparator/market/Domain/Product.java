package com.pricecomparator.market.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;


@Entity
public class Product {

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
    private Integer productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 20)
    private String measurement;

    @OneToMany(mappedBy = "product")
    private Set<ShoppingCartProduct> productShoppingCartProducts;

    @OneToMany(mappedBy = "product")
    private Set<WatchListProduct> productWatchListProducts;

    @OneToMany(mappedBy = "product")
    private Set<ProductPriceHistory> productProductPriceHistories;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(final Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(final String measurement) {
        this.measurement = measurement;
    }

    public Set<ShoppingCartProduct> getProductShoppingCartProducts() {
        return productShoppingCartProducts;
    }

    public void setProductShoppingCartProducts(
            final Set<ShoppingCartProduct> productShoppingCartProducts) {
        this.productShoppingCartProducts = productShoppingCartProducts;
    }

    public Set<WatchListProduct> getProductWatchListProducts() {
        return productWatchListProducts;
    }

    public void setProductWatchListProducts(final Set<WatchListProduct> productWatchListProducts) {
        this.productWatchListProducts = productWatchListProducts;
    }

    public Set<ProductPriceHistory> getProductProductPriceHistories() {
        return productProductPriceHistories;
    }

    public void setProductProductPriceHistories(
            final Set<ProductPriceHistory> productProductPriceHistories) {
        this.productProductPriceHistories = productProductPriceHistories;
    }

}
