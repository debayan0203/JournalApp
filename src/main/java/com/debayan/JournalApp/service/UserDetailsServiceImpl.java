package com.debayan.JournalApp.service;

import com.debayan.JournalApp.entity.User;
import com.debayan.JournalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);

        if (user != null) {
            // ✅ DEFENSIVE CHECK: If roles is null in DB, fall back to an empty list safely!
            List<String> userRoles = user.getRoles() != null ? user.getRoles() : Collections.emptyList();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(userRoles.toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("Username not found: " + username);
    }
}