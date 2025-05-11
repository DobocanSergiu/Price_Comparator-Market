package com.pricecomparator.market.Service;

import com.pricecomparator.market.Domain.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(int Id);
}
