package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.ShoppingCart;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Repository.ShoppingCartRepository;
import com.pricecomparator.market.Repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceImplementationTest {

    @InjectMocks
    private ShoppingCartServiceImplementation service;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void addShoppingCart_UserNotFound_Returns404() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.addShoppingCart(1);

        assertEquals(404, result.getCode());
        assertEquals("User not found", result.getMessage());
    }

    @Test
    void addShoppingCart_UserAlreadyHasCart_Returns409() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.of(new ShoppingCart()));

        HttpCode result = service.addShoppingCart(1);

        assertEquals(409, result.getCode());
        assertEquals("User already exists in shopping cart", result.getMessage());
    }

    @Test
    void addShoppingCart_Success_Returns200() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode result = service.addShoppingCart(1);

        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    void deleteShoppingCartById_ShoppingCartExists_Returns200() {
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(new ShoppingCart()));

        HttpCode result = service.deleteShoppingCartById(1);

        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        verify(shoppingCartRepository).deleteById(1);
    }

    @Test
    void deleteShoppingCartById_NotFound_Returns404() {
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.deleteShoppingCartById(1);

        assertEquals(404, result.getCode());
        assertEquals("WatchList not found", result.getMessage());
    }

    @Test
    void deleteShoppingCartByUserId_UserNotFound_Returns404() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.deleteShoppingCartByUserId(1);

        assertEquals(404, result.getCode());
        assertEquals("WatchList user not found", result.getMessage());
    }

    @Test
    void deleteShoppingCartByUserId_ShoppingCartNotFound_Returns404() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode result = service.deleteShoppingCartByUserId(1);

        assertEquals(404, result.getCode());
        assertEquals("WatchList not found", result.getMessage());
    }

    @Test
    void deleteShoppingCartByUserId_Success_Returns200() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.of(new ShoppingCart()));

        HttpCode result = service.deleteShoppingCartByUserId(1);

        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        verify(shoppingCartRepository).deleteByUserid(user);
    }
}
