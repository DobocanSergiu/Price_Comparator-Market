package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Repository.UserRepository;
import com.pricecomparator.market.Repository.WatchListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WatchListServiceImplementationTest {

    @Mock
    private WatchListRepository watchListRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WatchListServiceImplementation watchListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWatchList_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = watchListService.addWatchList(1);

        assertEquals(404, response.getCode());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void testAddWatchList_AlreadyExists() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.of(new WatchList()));

        HttpCode response = watchListService.addWatchList(1);

        assertEquals(409, response.getCode());
        assertEquals("User already exists in watch list", response.getMessage());
    }

    @Test
    void testAddWatchList_Success() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode response = watchListService.addWatchList(1);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        verify(watchListRepository, times(1)).save(any(WatchList.class));
    }

    @Test
    void testDeleteWatchListById_NotFound() {
        when(watchListRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = watchListService.deleteWatchListById(1);

        assertEquals(404, response.getCode());
        assertEquals("WatchList not found", response.getMessage());
    }

    @Test
    void testDeleteWatchListById_Success() {
        when(watchListRepository.findById(1)).thenReturn(Optional.of(new WatchList()));

        HttpCode response = watchListService.deleteWatchListById(1);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        verify(watchListRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteWatchlistByUserId_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = watchListService.deleteWatchlistByUserId(1);

        assertEquals(404, response.getCode());
        assertEquals("WatchList user not found", response.getMessage());
    }

    @Test
    void testDeleteWatchlistByUserId_WatchListNotFound() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode response = watchListService.deleteWatchlistByUserId(1);

        assertEquals(404, response.getCode());
        assertEquals("WatchList not found", response.getMessage());
    }

    @Test
    void testDeleteWatchlistByUserId_Success() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.of(new WatchList()));

        HttpCode response = watchListService.deleteWatchlistByUserId(1);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        verify(watchListRepository, times(1)).deleteByUserid(user);
    }
}
