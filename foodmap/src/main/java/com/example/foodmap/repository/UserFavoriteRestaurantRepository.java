package com.example.foodmap.repository;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.entity.UserFavoriteRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFavoriteRestaurantRepository
        extends JpaRepository<UserFavoriteRestaurant, Long> {

    // ✅ 檢查是否已收藏
    boolean existsByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);

    // ✅ 取消收藏
    void deleteByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);

    // ✅ 找出使用者的收藏關聯
    List<UserFavoriteRestaurant> findByUser_Id(Long userId);

    // ✅ 直接回 Restaurant（你原本的 JPQL 是對的）
    @Query("SELECT f.restaurant FROM UserFavoriteRestaurant f WHERE f.user.id = :userId")
    List<Restaurant> findFavoriteRestaurantsByUserId(@Param("userId") Long userId);
}