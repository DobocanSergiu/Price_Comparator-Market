package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;

import java.util.Optional;

public interface ProductService {

    /// Functions for Products Table
    Optional<Product> getProductById(int Id);
    HttpCode removeProductById(int Id);
    HttpCode addProduct(CreateProductRequest request);
    HttpCode updateProductName(UpdateProductNameRequest request);
    HttpCode updateProductBrand(UpdateProductBrandRequest request);
    HttpCode updateProductCategory(UpdateProductCategoryRequest request);
    HttpCode updateProductQuantity(UpdateProductQuantityRequest request);
    HttpCode updateProductMeasurement(UpdateProductMeasurementRequest request);


}
