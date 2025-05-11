package com.pricecomparator.market.Controller;

import com.pricecomparator.market.Domain.Users;
import com.pricecomparator.market.Service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pricecomparator.market.Domain.Users;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UserService userService;

    @Autowired
    public UsersController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<Optional<Users>> getUser(@PathVariable int userId)
    {

        Optional<Users> requestedUser = userService.getUserById(userId);

        if(requestedUser.isPresent())
        {
            return ResponseEntity.ok(requestedUser);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }

    }
}
