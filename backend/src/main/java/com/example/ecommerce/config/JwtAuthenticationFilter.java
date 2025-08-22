package com.example.ecommerce.config;

import com.example.ecommerce.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // 获取Authorization头
            String authHeader = request.getHeader("Authorization");

            // 检查token
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // 验证token
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 确保当前没有认证信息
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 获取用户详情
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // 再次验证token与用户的匹配性
                        if (jwtUtil.validateToken(token, userDetails)) {
                            // 创建认证对象
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            // 设置认证信息
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            logger.debug("JWT authentication successful for user: {}", username);
                        }
                    }
                } else {
                    logger.debug("JWT token validation failed");
                }
            }
        } catch (Exception e) {
            logger.error("JWT authentication error: {}", e.getMessage());
            // 不抛出异常，让请求继续处理
        }

        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }


}