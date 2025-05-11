package com.pricecomparator.market.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Set;


@Entity
@Table(name = "\"User\"")
public class User {

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
    private Integer userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "user")
    private Set<ShoppingCart> userShoppingCarts;

    @OneToMany(mappedBy = "user")
    private Set<WatchList> userWatchLists;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<ShoppingCart> getUserShoppingCarts() {
        return userShoppingCarts;
    }

    public void setUserShoppingCarts(final Set<ShoppingCart> userShoppingCarts) {
        this.userShoppingCarts = userShoppingCarts;
    }

    public Set<WatchList> getUserWatchLists() {
        return userWatchLists;
    }

    public void setUserWatchLists(final Set<WatchList> userWatchLists) {
        this.userWatchLists = userWatchLists;
    }

}
