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

    @Select("SELECT * FROM restaurant WHERE id = #{id}")
    @Results({
            @Result(property = "open", column = "is_open")
    })
    Restaurant findById(@Param("id") Integer id);

    @Insert("INSERT INTO restaurant(name, tag, image, is_open, price, distance, rating, latitude, longitude,address) " +
            "VALUES(#{name}, #{tag}, #{image}, #{open}, #{price}, #{distance}, #{rating}, #{latitude}, #{longitude},#{address})")
    void insert(Restaurant restaurant);


}
