package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Request.ShoppingCartProduct.CreateShoppingCartProductRequest;
import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.DTO.Response.ShoppingCartProduct.ShoppingCartProductResponse;
import com.pricecomparator.market.Domain.*;
import com.pricecomparator.market.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartProductServiceImplementation implements ShoppingCartProductService {

    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductPriceHistoryRepository productPriceHistoryRepository;

    public ShoppingCartProductServiceImplementation(ShoppingCartProductRepository shoppingCartProductRepository, ProductRepository productRepository, UserRepository userRepository, ShoppingCartRepository shoppingCartRepository, ProductPriceHistoryRepository productPriceHistoryRepository) {
        this.shoppingCartProductRepository = shoppingCartProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productPriceHistoryRepository = productPriceHistoryRepository;
    }

    @Override
    @Transactional
    public HttpCode addShoppingCartProduct(CreateShoppingCartProductRequest request) {

        /// User and product must exist
        if(productRepository.findById(request.getProductId()).isEmpty() || userRepository.findById(request.getUserId()).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product or User not found");
            return response;
        }

        /// User must have a shopping cart
        User user = userRepository.findById(request.getUserId()).get();
        if(shoppingCartRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized shopping cart");
            return response;
        }

        Product product = productRepository.findById(request.getProductId()).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserid(user).get();



        /// Add product to user shopping cart;
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setProductid(product);
        shoppingCartProduct.setShoppingcartid(shoppingCart);
        shoppingCartProductRepository.save(shoppingCartProduct);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;



    }

    @Override
    @Transactional
    public HttpCode removeShoppingCartProduct(int shoppingCartProductId) {
        if(shoppingCartProductRepository.findById(shoppingCartProductId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("Product not found");
            return response;
        }
        else
        {
            shoppingCartProductRepository.deleteById(shoppingCartProductId);
            HttpCode response = new HttpCode();
            response.setCode(200);
            response.setMessage("Product removed Successfully");
            return response;

        }


    }

    @Override
    @Transactional
    public HttpCode clearProductFromUserShoppingCart(int productId, int userId) {
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
        /// Shopping cart must be initialized
        if(shoppingCartRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized shopping cart");
            return response;
        }
        /// Add item to shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserid(user).get();
        shoppingCartProductRepository.removeAllByProductidAndShoppingcartid(product,shoppingCart);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;
    }



    @Override
    @Transactional
    public HttpCode clearUserShoppingCart(int userId) {
        /// User must exist
        if(userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("User not found");
            return response;
        }
        User user = userRepository.findById(userId).get();
        /// Shopping cart must be initialized
        if(shoppingCartRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(500);
            response.setMessage("Error, user does not have an initialized watch shopping cart");
            return response;
        }
        /// Add item to shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserid(user).get();
        shoppingCartProductRepository.deleteAllByShoppingcartid(shoppingCart);
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
    @Override
    @Transactional
    public List<?> getUserShoppingCart(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Collections.emptyList();  // Better than returning null
        }

        Optional<ShoppingCart> shoppingListOpt = shoppingCartRepository.findByUserid(userOpt.get());
        if (shoppingListOpt.isEmpty()) {
            return Collections.emptyList();
        }

        List<ShoppingCartProduct> userProducts = shoppingCartProductRepository.getAllByShoppingcartid(shoppingListOpt.get());
        List<ShoppingCartProductResponse> output = new ArrayList<>();

        for (ShoppingCartProduct product : userProducts) {
            ShoppingCartProductResponse response = new ShoppingCartProductResponse();
            response.setProductId(product.getProductid().getId());

            // Current price
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
}
