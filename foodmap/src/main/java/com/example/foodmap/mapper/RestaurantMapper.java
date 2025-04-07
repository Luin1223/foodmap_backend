package com.example.foodmap.mapper;


import com.example.foodmap.entity.Restaurant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RestaurantMapper {

    @Select("SELECT * FROM restaurant")
    @Results({
            @Result(property = "open", column = "is_open")
    })
    List<Restaurant> findAll();


    @Insert("INSERT INTO restaurant(name, tag, image, is_open, price, distance, rating, latitude, longitude) " +
            "VALUES(#{name}, #{tag}, #{image}, #{open}, #{price}, #{distance}, #{rating}, #{latitude}, #{longitude})")
    void insert(Restaurant restaurant);


}
