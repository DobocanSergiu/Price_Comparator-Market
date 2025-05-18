package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ProductPriceHistory;
import com.pricecomparator.market.Repository.ProductPriceHistoryRepository;
import com.pricecomparator.market.Repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductPriceHistoryServiceImplementationTest {

    @InjectMocks
    private ProductPriceHistoryServiceImplementation service;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPriceHistoryRepository productPriceHistoryRepository;

    private AutoCloseable closeable;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockProduct = new Product();
        mockProduct.setId(1);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getProductPriceById_ShouldReturnProductPrice() {
        ProductPriceHistory mockPrice = new ProductPriceHistory();
        when(productPriceHistoryRepository.findById(1)).thenReturn(Optional.of(mockPrice));

        Optional<ProductPriceHistory> result = service.getProductPriceById(1);

        assertTrue(result.isPresent());
        assertEquals(mockPrice, result.get());
    }

    @Test
    void getAllProductPrices_ProductExists_ReturnsPrices() {
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        List<ProductPriceHistory> prices = List.of(new ProductPriceHistory());
        when(productPriceHistoryRepository.getAllByProductid(mockProduct)).thenReturn(Optional.of(prices));

        Optional<List<ProductPriceHistory>> result = service.getAllProductPrices(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void getAllProductPrices_ProductNotFound_ReturnsEmpty() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Optional<List<ProductPriceHistory>> result = service.getAllProductPrices(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void removeProductPriceById_Exists_Returns204() {
        when(productPriceHistoryRepository.existsById(1)).thenReturn(true);

        HttpCode response = service.removeProductPriceById(1);

        assertEquals(204, response.getCode());
        verify(productPriceHistoryRepository).deleteById(1);
    }

    @Test
    void removeProductPriceById_NotExists_Returns404() {
        when(productPriceHistoryRepository.existsById(1)).thenReturn(false);

        HttpCode response = service.removeProductPriceById(1);

        assertEquals(404, response.getCode());
    }

    @Test
    void clearPriceHistoryOfProductById_ProductExists_Returns204() {
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        HttpCode response = service.clearPriceHistoryOfProductById(1);

        assertEquals(204, response.getCode());
        verify(productPriceHistoryRepository).deleteAllByProductid(mockProduct);
    }

    @Test
    void clearPriceHistoryOfProductById_NotExists_Returns404() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        HttpCode response = service.clearPriceHistoryOfProductById(1);

        assertEquals(404, response.getCode());
    }

    @Test
    void addProductPrice_AlreadyExists_Returns409() {
        CreateProductPriceRequest request = new CreateProductPriceRequest();
        request.setProductId(1);
        request.setDate("2024-05-01");
        request.setCurrency("USD");
        request.setPrice("100");
        request.setPercentage(5);

        Instant date = LocalDate.parse("2024-05-01").atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(86400);

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(productPriceHistoryRepository.findByProductidAndDate(mockProduct, date)).thenReturn(List.of(new ProductPriceHistory()));

        HttpCode response = service.addProductPrice(request);

        assertEquals(409, response.getCode());
    }

    @Test
    void addSalePeriod_WithExistingDate_Returns409() {
        CreateSaleRequest request = new CreateSaleRequest();
        request.setProductId(1);
        request.setStartDate("2024-05-01");
        request.setEndDate("2024-05-02");
        request.setCurrency("USD");
        request.setPrice("50.0");
        request.setPercentage(10);

        Instant start = LocalDate.parse("2024-05-01").atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(86400);

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(productPriceHistoryRepository.findByProductidAndDate(eq(mockProduct), any())).thenReturn(List.of(new ProductPriceHistory()));

        HttpCode response = service.addSalePeriod(request);

        assertEquals(409, response.getCode());
    }

    @Test
    void addSalePeriod_Success_Returns204() {
        CreateSaleRequest request = new CreateSaleRequest();
        request.setProductId(1);
        request.setStartDate("2024-05-01");
        request.setEndDate("2024-05-03");
        request.setCurrency("USD");
        request.setPrice("50.0");
        request.setPercentage(10);

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(productPriceHistoryRepository.findByProductidAndDate(eq(mockProduct), any())).thenReturn(Collections.emptyList());

        HttpCode response = service.addSalePeriod(request);

        assertEquals(204, response.getCode());
        verify(productPriceHistoryRepository, times(3)).save(any(ProductPriceHistory.class));
    }

    @Test
    void updateCurrency_ProductNotFound_Returns404() {
        UpdateProductPriceCurencyRequest request = new UpdateProductPriceCurencyRequest();
        request.setProductId(1);
        request.setCurrency("EUR");

        when(productRepository.existsById(1)).thenReturn(false);

        HttpCode response = service.updateCurrency(request);

        assertEquals(404, response.getCode());
    }

    @Test
    void updateCurrency_NoPricesFound_Returns404() {
        UpdateProductPriceCurencyRequest request = new UpdateProductPriceCurencyRequest();
        request.setProductId(1);
        request.setCurrency("EUR");

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(productPriceHistoryRepository.getAllByProductid(mockProduct)).thenReturn(Optional.empty());

        HttpCode response = service.updateCurrency(request);

        assertEquals(404, response.getCode());
    }

    @Test
    void updateCurrency_Success_Returns204() {
        UpdateProductPriceCurencyRequest request = new UpdateProductPriceCurencyRequest();
        request.setProductId(1);
        request.setCurrency("EUR");

        ProductPriceHistory pph = new ProductPriceHistory();
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(productPriceHistoryRepository.getAllByProductid(mockProduct)).thenReturn(Optional.of(List.of(pph)));

        HttpCode response = service.updateCurrency(request);

        assertEquals(204, response.getCode());
        assertEquals("EUR", pph.getCurrency());
        verify(productPriceHistoryRepository).save(pph);
    }

    @Test
    void updateDate_Success() {
        UpdateProductPriceDateRequest request = new UpdateProductPriceDateRequest();
        request.setDate("2024-05-02");
        request.setProductPriceId(1);

        ProductPriceHistory pph = new ProductPriceHistory();

        when(productPriceHistoryRepository.existsById(1)).thenReturn(true);
        when(productPriceHistoryRepository.findById(1)).thenReturn(Optional.of(pph));

        HttpCode response = service.updateDate(request);

        assertEquals(200, response.getCode());
        assertEquals("Product price updated", response.getMessage());
        verify(productPriceHistoryRepository).save(pph);
    }

    @Test
    void updateDate_NotFound() {
        UpdateProductPriceDateRequest request = new UpdateProductPriceDateRequest();
        request.setProductPriceId(1);

        when(productPriceHistoryRepository.existsById(1)).thenReturn(false);

        HttpCode response = service.updateDate(request);

        assertEquals(404, response.getCode());
    }

    @Test
    void updatePrice_Success() {
        UpdateProductPriceRequest request = new UpdateProductPriceRequest();
        request.setProductPriceId(1);
        request.setPrice("25.0");

        ProductPriceHistory pph = new ProductPriceHistory();

        when(productPriceHistoryRepository.existsById(1)).thenReturn(true);
        when(productPriceHistoryRepository.findById(1)).thenReturn(Optional.of(pph));

        HttpCode response = service.updatePrice(request);

        assertEquals(200, response.getCode());
        assertEquals(new BigDecimal("25.0"), pph.getPrice());
    }

    @Test
    void updateProductPriceDecreasePercentage_Success() {
        UpdateProductPriceDecreasePercentageRequest request = new UpdateProductPriceDecreasePercentageRequest();
        request.setProductPriceId(1);
        request.setPercentage(15);

        ProductPriceHistory pph = new ProductPriceHistory();

        when(productPriceHistoryRepository.existsById(1)).thenReturn(true);
        when(productPriceHistoryRepository.findById(1)).thenReturn(Optional.of(pph));

        HttpCode response = service.updateProductPriceDecreasePercentage(request);

        assertEquals(200, response.getCode());
        assertEquals(BigDecimal.valueOf(15), pph.getPricedecreasepercentage());
    }

    @Test
    void updateProductPriceDecreasePercentage_NotFound() {
        UpdateProductPriceDecreasePercentageRequest request = new UpdateProductPriceDecreasePercentageRequest();
        request.setProductPriceId(1);

        when(productPriceHistoryRepository.existsById(1)).thenReturn(false);

        HttpCode response = service.updateProductPriceDecreasePercentage(request);

        assertEquals(404, response.getCode());
    }
}
