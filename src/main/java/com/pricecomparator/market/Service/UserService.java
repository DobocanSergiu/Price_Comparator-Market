package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Response.ErrorCode;
import com.pricecomparator.market.Domain.User;

import java.util.Optional;

public interface UserService {

    /// Functions for Users table
    Optional<User> getUserById(int Id);
    ErrorCode removeUserById(int Id);
    ErrorCode addUser(CreateUserRequest user);
    ErrorCode updateUserPassword(int Id,String newPassword);
    ErrorCode updateUserEmail(int Id,String newEmail);




}
