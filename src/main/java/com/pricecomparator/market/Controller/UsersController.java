package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Request.User.UpdateEmailRequest;
import com.pricecomparator.market.DTO.Request.User.UpdatePasswordRequest;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Optional<User>> getUser(@PathVariable int userId)
    {

        Optional<User> requestedUser = userService.getUserById(userId);

        if(requestedUser.isPresent())
        {
            return ResponseEntity.ok(requestedUser);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteUser/{Id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int Id)
    {
        boolean success = userService.removeUserById(Id);
        if(success)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addUser/")
    public ResponseEntity<Void> addUser(@RequestBody CreateUserRequest request)
    {
        boolean success = userService.addUser(request);
        if(success)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }
    @PatchMapping("/updateUserPassword/")
    public ResponseEntity<Void> updateUserPassword(@RequestBody UpdatePasswordRequest request)
    {
        boolean success = userService.updateUserPassword(request.getUserId(), request.getNewPassword());
        if(success)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/updateUserEmail/")
    public ResponseEntity<Void> updateUserEmail(@RequestBody UpdateEmailRequest request)
    {
        boolean success = userService.updateUserEmail(request.getUserId(), request.getNewEmail());
        if(success)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }
}
