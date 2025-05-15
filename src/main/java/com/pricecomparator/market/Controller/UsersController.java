package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Request.User.UpdateEmailRequest;
import com.pricecomparator.market.DTO.Request.User.UpdatePasswordRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Service.UserService;
import com.pricecomparator.market.Service.WatchListService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final WatchListService watchListService;

    @Autowired
    public UsersController(UserService userService, WatchListService watchListService)
    {
        this.userService = userService;
        this.watchListService = watchListService;
    }

    /// User table

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

    @PostMapping("/addUser")
    public ResponseEntity<Void> addUser(@RequestBody CreateUserRequest request)
    {
        HttpCode response = userService.addUser(request);
        /// Add heree to create user watch list
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PatchMapping("/updateUserPassword")
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


    @PatchMapping("/updateUserEmail")
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

    /// Watch List Table

    @PostMapping("/addWatchList/{userId}")
    public ResponseEntity<Void> addWatchList(@PathVariable int userId)
    {
        HttpCode response = watchListService.addWatchList(userId);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if(response.getCode()==409)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/deleteWatchListById/{watchListId}")
    public ResponseEntity<Void> deleteWatchListById(@PathVariable int watchListId)
    {
        HttpCode response = watchListService.deleteWatchListById(watchListId);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/deleteWatchListByUserId/{userId}")
    @Transactional
    public ResponseEntity<Void> deleteWatchListByUserId(@PathVariable int userId)
    {
        HttpCode response = watchListService.deleteWatchlistByUserId(userId);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }







}
