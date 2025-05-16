package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.WatchListProduct.CreateWatchListProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.Product;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Domain.WatchListProduct;
import com.pricecomparator.market.Repository.ProductRepository;
import com.pricecomparator.market.Repository.UserRepository;
import com.pricecomparator.market.Repository.WatchListProductRepository;
import com.pricecomparator.market.Repository.WatchListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WatchListProductServiceImplementation implements WatchListProductService {


    private final WatchListProductRepository watchListProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WatchListRepository watchListRepository;
    public WatchListProductServiceImplementation(WatchListProductRepository watchListProductRepository, ProductRepository productRepository, UserRepository userRepository, WatchListRepository watchListRepository) {

        this.watchListProductRepository = watchListProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.watchListRepository = watchListRepository;
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

        /// User must have watch list
        User user = userRepository.findById(request.getUserId()).get();
        if(watchListRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized watch list");
            return response;
        }

        /// You cannnot have duplicates in a watch list
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
    public HttpCode removeWatchListProduct(int watchListProductId) {
        return null;
    }

    @Override
    public HttpCode clearProductFromUserWatchList(int productID, int userId) {
        return null;
    }

    @Override
    public HttpCode clearUserWatchList(int userId) {
        return null;
    }

    @Override
    public List<?> getUserWatchList(int userId) {
        return List.of();
    }
}
