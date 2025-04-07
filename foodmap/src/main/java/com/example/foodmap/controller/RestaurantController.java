package com.example.foodmap.controller;

import com.example.foodmap.entity.Restaurant;
import com.example.foodmap.service.RestaurantService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", originPatterns = "http://192.168.68.80:3000/") //允許來自前端請求
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService service;


    public RestaurantController(RestaurantService service){
        this.service = service;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getAll() {
        return service.getAll();
    }

}

