package com.debayan.JournalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    // ENTERPRISE FIX: Inject the singleton ObjectMapper so we don't recreate it on every request!
    @Autowired
    private ObjectMapper mapper;

    // 1. GENERIC GET: Fetches JSON string from Redis and converts it to any Java Class
    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o != null) {
                // Uses the injected mapper instead of new ObjectMapper()!
                return mapper.readValue(o.toString(), entityClass);
            }
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        return null;
    }

    // 2. GENERIC SET (With TTL): Converts any Java Object to JSON string and saves to Redis
    public void set(String key, Object o, Long ttl) {
        try {
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
    }
    // ✅ METHOD 1: Checks if a JWT string currently exists in our Redis Blacklist
    public boolean isBlacklisted(String jwt) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + jwt));
        } catch (Exception e) {
            log.error("❌ Redis Blacklist check failed ", e);
            return false; // Fail-safe: if Redis is temporarily offline, allow request processing to continue
        }
    }

    // ✅ METHOD 2: Adds a JWT to the Redis Blacklist with an automated expiration TTL (in seconds)
    public void blacklistToken(String jwt, Long ttlSeconds) {
        try {
            // We store the token with a "blacklist:" prefix. Value can be simple "revoked" string!
            redisTemplate.opsForValue().set("blacklist:" + jwt, "revoked", ttlSeconds, TimeUnit.SECONDS);
            log.info("🚫 Successfully blacklisted JWT in Redis RAM for the next {} seconds", ttlSeconds);
        } catch (Exception e) {
            log.error("❌ Failed to blacklist JWT in Redis ", e);
        }
    }
}