package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Repository.UserRepository;
import com.pricecomparator.market.Repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.pricecomparator.market.Service.Utilities.Security.generateRandomSalt;
import static com.pricecomparator.market.Service.Utilities.Security.generateSaltedPassword;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final WatchListRepository watchListRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, WatchListRepository watchListRepository)
    {
        this.userRepository = userRepository;
        this.watchListRepository = watchListRepository;
    }
    @Override
    public Optional<User> getUserById(int Id) {
        return userRepository.findById(Id);
    }

    @Override
    public HttpCode removeUserById(int Id)
    {
        /// User must exist for it to be removed
        if(userRepository.existsById(Id)==true)
        {
            userRepository.deleteById(Id);
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(200);
            errorCode.setMessage("User deleted successfully");
            return errorCode;
        }
        else {
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(400);
            errorCode.setMessage("User not found or request is invalid");
            return errorCode;

        }
    }

    @Override
    public HttpCode addUser(CreateUserRequest user) {
        ///  New users cannot use already existing usernames and emails
        if(userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail()))
        {
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(409);
            errorCode.setMessage("User or Email already exists");
            return errorCode;
        }
        else {
            /// Creating user
            String passwordSalt = generateRandomSalt(8);
            String hashedPassword = generateSaltedPassword(user.getPassword(), passwordSalt);
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setPasswordsalt(passwordSalt);
            newUser.setPasswordhash(hashedPassword);
            userRepository.save(newUser);

            /// Creating user watchlist
            WatchList watchList = new WatchList();
            watchList.setUserid(newUser);
            watchListRepository.save(watchList);

            HttpCode errorCode = new HttpCode();
            errorCode.setCode(200);
            errorCode.setMessage("User added successfully");
            return errorCode;
        }
    }

    @Override
    public HttpCode updateUserPassword(int Id,String newPassword) {
        /// User must exist
        if(userRepository.existsById(Id)==true)
        {
            String newSalt = generateRandomSalt(8);
            String hashedPassword = generateSaltedPassword(newPassword,newSalt);
            User user = userRepository.findById(Id).get();
            user.setPasswordhash(hashedPassword);
            user.setPasswordsalt(newSalt);
            userRepository.save(user);
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(200);
            errorCode.setMessage("User updated successfully");
            return errorCode;
        }
        else {
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(400);
            errorCode.setMessage("User updated failed, request is invalid or user wasn't found");
            return errorCode;

        }
    }

    @Override
    public HttpCode updateUserEmail(int Id,String newEmail) {
        /// If email already exists, the email cannot be used by another user
        if(userRepository.existsByEmail(newEmail)) {
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(409);
            errorCode.setMessage("Email is already in use");
           return errorCode;
        }
        /// Checks to see if format of email string is valid
        else if(newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")==false)
        {
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(400);
            errorCode.setMessage("Invalid email format");
            return errorCode;
        }
        /// Checks to see if user id exists
        else if(userRepository.findById(Id).isEmpty()){
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(404);
            errorCode.setMessage("User not found or request is invalid");
            return errorCode;
        }
        else
        {
            User user = userRepository.findById(Id).get();
            user.setEmail(newEmail);
            userRepository.save(user);
            HttpCode errorCode = new HttpCode();
            errorCode.setCode(200);
            errorCode.setMessage("User updated successfully");
            return errorCode;
        }
    }
}
