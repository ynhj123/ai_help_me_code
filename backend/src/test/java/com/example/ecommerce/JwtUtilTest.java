package com.example.ecommerce;

import com.example.ecommerce.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类测试
 */
@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=testSecretKeyForJWTTesting",
    "jwt.expiration=86400000"
})
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateAndValidateToken() {
        // 创建测试用户详情
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();

        // 生成token
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 验证token
        assertTrue(jwtUtil.validateToken(token));
        assertTrue(jwtUtil.validateToken(token, userDetails));

        // 获取用户名
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("testuser", username);

        System.out.println("JWT Test Passed! Generated token: " + token);
    }
}