package com.example.foodmap.config;

import com.example.foodmap.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 取得 Authorization Header
        String authHeader = request.getHeader("Authorization");

        // 2. 檢查 Header 是否存在且以 "Bearer " 開頭
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取 Token (去掉前面的 "Bearer " 七個字元)
        String token = authHeader.substring(7);

        try {
            // 4. 解析 Token 取得 User ID
            Long userId = jwtUtil.parseToken(token);

            // 5. 如果解析成功，且目前 SecurityContext 裡面還沒登入
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 這裡我們直接信任 Token 裡的 ID 建立認證物件
                // (實際專案中，這裡通常會再用 userId 去資料庫查 UserDetails 來確認權限)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // 使用者身分 (Principal)
                        null,   // 密碼 (Credentials，已登入故設為 null)
                        new ArrayList<>() // 權限角色 (Authorities)
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. 將認證資訊放入 SecurityContext，代表「登入成功」
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Token 過期或無效，這裡僅記錄錯誤，不拋出例外，讓請求繼續走下去(會被後面的權限驗證擋下)
            logger.error("無法設定使用者認證: {}", e);
        }

        // 繼續執行下一個過濾器
        filterChain.doFilter(request, response);
    }
}
