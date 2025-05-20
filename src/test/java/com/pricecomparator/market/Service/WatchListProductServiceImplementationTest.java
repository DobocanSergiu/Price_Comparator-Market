package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.WatchListProduct.CreateWatchListProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.*;
import com.pricecomparator.market.Repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WatchListProductServiceImplementationTest {

    @InjectMocks
    private WatchListProductServiceImplementation service;

    @Mock
    private WatchListProductRepository watchListProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WatchListRepository watchListRepository;

    @Mock
    private ProductPriceHistoryRepository productPriceHistoryRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void teardown() throws Exception {
        closeable.close();
    }

    @Test
    void addWatchListProduct_UserOrProductNotFound() {
        CreateWatchListProductRequest req = new CreateWatchListProductRequest();
        req.setUserId(1);
        req.setProductId(2);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.addWatchListProduct(req);
        assertEquals(404, result.getCode());
        assertEquals("Product or User not found", result.getMessage());
    }

    @Test
    void addWatchListProduct_NoWatchList() {
        CreateWatchListProductRequest req = new CreateWatchListProductRequest();
        req.setUserId(1);
        req.setProductId(2);

        User user = new User();
        Product product = new Product();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(2)).thenReturn(Optional.of(product));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode result = service.addWatchListProduct(req);
        assertEquals(500, result.getCode());
        assertEquals("Error, user does not have an initialized watch list", result.getMessage());
    }

    @Test
    void addWatchListProduct_AlreadyExistsInWatchList() {
        CreateWatchListProductRequest req = new CreateWatchListProductRequest();
        req.setUserId(1);
        req.setProductId(2);

        User user = new User();
        Product product = new Product();
        WatchList watchList = new WatchList();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(2)).thenReturn(Optional.of(product));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.of(watchList));
        when(watchListProductRepository.existsByProductidAndWatchlistid(product, watchList)).thenReturn(true);

        HttpCode result = service.addWatchListProduct(req);
        assertEquals(409, result.getCode());
        assertEquals("Product already in watchlist", result.getMessage());
    }

    @Test
    void addWatchListProduct_Success() {
        CreateWatchListProductRequest req = new CreateWatchListProductRequest();
        req.setUserId(1);
        req.setProductId(2);
        req.setTargetPrice("12.99");

        User user = new User();
        Product product = new Product();
        WatchList watchList = new WatchList();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(2)).thenReturn(Optional.of(product));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.of(watchList));
        when(watchListProductRepository.existsByProductidAndWatchlistid(product, watchList)).thenReturn(false);

        HttpCode result = service.addWatchListProduct(req);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        verify(watchListProductRepository).save(any(WatchListProduct.class));
    }

    @Test
    void removeWatchListProduct_NotFound() {
        when(watchListProductRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.removeWatchListProduct(1);
        assertEquals(404, result.getCode());
        assertEquals("Product not found", result.getMessage());
    }

    @Test
    void removeWatchListProduct_Success() {
        when(watchListProductRepository.findById(1)).thenReturn(Optional.of(new WatchListProduct()));

        HttpCode result = service.removeWatchListProduct(1);
        assertEquals(200, result.getCode());
        assertEquals("Product removed Successfully", result.getMessage());
        verify(watchListProductRepository).deleteById(1);
    }

    @Test
    void clearProductFromUserWatchList_UserOrProductNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.clearProductFromUserWatchList(2, 1);
        assertEquals(404, result.getCode());
    }

    @Test
    void clearProductFromUserWatchList_NoWatchList() {
        User user = new User();
        Product product = new Product();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(2)).thenReturn(Optional.of(product));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode result = service.clearProductFromUserWatchList(2, 1);
        assertEquals(500, result.getCode());
    }

    @Test
    void clearProductFromUserWatchList_Success() {
        User user = new User();
        Product product = new Product();
        WatchList watchList = new WatchList();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(2)).thenReturn(Optional.of(product));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.of(watchList));

        HttpCode result = service.clearProductFromUserWatchList(2, 1);
        assertEquals(200, result.getCode());
        verify(watchListProductRepository).removeAllByProductidAndWatchlistid(product, watchList);
    }

    @Test
    void clearUserWatchList_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode result = service.clearUserWatchList(1);
        assertEquals(404, result.getCode());
    }

    @Test
    void clearUserWatchList_NoWatchList() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode result = service.clearUserWatchList(1);
        assertEquals(500, result.getCode());
    }

    @Test
    void clearUserWatchList_Success() {
        User user = new User();
        WatchList watchList = new WatchList();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.of(watchList));

        HttpCode result = service.clearUserWatchList(1);
        assertEquals(200, result.getCode());
        verify(watchListProductRepository).deleteAllByWatchlistid(watchList);
    }

    @Test
    void getUserWatchList_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        List<?> result = service.getUserWatchList(1);
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserWatchList_NoWatchList() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUserid(user)).thenReturn(Optional.empty());

        List<?> result = service.getUserWatchList(1);
        assertTrue(result.isEmpty());
    }


}
