package com.debayan.JournalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Disabled
    @Test
    void testsendmail(){
        emailService.sendEmail("ronystars670@gmail.com", "Testing Java Mail", "hey, lol");
    }
}
