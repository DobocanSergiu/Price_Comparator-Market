package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Repository.ProductRepository;
import com.pricecomparator.market.Service.ProductServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ProductServiceImplementationTest {

    private ProductRepository productRepository;
    private ProductServiceImplementation productService;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImplementation(productRepository);
    }

    @Test
    public void testAddProduct_ShouldAddSuccessfully() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Lapte");
        request.setBrand("BrandA");
        request.setCategory("Lactate");
        request.setQuantity(1);
        request.setMeasurement("L");
        request.setStore("MagazinX");

        HttpCode response = productService.addProduct(request);

        assertEquals(200, response.getCode());
        assertEquals("Product has been added", response.getMessage());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testRemoveProduct_WhenExists_ShouldRemoveSuccessfully() {
        when(productRepository.existsById(1)).thenReturn(true);

        HttpCode response = productService.removeProductById(1);

        assertEquals(204, response.getCode());
        assertTrue(response.getMessage().contains("has been removed"));
        verify(productRepository).deleteById(1);
    }

    @Test
    public void testRemoveProduct_WhenNotExists_ShouldReturnError() {
        when(productRepository.existsById(1)).thenReturn(false);

        HttpCode response = productService.removeProductById(1);

        assertEquals(400, response.getCode());
        assertEquals("Delete failed", response.getMessage());
    }

    @Test
    public void testUpdateProductName_WhenExists_ShouldUpdate() {
        Product mockProduct = new Product();
        mockProduct.setId(1);
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        UpdateProductNameRequest request = new UpdateProductNameRequest();
        request.setProductId(1);
        request.setProductName("NewName");

        HttpCode response = productService.updateProductName(request);

        assertEquals(200, response.getCode());
        assertEquals("Product name updated successfully", response.getMessage());
        assertEquals("NewName", mockProduct.getName());
    }

    @Test
    public void testUpdateProductBrand_WhenNotExists_ShouldFail() {
        UpdateProductBrandRequest request = new UpdateProductBrandRequest();
        request.setProductId(1);
        request.setBrand("NewBrand");

        when(productRepository.existsById(1)).thenReturn(false);

        HttpCode response = productService.updateProductBrand(request);

        assertEquals(400, response.getCode());
        assertEquals("Product brand update failed", response.getMessage());
    }

    @Test
    public void testUpdateProductCategory_WhenExists_ShouldUpdate() {
        Product product = new Product();
        when(productRepository.existsById(2)).thenReturn(true);
        when(productRepository.findById(2)).thenReturn(Optional.of(product));

        UpdateProductCategoryRequest request = new UpdateProductCategoryRequest();
        request.setProductId(2);
        request.setCategory("Snacks");

        HttpCode response = productService.updateProductCategory(request);

        assertEquals(200, response.getCode());
        assertEquals("Product category updated successfully", response.getMessage());
        assertEquals("Snacks", product.getCategory());
    }

    @Test
    public void testUpdateProductQuantity_ShouldUpdate() {
        Product product = new Product();
        when(productRepository.existsById(3)).thenReturn(true);
        when(productRepository.findById(3)).thenReturn(Optional.of(product));

        UpdateProductQuantityRequest request = new UpdateProductQuantityRequest();
        request.setProductId(3);
        request.setQuantity(5);

        HttpCode response = productService.updateProductQuantity(request);

        assertEquals(200, response.getCode());
        assertEquals("Product quantity updated successfully", response.getMessage());
        assertEquals(5, product.getQuantity());
    }

    @Test
    public void testUpdateProductMeasurement_ShouldUpdate() {
        Product product = new Product();
        when(productRepository.existsById(4)).thenReturn(true);
        when(productRepository.findById(4)).thenReturn(Optional.of(product));

        UpdateProductMeasurementRequest request = new UpdateProductMeasurementRequest();
        request.setProductId(4);
        request.setMeasurement("Kg");

        HttpCode response = productService.updateProductMeasurement(request);

        assertEquals(200, response.getCode());
        assertEquals("Product measurement updated successfully", response.getMessage());
        assertEquals("Kg", product.getMeasurement());
    }

    @Test
    public void testUpdateProductStore_ShouldUpdate() {
        Product product = new Product();
        when(productRepository.existsById(5)).thenReturn(true);
        when(productRepository.findById(5)).thenReturn(Optional.of(product));

        UpdateProductStoreRequest request = new UpdateProductStoreRequest();
        request.setProductId(5);
        request.setStoreName("MegaStore");

        HttpCode response = productService.updateProductStore(request);

        assertEquals(200, response.getCode());
        assertEquals("Product store updated successfully", response.getMessage());
        assertEquals("MegaStore", product.getStore());
    }
}