package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
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
    public boolean removeUserById(int Id)
    {
        /// User must exist for it to be removed
        if(userRepository.existsById(Id)==true)
        {
            userRepository.deleteById(Id);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addUser(CreateUserRequest user) {
        ///  New users cannot use already existing usernames and emails
        if(userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail()))
        {
            return false;
        }
        String passwordSalt = generateRandomSalt(8);
        String hashedPassword =  generateSaltedPassword(user.getPassword(), passwordSalt);
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPasswordsalt(passwordSalt);
        newUser.setPasswordhash(hashedPassword);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public boolean updateUserPassword(int Id,String newPassword) {
        /// User must exist
        if(userRepository.existsById(Id)==true)
        {
            String newSalt = generateRandomSalt(8);
            String hashedPassword = generateSaltedPassword(newPassword,newSalt);
            User user = userRepository.findById(Id).get();
            user.setPasswordhash(hashedPassword);
            user.setPasswordsalt(newSalt);
            userRepository.save(user);
            return true;
        }
        else {
            System.out.println("User not found");
            return false;
        }
    }

    @Override
    public boolean updateUserEmail(int Id,String newEmail) {
        /// If email already exists, the email cannot be used by another user
        if(userRepository.existsByEmail(newEmail)) {
           return false;
        }
        /// Checks to see if format of email string is valid
        else if(newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")==false)
        {
            return false;
        }
        /// Checks to see if user id exists
        else if(userRepository.findById(Id).isEmpty()){
             return false;
        }
        else
        {
            User user = userRepository.findById(Id).get();
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        }
    }
}
