package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.WatchListProduct.CreateWatchListProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.WatchListProduct.WatchListProductResponse;
import com.pricecomparator.market.Domain.*;
import com.pricecomparator.market.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WatchListProductServiceImplementation implements WatchListProductService {


    private final WatchListProductRepository watchListProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WatchListRepository watchListRepository;
    private final ProductPriceHistoryRepository productPriceHistoryRepository;
    public WatchListProductServiceImplementation(WatchListProductRepository watchListProductRepository, ProductRepository productRepository, UserRepository userRepository, WatchListRepository watchListRepository, ProductPriceHistoryRepository productPriceHistoryRepository) {

        this.watchListProductRepository = watchListProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.watchListRepository = watchListRepository;
        this.productPriceHistoryRepository = productPriceHistoryRepository;
    }
    @Override
    @Transactional
    public HttpCode addWatchListProduct(@RequestBody CreateWatchListProductRequest request) {
        /// User and product must exist
        if(productRepository.findById(request.getProductId()).isEmpty() || userRepository.findById(request.getUserId()).isEmpty())
         {
             HttpCode response = new HttpCode();
             response.setCode(404);
             response.setMessage("Product or User not found");
             return response;
         }

        /// User must have a watch list
        User user = userRepository.findById(request.getUserId()).get();
        if(watchListRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized watch list");
            return response;
        }

        /// You cannot have duplicates in a watch list
        Product product = productRepository.findById(request.getProductId()).get();
        WatchList watchList = watchListRepository.findByUserid(user).get();
        if(watchListProductRepository.existsByProductidAndWatchlistid(product,watchList))
        {
            HttpCode response = new HttpCode();
            response.setCode(409);
            response.setMessage("Product already in watchlist");
            return response;

        }

        /// Add product to user watch list;
        WatchListProduct watchListProduct = new WatchListProduct();
        watchListProduct.setProductid(product);
        watchListProduct.setWatchlistid(watchList);
        watchListProduct.setWantedprice(new BigDecimal(request.getTargetPrice()));
        watchListProductRepository.save(watchListProduct);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;

    }

    @Override
    @Transactional
    public HttpCode removeWatchListProduct(int watchListProductId) {
        if(watchListProductRepository.findById(watchListProductId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product not found");
            return response;
        }
        else
        {
            watchListProductRepository.deleteById(watchListProductId);
            HttpCode response = new HttpCode();
            response.setCode(200);
            response.setMessage("Product removed Successfully");
            return response;

        }
    }

    @Override
    @Transactional
    public HttpCode clearProductFromUserWatchList(int productId, int userId) {
        /// User and product must exist
        if(productRepository.findById(productId).isEmpty() || userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product or User not found");
            return response;
        }
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();
        /// Watch list must be initialized
        if(watchListRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized watch list");
            return response;
        }
        /// Add item to watch list
        WatchList watchList = watchListRepository.findByUserid(user).get();
        watchListProductRepository.removeAllByProductidAndWatchlistid(product,watchList);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;
    }

    @Override
    @Transactional
    public HttpCode clearUserWatchList(int userId) {
        /// User must exist
        if(userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("User not found");
            return response;
        }
        User user = userRepository.findById(userId).get();
        /// Watch list must be initialized
        if(watchListRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized watch list");
            return response;
        }
        /// Add item to watch list
        WatchList watchList = watchListRepository.findByUserid(user).get();
        watchListProductRepository.deleteAllByWatchlistid(watchList);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;

    }

    public BigDecimal getCurrentPriceByProductId(int productId) {
        // Fetch the product by ID
        Product wantedIdProduct = productRepository.findById(productId).orElse(null);
        if (wantedIdProduct == null) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        // Fetch all price history entries for the product
        List<ProductPriceHistory> productPricesList = productPriceHistoryRepository.getAllByProductid(wantedIdProduct).orElse(new ArrayList<>());

        if (productPricesList.isEmpty()) {
            return BigDecimal.ZERO; // or throw an exception depending on your logic
        }

        Instant today = Instant.now();
        ProductPriceHistory closestPrice = null;

        for (ProductPriceHistory priceHistory : productPricesList) {
            Instant priceDate = priceHistory.getDate();

            if (priceDate.isAfter(today)) {
                continue; // Skip future prices
            }

            // If it's exactly today, return it immediately
            if (priceDate.truncatedTo(ChronoUnit.DAYS).equals(today.truncatedTo(ChronoUnit.DAYS))) {
                return priceHistory.getPrice();
            }

            // Keep track of the most recent price before today
            if (closestPrice == null || priceDate.isAfter(closestPrice.getDate())) {
                closestPrice = priceHistory;
            }
        }

        // Return the closest price from the past if found, otherwise 0
        if (closestPrice != null) {
            return closestPrice.getPrice();
        }
        else {
            return BigDecimal.ZERO;
        }
    }

    @Transactional
    @Override
    public List<?> getUserWatchList(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<WatchList> watchListOpt = watchListRepository.findByUserid(userOpt.get());
        if (watchListOpt.isEmpty()) {
            return Collections.emptyList();
        }

        List<WatchListProduct> userProducts = watchListProductRepository.getAllByWatchlistid(watchListOpt.get());
        List<WatchListProductResponse> output = new ArrayList<>();

        for (WatchListProduct product : userProducts) {
            WatchListProductResponse response = new WatchListProductResponse();
            response.setProductId(product.getProductid().getId());
            response.setTargetPrice(product.getWantedprice());
            response.setCurrentPrice(getCurrentPriceByProductId(product.getProductid().getId()));
            Product p = product.getProductid();
            response.setName(p.getName());
            response.setBrand(p.getBrand());
            response.setCategory(p.getCategory());
            response.setQuantity(p.getQuantity());
            response.setMeasurement(p.getMeasurement());

            output.add(response);
        }

        return output;
    }

    @Transactional
    @Override
    public List<?> getUserWatchListAtTargetOrLower(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<WatchList> watchListOpt = watchListRepository.findByUserid(userOpt.get());
        if (watchListOpt.isEmpty()) {
            return Collections.emptyList();
        }

        List<WatchListProduct> userProducts = watchListProductRepository.getAllByWatchlistid(watchListOpt.get());
        List<WatchListProductResponse> output = new ArrayList<>();

        for (WatchListProduct product : userProducts) {
            WatchListProductResponse response = new WatchListProductResponse();
            response.setProductId(product.getProductid().getId());
            response.setTargetPrice(product.getWantedprice());
            response.setCurrentPrice(getCurrentPriceByProductId(product.getProductid().getId()));
            Product p = product.getProductid();
            response.setName(p.getName());
            response.setBrand(p.getBrand());
            response.setCategory(p.getCategory());
            response.setQuantity(p.getQuantity());
            response.setMeasurement(p.getMeasurement());
            if(response.getCurrentPrice().compareTo(response.getTargetPrice()) <= 0 )
            {

                output.add(response);
            }
        }

        return output;
    }


}
