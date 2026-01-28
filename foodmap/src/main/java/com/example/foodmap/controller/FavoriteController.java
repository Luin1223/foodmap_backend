package com.example.foodmap.controller;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public List<Restaurant> getFavorites(@AuthenticationPrincipal Long userId) {
        return favoriteService.getMyFavorite(userId);
    }

    @PostMapping("/{restaurantId}")
    public void addFavorite(@AuthenticationPrincipal Long userId, @PathVariable Long restaurantId) {
        favoriteService.addFavorite(userId, restaurantId);
    }

    @DeleteMapping("/{restaurantId}")
    public void removeFavorite(@AuthenticationPrincipal Long userId, @PathVariable Long restaurantId) {
        favoriteService.removeFavorite(userId, restaurantId);
    }
}