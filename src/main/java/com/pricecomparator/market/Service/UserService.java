package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;

import java.util.Optional;

public interface UserService {

    /// Functions for Users table
    Optional<User> getUserById(int Id);
    HttpCode removeUserById(int Id);
    HttpCode addUser(CreateUserRequest user);
    HttpCode updateUserPassword(int Id,String newPassword);
    HttpCode updateUserEmail(int Id,String newEmail);


    HttpCode validateLogin(String username, String password);
}
