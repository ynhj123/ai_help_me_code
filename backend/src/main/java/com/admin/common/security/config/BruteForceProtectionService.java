package com.admin.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BruteForceProtectionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN_ATTEMPTS_KEY = "login_attempts:";
    private static final String LOCKED_KEY = "locked:";
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 30;
    private static final int ATTEMPT_EXPIRATION_MINUTES = 15;

    public void loginFailed(String username, String ip) {
        String key = LOGIN_ATTEMPTS_KEY + username + ":" + ip;
        String lockKey = LOCKED_KEY + username + ":" + ip;
        
        // 检查是否已锁定
        if (isLocked(username, ip)) {
            return;
        }
        
        // 增加失败次数
        Long attempts = redisTemplate.opsForValue().increment(key);
        
        if (attempts == 1) {
            redisTemplate.expire(key, ATTEMPT_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        }
        
        // 达到最大尝试次数，锁定账户
        if (attempts >= MAX_ATTEMPTS) {
            redisTemplate.opsForValue().set(lockKey, "LOCKED", LOCK_DURATION_MINUTES, TimeUnit.MINUTES);
        }
    }

    public void loginSucceeded(String username, String ip) {
        String key = LOGIN_ATTEMPTS_KEY + username + ":" + ip;
        String lockKey = LOCKED_KEY + username + ":" + ip;
        
        redisTemplate.delete(key);
        redisTemplate.delete(lockKey);
    }

    public boolean isLocked(String username, String ip) {
        String lockKey = LOCKED_KEY + username + ":" + ip;
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }

    public int getRemainingAttempts(String username, String ip) {
        String key = LOGIN_ATTEMPTS_KEY + username + ":" + ip;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts != null ? Math.max(0, MAX_ATTEMPTS - attempts) : MAX_ATTEMPTS;
    }
}