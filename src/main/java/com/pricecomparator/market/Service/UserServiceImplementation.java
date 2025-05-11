package com.pricecomparator.market.Service;

import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
