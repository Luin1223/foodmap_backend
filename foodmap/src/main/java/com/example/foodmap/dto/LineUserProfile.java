package com.example.foodmap.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LineUserProfile {
    private String userId;
    private String name;
    private String email;
    private String pictureUrl;
    private LocalDateTime updatedAt;
}