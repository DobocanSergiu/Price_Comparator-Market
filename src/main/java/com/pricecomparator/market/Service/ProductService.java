package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.Domain.Product;

import java.util.Optional;

public interface ProductService {

    /// Functions for Products Table
    Optional<Product> getProductById(int Id);
    boolean removeProductById(int Id);
    boolean addProduct(CreateProductRequest request);
    boolean updateProductName(UpdateProductNameRequest request);
    boolean updateProductBrand(UpdateProductBrandRequest request);
    boolean updateProductCategory(UpdateProductCategoryRequest request);
    boolean updateProductQuantity(UpdateProductQuantityRequest request);
    boolean updateProductMeasurement(UpdateProductMeasurementRequest request);


}
