package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Response.ErrorCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.pricecomparator.market.Service.Utilities.Security.generateRandomSalt;
import static com.pricecomparator.market.Service.Utilities.Security.generateSaltedPassword;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    @Override
    public Optional<User> getUserById(int Id) {
        return userRepository.findById(Id);
    }

    @Override
    public ErrorCode removeUserById(int Id)
    {
        /// User must exist for it to be removed
        if(userRepository.existsById(Id)==true)
        {
            userRepository.deleteById(Id);
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(200);
            errorCode.setMessage("User deleted successfully");
            return errorCode;
        }
        else {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(400);
            errorCode.setMessage("User not found or request is invalid");
            return errorCode;

        }
    }

    @Override
    public ErrorCode addUser(CreateUserRequest user) {
        ///  New users cannot use already existing usernames and emails
        if(userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail()))
        {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(409);
            errorCode.setMessage("User or Email already exists");
            return errorCode;
        }
        String passwordSalt = generateRandomSalt(8);
        String hashedPassword =  generateSaltedPassword(user.getPassword(), passwordSalt);
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPasswordsalt(passwordSalt);
        newUser.setPasswordhash(hashedPassword);
        userRepository.save(newUser);
        ErrorCode errorCode = new ErrorCode();
        errorCode.setCode(200);
        errorCode.setMessage("User added successfully");
        return errorCode;
    }

    @Override
    public ErrorCode updateUserPassword(int Id,String newPassword) {
        /// User must exist
        if(userRepository.existsById(Id)==true)
        {
            String newSalt = generateRandomSalt(8);
            String hashedPassword = generateSaltedPassword(newPassword,newSalt);
            User user = userRepository.findById(Id).get();
            user.setPasswordhash(hashedPassword);
            user.setPasswordsalt(newSalt);
            userRepository.save(user);
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(200);
            errorCode.setMessage("User updated successfully");
            return errorCode;
        }
        else {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(400);
            errorCode.setMessage("User updated failed, request is invalid or user wasn't found");
            return errorCode;

        }
    }

    @Override
    public ErrorCode updateUserEmail(int Id,String newEmail) {
        /// If email already exists, the email cannot be used by another user
        if(userRepository.existsByEmail(newEmail)) {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(409);
            errorCode.setMessage("Email is already in use");
           return errorCode;
        }
        /// Checks to see if format of email string is valid
        else if(newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")==false)
        {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(400);
            errorCode.setMessage("Invalid email format");
            return errorCode;
        }
        /// Checks to see if user id exists
        else if(userRepository.findById(Id).isEmpty()){
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(404);
            errorCode.setMessage("User not found or request is invalid");
            return errorCode;
        }
        else
        {
            User user = userRepository.findById(Id).get();
            user.setEmail(newEmail);
            userRepository.save(user);
            ErrorCode errorCode = new ErrorCode();
            errorCode.setCode(200);
            errorCode.setMessage("User updated successfully");
            return errorCode;
        }
    }
}
