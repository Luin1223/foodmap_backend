package com.example.foodmap.service;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.entity.User;
import com.example.foodmap.entity.UserFavoriteRestaurant;
import com.example.foodmap.mapper.RestaurantMapper;
import com.example.foodmap.repository.UserFavoriteRestaurantRepository;
import com.example.foodmap.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserFavoriteRestaurantRepository favoriteRepo;
    private final UserRepository userRepo;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public void addFavorite(Long userId, Long restaurantId){
        // 1. 檢查是否存在 (JPA)
        if(favoriteRepo.existsByUser_IdAndRestaurant_Id(userId,restaurantId)){
            return;
        }

        // 2. 檢查餐廳是否存在 (MyBatis)
        // 雖然只存 ID，但還是要確認這個 ID 是有效的餐廳
        Restaurant restaurant = restaurantMapper.findById(restaurantId);
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found");
        }

        // 3. 獲取 User Proxy (JPA)
        User user = userRepo.getReferenceById(userId);

        UserFavoriteRestaurant fav = new UserFavoriteRestaurant();
        fav.setUser(user);
        fav.setRestaurant(restaurant);

        favoriteRepo.save(fav);
    }

    @Transactional
    public void removeFavorite(Long userId, Long restaurantId){
        favoriteRepo.deleteByUser_IdAndRestaurant_Id(userId, restaurantId);
    }

    public List<Restaurant> getMyFavorite(Long userId){
        return restaurantMapper.findFavoritesByUserId(userId);
    }
}
