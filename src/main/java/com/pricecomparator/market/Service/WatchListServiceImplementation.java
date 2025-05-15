package com.pricecomparator.market.Service;

import com.pricecomparator.market.DTO.Response.HttpCode;
import com.pricecomparator.market.Domain.User;
import com.pricecomparator.market.Domain.WatchList;
import com.pricecomparator.market.Repository.UserRepository;
import com.pricecomparator.market.Repository.WatchListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImplementation implements WatchListService {

    private final WatchListRepository watchListRepository;
    private final UserRepository userRepository;
    public WatchListServiceImplementation(WatchListRepository watchListRepository, UserRepository userRepository) {
        this.watchListRepository = watchListRepository;
        this.userRepository = userRepository;

    }


    /// Add watch list for given user; A user can only own 1 watch list
    @Override
    public HttpCode addWatchList(int UserId) {

        if(userRepository.findById(UserId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("User not found");
            return response;

        }
        User user = userRepository.findById(UserId).get();
        if(watchListRepository.findByUserid(user).isPresent())
        {
            HttpCode response = new HttpCode();
            response.setCode(409);
            response.setMessage("User already exists in watch list");
            return response;
        }
        WatchList watchList = new WatchList();
        watchList.setUserid(user);
        watchListRepository.save(watchList);

        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;
    }

    /// Delete watch list by watch list id
    @Override
    public HttpCode deleteWatchListById(int watchListId) {

        if(watchListRepository.findById(watchListId).isPresent())
        {
            watchListRepository.deleteById(watchListId);
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

    ///  Delete watch list by user id
    @Override
    @Transactional
    public HttpCode deleteWatchlistByUserId(int userId) {
        if(userRepository.findById(userId).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("WatchList user not found");
            return response;

        }
        User user = userRepository.findById(userId).get();
        if(watchListRepository.findByUserid(user).isEmpty())
        {
            HttpCode response = new HttpCode();
            response.setCode(404);
            response.setMessage("WatchList not found");
            return response;
        }
        watchListRepository.deleteByUserid(user);
        HttpCode response = new HttpCode();
        response.setCode(200);
        response.setMessage("Success");
        return response;

    }


}
