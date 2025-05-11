package com.pricecomparator.market.Repository;

import com.pricecomparator.market.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}