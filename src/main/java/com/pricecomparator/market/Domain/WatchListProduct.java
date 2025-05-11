package com.pricecomparator.market.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.math.BigDecimal;


@Entity
public class WatchListProduct {

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
    private Integer watchListProductId;

    @Column(precision = 12, scale = 2)
    private BigDecimal wantedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_list_id", nullable = false)
    private WatchList watchList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Integer getWatchListProductId() {
        return watchListProductId;
    }

    public void setWatchListProductId(final Integer watchListProductId) {
        this.watchListProductId = watchListProductId;
    }

    public BigDecimal getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(final BigDecimal wantedPrice) {
        this.wantedPrice = wantedPrice;
    }

    public WatchList getWatchList() {
        return watchList;
    }

    public void setWatchList(final WatchList watchList) {
        this.watchList = watchList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

}
