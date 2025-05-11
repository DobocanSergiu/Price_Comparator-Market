package com.pricecomparator.market.Service;

import com.pricecomparator.market.Domain.Users;

import java.util.Optional;

public interface UserService {

    Optional<Users> getUserById(int Id);
}
