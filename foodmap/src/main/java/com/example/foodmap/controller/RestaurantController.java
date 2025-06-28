package com.example.foodmap.controller;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @GetMapping("/restaurants")
    public List<Restaurant> getAll() {
        return service.getAll();
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant getById(@PathVariable int id){
        return service.getById(id);
    }


}

