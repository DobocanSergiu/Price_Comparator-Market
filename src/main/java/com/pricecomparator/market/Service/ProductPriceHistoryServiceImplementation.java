package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ProductPrice.*;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.ProductPriceHistory;
import com.pricecomparator.market.Repository.ProductPriceHistoryRepository;
import com.pricecomparator.market.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ProductPriceHistoryServiceImplementation implements ProductPriceHistoryService{


    private final ProductRepository productRepository;
    private final ProductPriceHistoryRepository productPriceHistoryRepository;

    public ProductPriceHistoryServiceImplementation(ProductRepository productRepository, ProductPriceHistoryRepository productPriceHistoryRepository) {
        this.productRepository = productRepository;
        this.productPriceHistoryRepository = productPriceHistoryRepository;
    }


    ///Gets price of specific product during a specific date
    @Override
    public Optional<ProductPriceHistory> getProductPriceById(int productPriceId) {
        return productPriceHistoryRepository.findById(productPriceId);
    }

    /// Gets all prices of a specific product
    @Override
    public Optional<List<ProductPriceHistory>> getAllProductPrices(int productId) {
        Optional<Product> productWithId= productRepository.findById(productId);
        if(productWithId.isEmpty()){
            return Optional.empty();
        }
        Product foundProductWithId = productWithId.get();
        Optional<List<ProductPriceHistory>> productPriceList = productPriceHistoryRepository.getAllByProductid(foundProductWithId);
        return productPriceList;
    }

    /// Remove specific price from a product
    @Override
    public HttpCode removeProductPriceById(int productPriceId) {
        if(productPriceHistoryRepository.existsById(productPriceId)){
            productPriceHistoryRepository.deleteById(productPriceId);
            HttpCode response = new HttpCode();
            response.setCode(204);
            response.setMessage("Product price deleted");
            return response;
        }
        else
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price not found");
            return response;
        }
    }

    /// Removes all prices from a specific product
    @Override
    @Transactional
    public HttpCode clearPriceHistoryOfProductById(int productId) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            productPriceHistoryRepository.deleteAllByProductid(product); // Ensure correct method name
            HttpCode response = new HttpCode();
            response.setCode(204);
            response.setMessage("Product price cleared");
            return response;
        } else {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product id not found");
            return response;
        }
    }

    /// Adds new product price
    @Override
    public HttpCode addProductPrice(CreateProductPriceRequest request) {
        int product_id = request.getProductId();
        String currency = request.getCurrency();
        String request_date = request.getDate();
        String price  = request.getPrice();
        int percentage = request.getPercentage();

        /// Product must exist
        if(productRepository.existsById(product_id)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product id not found");
            return response;

        }
        /// Get product with specific productId
        Product product = productRepository.findById(product_id).get();
        /// Transform initial date represented as string into Instant data type
        Instant date = LocalDate.parse(request_date).atStartOfDay(ZoneId.systemDefault()).toInstant().plus(1, ChronoUnit.DAYS);


        /// entry with the same product id and price must not exist
        List<ProductPriceHistory> productsWithSpecificProductIdAndDate  = productPriceHistoryRepository.findByProductidAndDate(product,date);
        if(!productsWithSpecificProductIdAndDate.isEmpty())
        {
                HttpCode response = new HttpCode();
                response.setCode(409);
                response.setMessage("Product price at specific date already exists");
                return response;
        }

        /// After validation create and add entry
        ProductPriceHistory entry = new ProductPriceHistory();
        entry.setProductid(product);
        entry.setCurrency(currency);
        entry.setPrice(new BigDecimal(price));
        entry.setDate(date);
        entry.setPricedecreasepercentage(BigDecimal.valueOf(percentage));
        productPriceHistoryRepository.save(entry);
        HttpCode response = new HttpCode();
        response.setCode(204);
        response.setMessage("Product price added");
        return response;



    }

    /// Creates sale period meaning multiple entries for a specific product at a specific price across multiple days
    @Override
    public HttpCode addSalePeriod(CreateSaleRequest request) {

        String currency = request.getCurrency();
        String price = request.getPrice();
        int product_id = request.getProductId();
        int percentage = request.getPercentage();
        String start_date = request.getStartDate();
        String end_date = request.getEndDate();

        if(productRepository.existsById(product_id)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product id not found");
            return response;

        }

        /// Get product with specific productId
        Product product = productRepository.findById(product_id).get();
        /// Transform initial date represented as string into Instant data type
        Instant startDate = LocalDate.parse(start_date).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endDate = LocalDate.parse(end_date).atStartOfDay(ZoneId.systemDefault()).toInstant();

        Instant currentDate = startDate;
        while(!currentDate.isAfter(endDate))
        {
            /// Going through each date in range to check if it is already used
            List<ProductPriceHistory> productsWithSpecificProductIdAndDate  = productPriceHistoryRepository.findByProductidAndDate(product,currentDate);
            if(!productsWithSpecificProductIdAndDate.isEmpty())
            {
                HttpCode response = new HttpCode();
                response.setCode(409);
                response.setMessage("Product price at specific date already exists");
                return response;
            }
            currentDate= currentDate.plus(1, ChronoUnit.DAYS);

        }

        /// After validation, create all entries from range and add to database
        currentDate = startDate;
        while(!currentDate.isAfter(endDate))
        {
            ProductPriceHistory entry = new ProductPriceHistory();
            entry.setProductid(product);
            entry.setCurrency(currency);
            entry.setPrice(new BigDecimal(price));
            entry.setDate(currentDate);
            entry.setPricedecreasepercentage(BigDecimal.valueOf(percentage));
            productPriceHistoryRepository.save(entry);


            currentDate= currentDate.plus(1, ChronoUnit.DAYS);


        }
        HttpCode response = new HttpCode();
        response.setCode(204);
        response.setMessage("Product prices added");
        return response;


    }

    /// Updates currency for all prices of specific product
    @Override
    public HttpCode updateCurrency(UpdateProductPriceCurencyRequest request) {

        int product_id = request.getProductId();
        String currency = request.getCurency();

        /// Check to see if product exists
        if(productRepository.existsById(product_id)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product with given id does not exist");
            return response;
        }

        Optional<List<ProductPriceHistory>> productsWithGivenProductId =productPriceHistoryRepository.getAllByProductid(productRepository.findById(product_id).get());
        if(!productsWithGivenProductId.isPresent() || productsWithGivenProductId.isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product prices for given id not found");
            return response;
        }

        /// Update currency of all entries
        for(int i =0;i<productsWithGivenProductId.get().size();i++)
        {
            ProductPriceHistory currentProductPrice = productsWithGivenProductId.get().get(i);
            currentProductPrice.setCurrency(currency);
            productPriceHistoryRepository.save(currentProductPrice);

        }
        HttpCode response = new HttpCode();
        response.setCode(204);
        response.setMessage("Product prices updated");
        return response;
    }

    /// Updates date of a specific product at a specific price
    @Override
    public HttpCode updateDate(UpdateProductPriceDateRequest request) {

        String date = request.getDate();
        int productPriceId = request.getProductPriceId();

        /// Checks to see if specific product price exists
        if(productPriceHistoryRepository.existsById(productPriceId)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price with given id does not exist");
            return response;
        }
        ProductPriceHistory productPrice = productPriceHistoryRepository.findById(productPriceId).get();
        productPrice.setDate(LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant());
        productPriceHistoryRepository.save(productPrice);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Product price updated");
        return response;

    }

    /// Updates monetary value of a specific price; Does not update currency type (Ex: from 10 USD to 20 USD)
    @Override
    public HttpCode updatePrice(UpdateProductPriceRequest request) {
        String price = request.getPrice();
        int productPriceId = request.getProductPriceId();
        if(productPriceHistoryRepository.existsById(productPriceId)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price with given id does not exist");
            return response;
        }
        ProductPriceHistory productPrice = productPriceHistoryRepository.findById(productPriceId).get();
        productPrice.setPrice(new BigDecimal(price));
        productPriceHistoryRepository.save(productPrice);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Product price updated");
        return response;

    }

    /// Updates price decrease percentage
    @Override
    public HttpCode updateProductPriceDecreasePercentage(UpdateProductPriceDecreasePercentageRequest request) {
        int percentage = request.getPercentage();
        int productPriceId = request.getProductPriceId();
        if(productPriceHistoryRepository.existsById(productPriceId)==false)
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product price with given id does not exist");
            return response;
        }
        ProductPriceHistory productPrice = productPriceHistoryRepository.findById(productPriceId).get();
        productPrice.setPricedecreasepercentage(new BigDecimal(percentage));
        productPriceHistoryRepository.save(productPrice);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Product price updated");
        return response;

    }
}
