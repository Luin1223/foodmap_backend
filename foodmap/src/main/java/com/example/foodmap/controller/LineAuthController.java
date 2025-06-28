package com.example.foodmap.controller;

import com.example.foodmap.dto.LineUserProfile;
import com.example.foodmap.entity.User;
import com.example.foodmap.repository.UserRepository;
import com.example.foodmap.service.LineAuthService;
import com.example.foodmap.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") //REST API控制器，路徑開頭/api/auth
@CrossOrigin//允許來自前端跨網域請求
@RequiredArgsConstructor //自動生成建構子來注入下面final欄位
public class LineAuthController {

    private final UserRepository userRepository;
    private final LineAuthService lineAuthService;
    private final JwtUtil jwtUtil;

    /* 有加上@RequiredArgsConstructor就會自動生成包含特定參數的建構函數(constructor)
    public LineAuthController(UserRepository userRepository, LineAuthService lineAuthService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.lineAuthService = lineAuthService;
        this.jwtUtil = jwtUtil;
    }*/

    //接收前端code(使用者從賴回傳的授權碼)
    @PostMapping("/line/code")
    public ResponseEntity<?> handleLineLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code"); //從body中取出授權碼

        // 1. 從 code 取得 profile
        LineUserProfile profile = lineAuthService.getUserProfileFromCode(code);

        // 2. 查詢或建立使用者
        //根據第三方登入提供者(Line)和其ID查找使用者
        User user = userRepository.findByProviderAndProviderId("line", profile.getUserId())
                .map(existingUser -> {
                    // 資料庫有使用者，更新使用者資料
                    existingUser.setName(profile.getName());
                    existingUser.setEmail(profile.getEmail());
                    existingUser.setPicture(profile.getPictureUrl());
                    existingUser.setUpdatedAt(profile.getUpdatedAt());
                    return userRepository.save(existingUser);  // 強制存檔 → 會觸發 updated_at
                })
                .orElseGet(() -> {
                    //資料庫沒有使用者，建立新使用者
                    User newUser = new User();
                    newUser.setProvider("line");
                    newUser.setProviderId(profile.getUserId());
                    newUser.setName(profile.getName());
                    newUser.setEmail(profile.getEmail());
                    newUser.setPicture(profile.getPictureUrl());
                    return userRepository.save(newUser);
                });


        // 3. 產生 JWT token(前端會用它來驗證身分)
        String token = jwtUtil.generateToken(user.getId());

        // 4. 回傳給前端
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "picture", user.getPicture()
                )
        ));
    }
}