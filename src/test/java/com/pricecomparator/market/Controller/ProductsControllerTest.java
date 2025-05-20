package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.ProductPrice.ProductPriceResponse;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ProductPriceHistory;
import com.pricecomparator.market.Service.ProductPriceHistoryService;
import com.pricecomparator.market.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductsControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductPriceHistoryService productPriceHistoryService;

    @InjectMocks
    private ProductsController productsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProduct_found() {
        Product product = new Product();
        when(productService.getProductById(1)).thenReturn(Optional.of(product));
        ResponseEntity<?> response = productsController.getProduct(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetProduct_notFound() {
        when(productService.getProductById(1)).thenReturn(Optional.empty());
        ResponseEntity<?> response = productsController.getProduct(1);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProduct_success() {
        when(productService.removeProductById(1)).thenReturn(new HttpCode(204, "Deleted"));
        ResponseEntity<?> response = productsController.deleteProduct(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testAddProduct_success() {
        when(productService.addProduct(any())).thenReturn(new HttpCode(200, "Added"));
        ResponseEntity<?> response = productsController.addProduct(new CreateProductRequest());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductName() {
        when(productService.updateProductName(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updateProductName(new UpdateProductNameRequest());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductBrand() {
        when(productService.updateProductBrand(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updateProductBrand(new UpdateProductBrandRequest());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductCategory() {
        when(productService.updateProductCategory(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updateProductCategory(new UpdateProductCategoryRequest());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductQuantity() {
        when(productService.updateProductQuantity(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updateProductQuantity(new UpdateProductQuantityRequest());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductMeasurement() {
        when(productService.updateProductMeasurement(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updateProductBrand(new UpdateProductMeasurementRequest());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProductPriceById_success() {
        when(productPriceHistoryService.removeProductPriceById(1)).thenReturn(new HttpCode(204, "Deleted"));
        ResponseEntity<?> response = productsController.deleteProductPriceById(1);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteAllPricesByProductId_success() {
        when(productPriceHistoryService.clearPriceHistoryOfProductById(1)).thenReturn(new HttpCode(204, "Deleted"));
        ResponseEntity<?> response = productsController.deleteAllPricesByProductId(1);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testAddProductPrice_conflict() {
        when(productPriceHistoryService.addProductPrice(any())).thenReturn(new HttpCode(409, "Conflict"));
        ResponseEntity<?> response = productsController.addProductPrice(new CreateProductPriceRequest());
        assertEquals(409, response.getStatusCodeValue());
    }

    @Test
    void testAddSalePeriod_success() {
        when(productPriceHistoryService.addSalePeriod(any())).thenReturn(new HttpCode(204, "Success"));
        ResponseEntity<?> response = productsController.addSalePeriod(new CreateSaleRequest());
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductCurrency_success() {
        when(productPriceHistoryService.updateCurrency(any())).thenReturn(new HttpCode(204, "Updated"));
        ResponseEntity<?> response = productsController.updateProductCurrency(new UpdateProductPriceCurencyRequest());
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testUpdatePriceDate_success() {
        when(productPriceHistoryService.updateDate(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updatePriceDate(new UpdateProductPriceDateRequest());
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testUpdatePriceValue_success() {
        when(productPriceHistoryService.updatePrice(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updatePriceValue(new UpdateProductPriceRequest());
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProductPriceDecreasePercentage_success() {
        when(productPriceHistoryService.updateProductPriceDecreasePercentage(any())).thenReturn(new HttpCode(200, "Updated"));
        ResponseEntity<?> response = productsController.updateProductPriceDecreasePercentage(new UpdateProductPriceDecreasePercentageRequest());
        assertEquals(204, response.getStatusCodeValue());
    }
}
