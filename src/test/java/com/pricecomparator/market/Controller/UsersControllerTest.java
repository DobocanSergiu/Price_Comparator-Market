package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.ShoppingCartProduct.CreateShoppingCartProductRequest;
import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Request.User.UpdateEmailRequest;
import com.pricecomparator.market.DTO.Request.User.UpdatePasswordRequest;
import com.pricecomparator.market.DTO.Request.WatchListProduct.CreateWatchListProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private WatchListService watchListService;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private WatchListProductService watchListProductService;
    @Mock
    private ShoppingCartProductService shoppingCartProductService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserFound() {
        User user = new User();
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        ResponseEntity<Optional<User>> response = usersController.getUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        ResponseEntity<Optional<User>> response = usersController.getUser(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUserSuccess() {
        when(userService.removeUserById(1)).thenReturn(new HttpCode(200));
        ResponseEntity<Void> response = usersController.deleteUser(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteUserFail() {
        when(userService.removeUserById(1)).thenReturn(new HttpCode(400));
        ResponseEntity<Void> response = usersController.deleteUser(1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addUserSuccess() {
        when(userService.addUser(any())).thenReturn(new HttpCode(200));
        ResponseEntity<Void> response = usersController.addUser(new CreateUserRequest());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addUserConflict() {
        when(userService.addUser(any())).thenReturn(new HttpCode(409));
        ResponseEntity<Void> response = usersController.addUser(new CreateUserRequest());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void updateUserPasswordSuccess() {
        when(userService.updateUserPassword(1, "newpass")).thenReturn(new HttpCode(200));
        UpdatePasswordRequest req = new UpdatePasswordRequest();
        req.setUserId(1);
        req.setNewPassword("newpass");
        ResponseEntity<Void> response = usersController.updateUserPassword(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUserEmailSuccess() {
        when(userService.updateUserEmail(1, "new@mail.com")).thenReturn(new HttpCode(200));
        UpdateEmailRequest req = new UpdateEmailRequest();
        req.setUserId(1);
        req.setNewEmail("new@mail.com");
        ResponseEntity<Void> response = usersController.updateUserEmail(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addWatchListSuccess() {
        when(watchListService.addWatchList(1)).thenReturn(new HttpCode(200));
        assertEquals(HttpStatus.OK, usersController.addWatchList(1).getStatusCode());
    }

    @Test
    void deleteWatchListByIdSuccess() {
        when(watchListService.deleteWatchListById(1)).thenReturn(new HttpCode(200));
        assertEquals(HttpStatus.OK, usersController.deleteWatchListById(1).getStatusCode());
    }

    @Test
    void addShoppingCartProductSuccess() {
        when(shoppingCartProductService.addShoppingCartProduct(any())).thenReturn(new HttpCode(200));
        ResponseEntity<Void> response = usersController.addShoppingCartProduct(new CreateShoppingCartProductRequest());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserShoppingCartTest() {
        when(shoppingCartProductService.getUserShoppingCart(1)).thenReturn(Collections.emptyList());
        ResponseEntity<List<?>> response = usersController.getUserShoppingCart(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getUserWatchListTest() {
        when(watchListProductService.getUserWatchList(1)).thenReturn(Collections.emptyList());
        ResponseEntity<List<?>> response = usersController.getUserWatchList(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
