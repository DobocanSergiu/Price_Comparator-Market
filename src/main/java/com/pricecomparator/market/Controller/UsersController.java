package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Request.User.UpdateEmailRequest;
import com.pricecomparator.market.DTO.Request.User.UpdatePasswordRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        HttpCode repsonse = userService.removeUserById(Id);
        if(repsonse.getCode()==200)
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
        HttpCode response = userService.addUser(request);
        if(response.getCode()==201)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PatchMapping("/updateUserPassword/")
    public ResponseEntity<Void> updateUserPassword(@RequestBody UpdatePasswordRequest request)
    {
        HttpCode response = userService.updateUserPassword(request.getUserId(), request.getNewPassword());
        if(response.getCode()==200)
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
        HttpCode response = userService.updateUserEmail(request.getUserId(), request.getNewEmail());
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if(response.getCode()==400)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==409)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }
}
