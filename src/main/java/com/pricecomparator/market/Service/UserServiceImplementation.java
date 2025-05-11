package com.pricecomparator.market.Service;

import com.pricecomparator.market.Domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UsersRepository userRepository;

    @Autowired
    public UserServiceImplementation(UsersRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    @Override
    public Optional<Users> getUserById(int Id) {
        return userRepository.findById(Id);
    }
}
