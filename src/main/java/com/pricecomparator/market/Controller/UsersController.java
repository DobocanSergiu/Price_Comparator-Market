package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.ShoppingCartProduct.CreateShoppingCartProductRequest;
import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Request.User.UpdateEmailRequest;
import com.pricecomparator.market.DTO.Request.User.UpdatePasswordRequest;
import com.pricecomparator.market.DTO.Request.User.ValidateLoginRequest;
import com.pricecomparator.market.DTO.Request.WatchListProduct.CreateWatchListProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Repository.UserRepository;
import com.pricecomparator.market.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final WatchListService watchListService;
    private final ShoppingCartService shoppingCartService;
    private final WatchListProductService watchListProductService;
    private final ShoppingCartProductService shoppingCartProductService;

    @Autowired
    public UsersController(UserService userService, WatchListService watchListService, ShoppingCartService shoppingCartService, WatchListProductService watchListProductService, ShoppingCartProductService shoppingCartProductService)
    {
        this.userService = userService;
        this.watchListService = watchListService;
        this.shoppingCartService = shoppingCartService;
        this.watchListProductService = watchListProductService;
        this.shoppingCartProductService = shoppingCartProductService;
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


    /// Shopping Cart Table

    @PostMapping("/addShoppingCart/{userId}")
    public ResponseEntity<Void> addShoppingCart(@PathVariable int userId)
    {
        HttpCode response = shoppingCartService.addShoppingCart(userId);
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

    @DeleteMapping("/deleteShoppingCartById/{shoppingCartId}")
    public ResponseEntity<Void> deleteShoppingCartById(@PathVariable int shoppingCartId)
    {
        HttpCode response = shoppingCartService.deleteShoppingCartById(shoppingCartId);
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

    @DeleteMapping("/deleteShoppingCartByUserId/{userId}")
    public ResponseEntity<Void> deleteShoppingCartByUserId(@PathVariable int userId)
    {
        HttpCode response = shoppingCartService.deleteShoppingCartByUserId(userId);
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


    /// WatchList Product table

    @PostMapping("/addWatchListProduct")
    public ResponseEntity<Void> addWatchListProduct(@RequestBody CreateWatchListProductRequest request)
    {
            HttpCode response=watchListProductService.addWatchListProduct(request);

            if(response.getCode()==200)
            {
                return ResponseEntity.ok().build();
            }
            else if(response.getCode()==404)
            {
                return ResponseEntity.notFound().build();
            }
            else if(response.getCode()==409)
            {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            else if(response.getCode()==500)
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            else
            {
                return ResponseEntity.badRequest().build();
            }


    }

    @DeleteMapping("deleteWatchListProduct/{watchListProductId}")
    public ResponseEntity<Void> removeWatchListProduct(@PathVariable int watchListProductId)
    {
        HttpCode response = watchListProductService.removeWatchListProduct(watchListProductId);
        if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("clearProductFromUserWatchList/{productId}/{userId}")
    public ResponseEntity<Void> clearProductFromUserWatchList(@PathVariable int productId, @PathVariable int userId)
    {
        HttpCode response = watchListProductService.clearProductFromUserWatchList(productId, userId);
        if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==500)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("clearUserWatchList/{userId}")
    public ResponseEntity<Void> clearUserWatchList(@PathVariable int userId)
    {
        HttpCode response = watchListProductService.clearUserWatchList(userId);
        if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==500)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getUserWatchList/{userId}")
    public ResponseEntity<List<?>> getUserWatchList(@PathVariable int userId) {
        List<?> response = watchListProductService.getUserWatchList(userId);
        return ResponseEntity.ok(response);
    }

    /// Shopping Cart Product Table


    @PostMapping("/addShoppingCartProduct")
    public ResponseEntity<Void> addShoppingCartProduct(@RequestBody CreateShoppingCartProductRequest request)
    {
        HttpCode response=shoppingCartProductService.addShoppingCartProduct(request);

        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==409)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else if(response.getCode()==500)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }


    }

    @DeleteMapping("deleteShoppingCartProduct/{shoppingCartProductId}")
    public ResponseEntity<Void> removeShoppingCartProduct(@PathVariable int shoppingCartProductId)
    {
        HttpCode response = shoppingCartProductService.removeShoppingCartProduct(shoppingCartProductId);
        if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("clearProductFromUserShoppingCart/{productId}/{userId}")
    public ResponseEntity<Void> clearProductFromUserShoppingCart(@PathVariable int productId, @PathVariable int userId)
    {
        HttpCode response = shoppingCartProductService.clearProductFromUserShoppingCart(productId, userId);
        if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==500)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("clearUserShoppingCart/{userId}")
    public ResponseEntity<Void> clearUserShoppingCart(@PathVariable int userId)
    {
        HttpCode response = shoppingCartProductService.clearUserShoppingCart(userId);
        if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==500)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getUserShoppingCart/{userId}")
    public ResponseEntity<List<?>> getUserShoppingCart(@PathVariable int userId) {
        List<?> response = shoppingCartProductService.getUserShoppingCart(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserWatchListAtTargetOrLower/{userId}")
    public ResponseEntity<List<?>> getUserWatchListAtTargetOrLower(@PathVariable int userId) {
        List<?> response = watchListProductService.getUserWatchListAtTargetOrLower(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validateLogin")
    public ResponseEntity<HttpCode> validateLogin(@RequestBody ValidateLoginRequest request)
    {
        HttpCode response = userService.validateLogin(request.getUsername(), request.getPassword());
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.notFound().build();
        }
        else if(response.getCode()==401)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }






}
