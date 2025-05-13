package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable int productId)
    {
        Optional<Product> requestedProduct = productService.getProductById(productId);
        if(requestedProduct.isPresent())
        {
            return ResponseEntity.ok(requestedProduct);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<Void>  deleteProduct(@PathVariable int productId)
    {
        HttpCode response = productService.removeProductById(productId);

        if(response.getCode()==204)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addProduct/{productId}")
    public ResponseEntity<Void> addProduct(@RequestBody CreateProductRequest product)
    {
        HttpCode response = productService.addProduct(product);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else if( response.getCode()==409)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/updateProductName/")
    public ResponseEntity<Void> updateProductName(@RequestBody UpdateProductNameRequest product)
    {
        HttpCode response = productService.updateProductName(product);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/updateProductBrand")
    public ResponseEntity<Void> updateProductBrand(@RequestBody UpdateProductBrandRequest product)
    {
        HttpCode response = productService.updateProductBrand(product);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @PatchMapping("/updateProductCategory")
    public ResponseEntity<Void> updateProductCategory(@RequestBody UpdateProductCategoryRequest product)
    {
        HttpCode response = productService.updateProductCategory(product);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @PatchMapping("/updateProductQuantity")
    public ResponseEntity<Void> updateProductQuantity(@RequestBody UpdateProductQuantityRequest product)
    {
        HttpCode response = productService.updateProductQuantity(product);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @PatchMapping("/updateProductMeasurment")
    public ResponseEntity<Void> updateProductBrand(@RequestBody UpdateProductMeasurementRequest product)
    {
        HttpCode response = productService.updateProductMeasurement(product);
        if(response.getCode()==200)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }




}
