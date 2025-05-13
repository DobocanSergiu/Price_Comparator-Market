package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.DTO.Response.ErrorCode;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImplementation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> getProductById(int Id) {
        return productRepository.findById(Id);
    }

    @Override
    public HttpCode removeProductById(int Id) {
        /// If product with given id is found, it can be deleted
        if(productRepository.existsById(Id)) {
            productRepository.deleteById(Id);
            HttpCode response = new HttpCode();
            response.setMessage("Product with id " + Id + " has been removed");
            response.setCode(204);
            return response;
        }
        else
        {
            HttpCode response = new HttpCode();
            response.setMessage("Delete failed");
            response.setCode(400);
            return response;


        }
    }

    @Override
    public boolean addProduct(CreateProductRequest request) {
        int newProductId  = request.getProductId();
        /// Duplicate id's aren't allowed
        if(productRepository.existsById(newProductId)) {
            return false;
        }
        else
        {
            Product newProduct = new Product();
            newProduct.setId(newProductId);
            newProduct.setName(request.getName());
            newProduct.setBrand(request.getBrand());
            newProduct.setCategory(request.getCategory());
            newProduct.setQuantity(request.getQuantity());
            newProduct.setMeasurement(request.getMeasurement());
            productRepository.save(newProduct);
            return true;

        }
    }

    @Override
    public boolean updateProductName(UpdateProductNameRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setName(request.getProductName());
            productRepository.save(product);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean updateProductBrand(UpdateProductBrandRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setBrand(request.getBrand());
            productRepository.save(product);
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public boolean updateProductCategory(UpdateProductCategoryRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setCategory(request.getCategoryName());
            productRepository.save(product);
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public boolean updateProductQuantity(UpdateProductQuantityRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setQuantity(request.getQuantity());
            productRepository.save(product);
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public boolean updateProductMeasurement(UpdateProductMeasurementRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setMeasurement(request.getMeasurement());
            productRepository.save(product);
            return true;
        }
        else {
            return false;
        }
    }
}
