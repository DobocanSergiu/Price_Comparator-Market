package com.pricecomparator.market.Controller;

import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
        boolean requestedProduct = productService.removeProductById(productId);

        if(requestedProduct==true)
        {
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }



}
