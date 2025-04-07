package com.example.foodmap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.foodmap.mapper")  // 添加這行，告訴 Spring 要掃描 Mapper 的位置
public class FoodmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodmapApplication.class, args);
	}

}
