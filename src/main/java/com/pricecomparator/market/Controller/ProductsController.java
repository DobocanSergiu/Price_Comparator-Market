package com.pricecomparator.market.Controller;

import com.pricecomparator.market.DTO.Request.Product.*;
import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.ProductPrice.ProductPriceResponse;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ProductPriceHistory;
import com.pricecomparator.market.Repository.ProductPriceHistoryRepository;
import com.pricecomparator.market.Service.ProductPriceHistoryService;
import com.pricecomparator.market.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;
    private final ProductPriceHistoryService productPriceHistoryService;


    @Autowired
    public ProductsController(ProductService productService, ProductPriceHistoryService productPriceHistoryService, ProductPriceHistoryRepository productPriceHistoryRepository) {
        this.productService = productService;
        this.productPriceHistoryService = productPriceHistoryService;
    }


    /// Product Table
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

    @PostMapping("/addProduct/")
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

    @PatchMapping("/updateProductName")
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

    @PatchMapping("/updateProductMeasurement")
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

    /// Product Price history table

    @GetMapping("/getProductPriceByPriceId/{productPriceId}")
    public ResponseEntity<?> getProductPrice(@PathVariable int productPriceId)
    {
        Optional<ProductPriceHistory> requestedProductPrice = productPriceHistoryService.getProductPriceById(productPriceId);
        if(requestedProductPrice.isPresent())
        {
            ProductPriceResponse dto = new ProductPriceResponse(requestedProductPrice.get());
            return ResponseEntity.ok(dto);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getProductPricesByProductId/{productId}")
    public ResponseEntity<?> getProductPrices(@PathVariable int productId)
    {
        Optional<List<ProductPriceHistory>> requestedProductPrices = productPriceHistoryService.getAllProductPrices(productId);
        if(requestedProductPrices.isPresent())
        {
            /// Create List of dto's
            List<ProductPriceHistory> productPrices = requestedProductPrices.get();
            List<ProductPriceResponse> productPriceResponses = new ArrayList<>();
            for(int i = 0; i < productPrices.size(); i++)
            {
                ProductPriceResponse dto = new ProductPriceResponse(productPrices.get(i));
                productPriceResponses.add(dto);

            }
            return ResponseEntity.ok(productPriceResponses);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletePriceById/{priceId}")
    public ResponseEntity<Void> deleteProductPriceById(@PathVariable int priceId)
    {

        HttpCode response = productPriceHistoryService.removeProductPriceById(priceId);
        if(response.getCode()==204)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else if (response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deletePricesByProductId/{productId}")
    public ResponseEntity<Void> deleteAllPricesByProductId(@PathVariable int productId)
    {
        HttpCode response = productPriceHistoryService.clearPriceHistoryOfProductById(productId);
        if(response.getCode()==204)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addProductPrice")
    public ResponseEntity<Void> addProductPrice(@RequestBody CreateProductPriceRequest productPrice)
    {
        HttpCode response = productPriceHistoryService.addProductPrice(productPrice);
        if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else if(response.getCode()==409)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else if(response.getCode()==204)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/addSalePeriod")
    public ResponseEntity<Void> addSalePeriod(@RequestBody CreateSaleRequest sale)
    {
        HttpCode response = productPriceHistoryService.addSalePeriod(sale);
        if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else if(response.getCode()==409)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else if(response.getCode()==204)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/updateProductCurrency")
    public ResponseEntity<Void> updateProductCurrency(@RequestBody UpdateProductPriceCurencyRequest productCurrency)
    {
        HttpCode response = productPriceHistoryService.updateCurrency(productCurrency);
        if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else if(response.getCode()==204)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/updatePriceDate")
    public ResponseEntity<Void> updatePriceDate(@RequestBody UpdateProductPriceDateRequest productDate)
    {
        HttpCode response = productPriceHistoryService.updateDate(productDate);
        if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/updatePriceValue")
    public ResponseEntity<Void> updatePriceValue(@RequestBody UpdateProductPriceRequest productPrice)
    {
        HttpCode response = productPriceHistoryService.updatePrice(productPrice);
        if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }

    }
    @PatchMapping("/updateProductPriceDecreasePercentage")
    public ResponseEntity<Void> updateProductPriceDecreasePercentage(@RequestBody UpdateProductPriceDecreasePercentageRequest priceDecreasePercentage)
    {
        HttpCode response = productPriceHistoryService.updateProductPriceDecreasePercentage(priceDecreasePercentage);
        if(response.getCode()==404)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        else if(response.getCode()==200)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllDiscounts")
    public List<?> getAllDiscounts()
    {
        return productPriceHistoryService.getAllDiscounts();
    }

    @GetMapping("/getAllPresentOrFutureDiscounts")
    public List<?> getAllPresentOrFutureDiscounts()
    {
        return productPriceHistoryService.getAllPresentOrFutureDiscounts();
    }







}
