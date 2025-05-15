package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.ShoppingCart;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Repository.ShoppingCartRepository;
import com.pricecomparator.market.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImplementation implements ShoppingCartService{

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    public ShoppingCartServiceImplementation(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;


    }

    /// Add shopping cart for a given user; A user can only own 1 watch list
    @Override
    public HttpCode addShoppingCart(int UserId) {
        if(userRepository.findById(UserId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("User not found");
            return response;

        }
        User user = userRepository.findById(UserId).get();
        if(shoppingCartRepository.findByUserid(user).isPresent())
        {
            HttpCode response = new HttpCode();
            response.setCode(409);
            response.setMessage("User already exists in shopping cart");
            return response;
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserid(user);
        shoppingCartRepository.save(shoppingCart);

        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;

    }

    @Override
    public HttpCode deleteShoppingCartById(int shoppingCartId) {
        if(shoppingCartRepository.findById(shoppingCartId).isPresent())
        {
            shoppingCartRepository.deleteById(shoppingCartId);
            HttpCode response = new HttpCode();
            response.setCode(200);
            response.setMessage("Success");
            return response;
        };
        HttpCode response = new HttpCode();
        response.setCode(404);
        response.setMessage("WatchList not found");
        return response;
    }

    @Override
    @Transactional
    public HttpCode deleteShoppingCartByUserId(int userId) {
        if(userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("WatchList user not found");
            return response;

        }
        User user = userRepository.findById(userId).get();
        if(shoppingCartRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("WatchList not found");
            return response;
        }
        shoppingCartRepository.deleteByUserid(user);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;
    }
}
