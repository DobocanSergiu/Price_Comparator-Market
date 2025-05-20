package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ShoppingCartProduct.CreateShoppingCartProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.ShoppingCartProduct.ShoppingCartProductResponse;
import com.pricecomparator.market.Domain.*;
import com.pricecomparator.market.Repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartProductServiceImplementationTest {

    @InjectMocks
    private ShoppingCartProductServiceImplementation service;

    @Mock
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductPriceHistoryRepository productPriceHistoryRepository;

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
    void addShoppingCartProduct_ProductOrUserNotFound_Returns404() {
        CreateShoppingCartProductRequest request = new CreateShoppingCartProductRequest();
        request.setProductId(1);
        request.setUserId(1);

        when(productRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = service.addShoppingCartProduct(request);
        assertEquals(404, response.getCode());
        assertEquals("Product or User not found", response.getMessage());
    }

    @Test
    void addShoppingCartProduct_UserHasNoShoppingCart_Returns500() {
        CreateShoppingCartProductRequest request = new CreateShoppingCartProductRequest();
        request.setProductId(1);
        request.setUserId(1);

        User user = new User();
        when(productRepository.findById(1)).thenReturn(Optional.of(new Product()));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode response = service.addShoppingCartProduct(request);
        assertEquals(500, response.getCode());
        assertEquals("Error, user does not have an initialized shopping cart", response.getMessage());
    }

    @Test
    void addShoppingCartProduct_Success_Returns200() {
        CreateShoppingCartProductRequest request = new CreateShoppingCartProductRequest();
        request.setProductId(1);
        request.setUserId(1);

        User user = new User();
        Product product = new Product();
        ShoppingCart cart = new ShoppingCart();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.of(cart));

        HttpCode response = service.addShoppingCartProduct(request);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        verify(shoppingCartProductRepository).save(any(ShoppingCartProduct.class));
    }

    @Test
    void removeShoppingCartProduct_NotFound_Returns404() {
        when(shoppingCartProductRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = service.removeShoppingCartProduct(1);
        assertEquals(404, response.getCode());
        assertEquals("Product not found", response.getMessage());
    }

    @Test
    void removeShoppingCartProduct_Found_Returns200() {
        ShoppingCartProduct scp = new ShoppingCartProduct();
        when(shoppingCartProductRepository.findById(1)).thenReturn(Optional.of(scp));

        HttpCode response = service.removeShoppingCartProduct(1);

        assertEquals(200, response.getCode());
        assertEquals("Product removed Successfully", response.getMessage());
        verify(shoppingCartProductRepository).deleteById(1);
    }

    @Test
    void clearProductFromUserShoppingCart_ProductOrUserNotFound_Returns404() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = service.clearProductFromUserShoppingCart(1, 1);
        assertEquals(404, response.getCode());
        assertEquals("Product or User not found", response.getMessage());
    }

    @Test
    void clearProductFromUserShoppingCart_NoShoppingCart_Returns500() {
        User user = new User();
        Product product = new Product();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode response = service.clearProductFromUserShoppingCart(1, 1);
        assertEquals(500, response.getCode());
        assertEquals("Error, user does not have an initialized shopping cart", response.getMessage());
    }

    @Test
    void clearProductFromUserShoppingCart_Success_Returns200() {
        User user = new User();
        Product product = new Product();
        ShoppingCart cart = new ShoppingCart();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.of(cart));

        HttpCode response = service.clearProductFromUserShoppingCart(1, 1);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        verify(shoppingCartProductRepository).removeAllByProductidAndShoppingcartid(product, cart);
    }

    @Test
    void clearUserShoppingCart_UserNotFound_Returns404() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = service.clearUserShoppingCart(1);
        assertEquals(404, response.getCode());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void clearUserShoppingCart_NoShoppingCart_Returns500() {
        User user = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.empty());

        HttpCode response = service.clearUserShoppingCart(1);

        assertEquals(500, response.getCode());
        assertEquals("Error, user does not have an initialized watch shopping cart", response.getMessage());
    }

    @Test
    void clearUserShoppingCart_Success_Returns200() {
        User user = new User();
        ShoppingCart cart = new ShoppingCart();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.of(cart));

        HttpCode response = service.clearUserShoppingCart(1);

        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        verify(shoppingCartProductRepository).deleteAllByShoppingcartid(cart);
    }

    @Test
    void getUserShoppingCart_UserNotFound_ReturnsEmptyList() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        List<?> result = service.getUserShoppingCart(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserShoppingCart_NoShoppingCart_ReturnsEmptyList() {
        User user = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserid(user)).thenReturn(Optional.empty());

        List<?> result = service.getUserShoppingCart(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
