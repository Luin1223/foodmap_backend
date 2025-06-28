package com.example.foodmap.service;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    public List<Restaurant> getAll(){
        return restaurantMapper.findAll();
    }

    public Restaurant getById(int id){
        return restaurantMapper.findById(id);
    }

    public void addRestaurant(Restaurant r){
        restaurantMapper.insert(r);
    }
}
