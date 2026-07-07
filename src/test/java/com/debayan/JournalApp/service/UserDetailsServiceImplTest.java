package com.debayan.JournalApp.service;

import com.debayan.JournalApp.entity.User;
import com.debayan.JournalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

// 1. Tell JUnit to use lightweight Mockito instead of starting a Spring server
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    // 2. @InjectMocks creates the service IN MEMORY without checking application.yml
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    // 3. @Mock creates a fake dummy repository
    @Mock
    private UserRepository userRepository;

    @Disabled
    @Test
    void loadUserByUsernameTest() {
        // ARRANGE
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(User.builder().userName("ram").password("123").roles(new ArrayList<>()).build());

        // ACT
        UserDetails user = userDetailsService.loadUserByUsername("ram");

        // ASSERT
        Assertions.assertNotNull(user);
        Assertions.assertEquals("ram", user.getUsername());
    }
}