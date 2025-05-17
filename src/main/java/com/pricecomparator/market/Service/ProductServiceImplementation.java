package com.pricecomparator.market.Service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.pricecomparator.market.DTO.Request.Product.*;
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
    public HttpCode addProduct(CreateProductRequest request) {


            Product newProduct = new Product();
            newProduct.setName(request.getName());
            newProduct.setBrand(request.getBrand());
            newProduct.setCategory(request.getCategory());
            newProduct.setQuantity(request.getQuantity());
            newProduct.setMeasurement(request.getMeasurement());
            newProduct.setStore(request.getStore());
            productRepository.save(newProduct);
            HttpCode response = new HttpCode();
            response.setMessage("Product has been added");
            response.setCode(200);
            return response;

    }

    @Override
    public HttpCode updateProductName(UpdateProductNameRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setName(request.getProductName());
            productRepository.save(product);
            HttpCode response = new HttpCode();
            response.setMessage("Product name updated successfully");
            response.setCode(200);
            return response;

        }
        else {
            HttpCode response = new HttpCode();
            response.setMessage("Product name update failed");
            response.setCode(400);
            return response;
        }
    }

    @Override
    public HttpCode updateProductBrand(UpdateProductBrandRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setBrand(request.getBrand());
            productRepository.save(product);
            HttpCode response = new HttpCode();
            response.setMessage("Product brand updated successfully");
            response.setCode(200);
            return response;

        }
        else {
            HttpCode response = new HttpCode();
            response.setMessage("Product brand update failed");
            response.setCode(400);
            return response;

        }

    }

    @Override
    public HttpCode updateProductCategory(UpdateProductCategoryRequest request) {

        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setCategory(request.getCategory());
            productRepository.save(product);
            HttpCode response = new HttpCode();
            response.setMessage("Product category updated successfully");
            response.setCode(200);
            return response;
        }
        else {
            HttpCode response = new HttpCode();
            response.setMessage("Product category update failed");
            response.setCode(400);
            return response;
        }

    }

    @Override
    public HttpCode updateProductQuantity(UpdateProductQuantityRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setQuantity(request.getQuantity());
            productRepository.save(product);
            HttpCode response = new HttpCode();
            response.setMessage("Product quantity updated successfully");
            response.setCode(200);
            return response;
        }
        else {
            HttpCode response = new HttpCode();
            response.setMessage("Product quantity update failed");
            response.setCode(400);
            return response;
        }

    }

    @Override
    public HttpCode updateProductMeasurement(UpdateProductMeasurementRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setMeasurement(request.getMeasurement());
            productRepository.save(product);
            HttpCode response = new HttpCode();
            response.setMessage("Product measurement updated successfully");
            response.setCode(200);
            return response;
        }
        else {
            HttpCode response = new HttpCode();
            response.setMessage("Product measurement update failed");
            response.setCode(400);
            return response;
        }
    }

    @Override
    public HttpCode updateProductStore(UpdateProductStoreRequest request) {
        int productId  = request.getProductId();
        if(productRepository.existsById(productId)) {

            Product product = productRepository.findById(productId).get();
            product.setStore(request.getStoreName());
            productRepository.save(product);
            HttpCode response = new HttpCode();
            response.setMessage("Product store updated successfully");
            response.setCode(200);
            return response;
        }
        else {
            HttpCode response = new HttpCode();
            response.setMessage("Product store update failed");
            response.setCode(400);
            return response;
        }

    }
}
