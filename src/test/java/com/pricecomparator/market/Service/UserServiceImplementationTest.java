package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.User.CreateUserRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Repository.ShoppingCartRepository;
import com.pricecomparator.market.Repository.UserRepository;
import com.pricecomparator.market.Repository.WatchListRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WatchListRepository watchListRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_UserExists() {
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setEmail("email@gmail.com");
        user.setPasswordsalt("123456");
        user.setPasswordhash("passwordhash");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("username", result.get().getUsername());
        assertEquals("passwordhash", result.get().getPasswordhash());
        assertEquals("123456", result.get().getPasswordsalt());
        assertEquals("email@gmail.com", result.get().getEmail());
    }

    @Test
    void testRemoveUserById_UserExists() {
        when(userRepository.existsById(1)).thenReturn(true);

        HttpCode response = userService.removeUserById(1);

        assertEquals(200, response.getCode());
        verify(userRepository).deleteById(1);
    }

    @Test
    void testRemoveUserById_UserDoesNotExist() {
        when(userRepository.existsById(1)).thenReturn(false);

        HttpCode response = userService.removeUserById(1);

        assertEquals(400, response.getCode());
        verify(userRepository, never()).deleteById(anyInt());
    }

    @Test
    void testAddUser_UserExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername("test")).thenReturn(false);
        HttpCode response = userService.addUser(request);

        assertEquals(200, response.getCode());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void testUpdateUserPassword_UserExists() {
        User user = new User();
        user.setId(1);
        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        HttpCode response = userService.updateUserPassword(1, "newPass");

        assertEquals(200, response.getCode());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserEmail_EmailInUse() {
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        HttpCode response = userService.updateUserEmail(1, "existing@example.com");

        assertEquals(409, response.getCode());
    }

    @Test
    void testUpdateUserEmail_InvalidFormat() {
        HttpCode response = userService.updateUserEmail(1, "bad-email");

        assertEquals(400, response.getCode());
    }

    @Test
    void testUpdateUserEmail_UserNotFound() {
        when(userRepository.existsByEmail("unique@example.com")).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = userService.updateUserEmail(1, "unique@example.com");

        assertEquals(404, response.getCode());
    }

    @Test
    void testUpdateUserEmail_Success() {
        User user = new User();
        user.setId(1);
        when(userRepository.existsByEmail("unique@example.com")).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        HttpCode response = userService.updateUserEmail(1, "unique@example.com");

        assertEquals(200, response.getCode());
        assertEquals("unique@example.com", user.getEmail());
        verify(userRepository).save(user);
    }
}
