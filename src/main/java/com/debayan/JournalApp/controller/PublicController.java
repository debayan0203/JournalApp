package com.debayan.JournalApp.controller;

import com.debayan.JournalApp.entity.User;
import com.debayan.JournalApp.service.RedisService;
import com.debayan.JournalApp.service.UserDetailsServiceImpl;
import com.debayan.JournalApp.service.UserService;
import com.debayan.JournalApp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @GetMapping("/health-check")
    public String healthcheck(){
        return "Ok";
    }
    @PostMapping("/signup")
    public void signup(@RequestBody User user){
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
           UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
           String jwt = jwtUtil.generateToken(userDetails.getUsername());
           return new ResponseEntity<>(jwt, HttpStatus.OK);
       }
       catch(Exception e){
           log.error("Exception occured while logging in: ", e);
           return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
       }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            // We blacklist the token for 1800 seconds (30 minutes) to match our token's maximum lifespan!
            redisService.blacklistToken(jwt, 1800L);
            return new ResponseEntity<>("Logged out successfully. Token has been revoked!", HttpStatus.OK);
        }

        return new ResponseEntity<>("No valid Bearer token found in request header", HttpStatus.BAD_REQUEST);
    }
}
