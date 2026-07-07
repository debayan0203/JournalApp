package com.debayan.JournalApp.service;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @Disabled
    void testSend(){
        redisTemplate.opsForValue().set("email","dd@email.com");
        Object email = redisTemplate.opsForValue().get("email");
    }
}
