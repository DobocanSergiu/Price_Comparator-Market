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
import java.util.ArrayList;
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
    public HttpCode clearProductFromUserWatchList(int productID, int userId) {
        /// User and product must exist
        if(productRepository.findById(productID).isEmpty() || userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product or User not found");
            return response;
        }
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productID).get();
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
        /// User  must exist
        if(userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product or User not found");
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

    public Optional<BigDecimal> getCurrentPrice(Product product) {
        Optional<ProductPriceHistory> latestEntry = productPriceHistoryRepository.findTopByProductidOrderByDateDesc(product);
        return latestEntry.map(ProductPriceHistory::getPrice);
    }
    @Override
    @Transactional
    public List<?> getUserWatchList(int userId) {
        /// User  must exist
        if(userRepository.findById(userId).isEmpty())
        {
            return null;
        }
        User user = userRepository.findById(userId).get();
        /// Watch list must be initialized
        if(watchListRepository.findByUserid(user).isEmpty()) {
            return null;
        }
        WatchList watchList = watchListRepository.findByUserid(user).get();
        /// Formating user watch list
        List<WatchListProduct> userProducts = watchListProductRepository.getAllByWatchlistid(watchList);
        List<WatchListProductResponse> output = new ArrayList<WatchListProductResponse>();
        for(int i =0;i<userProducts.size();i++)
        {
            WatchListProduct product = userProducts.get(i);
            WatchListProductResponse productResponse = new WatchListProductResponse();
            productResponse.setProductId(product.getId());
            productResponse.setTargetPrice(product.getWantedprice());

            /// Get latest price/current price
            productResponse.setCurrentPrice(new BigDecimal("0"));
            Product detailedProduct = productRepository.findById(product.getId()).get();
            if(getCurrentPrice(detailedProduct).isPresent())
            {
                productResponse.setCurrentPrice(getCurrentPrice(detailedProduct).get());
            }

            productResponse.setName(product.getProductid().getName());
            productResponse.setBrand(product.getProductid().getBrand());
            productResponse.setCategory(product.getProductid().getCategory());
            productResponse.setQuantity(product.getProductid().getQuantity());
            productResponse.setMeasurement(product.getProductid().getMeasurement());
            output.add(productResponse);

        }
        return output;

    }
}
