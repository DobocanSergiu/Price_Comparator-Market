package com.pricecomparator.market.Domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "watch_list_products")
public class WatchListProduct {
    @Id
    @ColumnDefault("nextval('watch_list_products_watchlistproductid_seq')")
    @Column(name = "watchlistproductid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Or GenerationType.AUTO / SEQUENCE based on DB
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "watchlistid", nullable = false)
    private WatchList watchlistid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productid", nullable = false)
    private Product productid;

    @Column(name = "wantedprice", precision = 10, scale = 2)
    private BigDecimal wantedprice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WatchList getWatchlistid() {
        return watchlistid;
    }

    public void setWatchlistid(WatchList watchlistid) {
        this.watchlistid = watchlistid;
    }

    public Product getProductid() {
        return productid;
    }

    public void setProductid(Product productid) {
        this.productid = productid;
    }

    public BigDecimal getWantedprice() {
        return wantedprice;
    }

    public void setWantedprice(BigDecimal wantedprice) {
        this.wantedprice = wantedprice;
    }

}