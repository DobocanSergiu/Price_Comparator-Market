package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> getUserByUsername(String username);

    Optional<User> findUserByUsername(String username);
}