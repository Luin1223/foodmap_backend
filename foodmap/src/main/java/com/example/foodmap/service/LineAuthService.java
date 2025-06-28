package com.example.foodmap.service;

import com.example.foodmap.dto.LineUserProfile;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class LineAuthService {

    //注入LINE應用程式的設定參數
    @Value("${line.login.channel-id}")
    private String clientId;

    @Value("${line.login.channel-secret}")
    private String clientSecret;

    //LINE回傳授權碼後導回你的前端網址
    @Value("${line.login.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public LineUserProfile getUserProfileFromCode(String code) {
        try {
            System.out.println("收到 code: " + code); // Debug log

            // Step 1: 交換 code 拿 access token
            //LINE拿token的API
            String tokenUrl = "https://api.line.me/oauth2/v2.1/token";

            //設定HTTP標頭格式，因為是用x-www-form-urlencoded
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            //這些參數是LINE規定要傳的，組成交換token的表單
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("code", code);
            params.add("redirect_uri", redirectUri);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);

            //送出POST請求換取access_token，LINE會回傳一個JSON
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(tokenUrl, request, JsonNode.class);

            System.out.println("access_token 回應: " + response.getBody());

            //從JSON中取出access token
            String accessToken = response.getBody().get("access_token").asText();

            // Step 2: 使用 access token 拿使用者資料
            //設定Bearer token身分驗證
            HttpHeaders profileHeaders = new HttpHeaders();
            profileHeaders.setBearerAuth(accessToken);

            //呼叫LINE的 取得使用者資訊 API，會回傳displayName、pictureUrl、userId等
            HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);
            ResponseEntity<JsonNode> profileResponse = restTemplate.exchange(
                    "https://api.line.me/v2/profile",
                    HttpMethod.GET,
                    profileRequest,
                    JsonNode.class
            );

            JsonNode profile = profileResponse.getBody();
            System.out.println("LINE 回傳使用者資料: " + profile);

            //建立自定義LineUserProfile DTO，準備給Controller使用
            LineUserProfile userProfile = new LineUserProfile();
            userProfile.setUserId(profile.get("userId").asText());
            userProfile.setName(profile.get("displayName").asText());
            userProfile.setPictureUrl(profile.get("pictureUrl").asText());
            userProfile.setEmail(null); // 預設為 null
            System.out.println("LINE token response: " + response.getBody());
            return userProfile;
        } catch (Exception e) {
            System.err.println("LINE 登入錯誤：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("LINE 登入失敗");
        }
    }

}