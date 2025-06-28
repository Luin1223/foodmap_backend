package com.example.foodmap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //代表這是一個設定類別
@EnableWebSecurity //啟用Spring Security的Web安全功能
public class SecurityConfig implements WebMvcConfigurer { //實作WebMvcConfigurer介面代表我要加上一些MVC設定(CORS)

    //CORS設定(解決跨網域請求)
    //這段是告訴Spring Boot我允許前端網站跨網域呼叫這些API
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允許所有 API 路徑
                .allowedOrigins("http://localhost:3000","http://192.168.68.87:3000/") // 允許的前端網址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許所有 header
                .allowCredentials(true); // 允許攜帶cookie 或 token
    }

    //安全過濾器設定(Spring Security)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //禁用CSRF(跨站請求偽造)，前後端分離不需要CSRF保護
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // LINE 登入接口開放匿名訪問
                        .requestMatchers("/api/restaurants/**").permitAll()  // 開放餐廳 API 給所有人訪問
                        .anyRequest().authenticated()  // 其他 API 需要身份認證
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 不使用 Session，JWT 或其他方式驗證
                );
        return http.build();
    }
}