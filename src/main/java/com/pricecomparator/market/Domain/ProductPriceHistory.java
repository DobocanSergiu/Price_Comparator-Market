package com.pricecomparator.market.Domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product_price_histories")
public class ProductPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productpricehistoryid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productid", nullable = false)
    private Product productid;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "pricedecreasepercentage", precision = 5, scale = 2)
    private BigDecimal pricedecreasepercentage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProductid() {
        return productid;
    }

    public void setProductid(Product productid) {
        this.productid = productid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPricedecreasepercentage() {
        return pricedecreasepercentage;
    }

    public void setPricedecreasepercentage(BigDecimal pricedecreasepercentage) {
        this.pricedecreasepercentage = pricedecreasepercentage;
    }

}