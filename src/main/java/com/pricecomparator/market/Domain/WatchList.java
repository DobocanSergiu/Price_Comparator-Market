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
public class WatchList {

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
    private Integer watchListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "watchList")
    private Set<WatchListProduct> watchListWatchListProducts;

    public Integer getWatchListId() {
        return watchListId;
    }

    public void setWatchListId(final Integer watchListId) {
        this.watchListId = watchListId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Set<WatchListProduct> getWatchListWatchListProducts() {
        return watchListWatchListProducts;
    }

    public void setWatchListWatchListProducts(
            final Set<WatchListProduct> watchListWatchListProducts) {
        this.watchListWatchListProducts = watchListWatchListProducts;
    }

}
