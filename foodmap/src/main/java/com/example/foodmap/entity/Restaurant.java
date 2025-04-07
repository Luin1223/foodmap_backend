package com.example.foodmap.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
public class Restaurant {

    @Id
    private Long id;
    private String name;
    private String tag;
    private String image;

    @Column(name = "is_open")
    private boolean open;
    private String price;
    private double distance;
    private double rating;
    private double latitude;
    private double longitude;
}
