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
    Restaurant findById(@Param("id") Long id);

    @Insert("INSERT INTO restaurant(name, tag, image, is_open, price, distance, rating, latitude, longitude,address) " +
            "VALUES(#{name}, #{tag}, #{image}, #{open}, #{price}, #{distance}, #{rating}, #{latitude}, #{longitude},#{address})")
    void insert(Restaurant restaurant);

    @Select("SELECT r.* FROM restaurant r " +
            "INNER JOIN user_favorite_restaurants f ON r.id = f.restaurant_id " +
            "WHERE f.user_id = #{userId}")
    @Results({
            @Result(property = "open", column = "is_open") // 這裡也需要 Result Map，否則查出來的餐廳 open 狀態會是 null
    })
    List<Restaurant> findFavoritesByUserId(@Param("userId") Long userId);


}
