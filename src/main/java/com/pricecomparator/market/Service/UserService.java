package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.Domain.User;

import java.util.Optional;

public interface UserService {

    /// Functions for user table
    Optional<User> getUserById(int Id);
    boolean removeUserById(int Id);
    boolean addUser(CreateUserRequest user);
    boolean updateUserPassword(int Id,String newPassword);
    boolean updateUserEmail(int Id,String newEmail);




}
