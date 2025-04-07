package com.example.foodmap.service;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantMapper restaurantMapper){
        this.restaurantMapper = restaurantMapper;
    }

    public List<Restaurant> getAll(){
        return restaurantMapper.findAll();
    }

    public void addRestaurant(Restaurant r){
        restaurantMapper.insert(r);
    }
}
