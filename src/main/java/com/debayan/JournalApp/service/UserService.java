package com.debayan.JournalApp.service;

import com.debayan.JournalApp.entity.JournalEntry;
import com.debayan.JournalApp.entity.User;
import com.debayan.JournalApp.repository.JournalEntryRepository;
import com.debayan.JournalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService
{
//    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public void saveNewUser(User u){
        try{
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            u.setRoles(Arrays.asList("USER"));
            userRepository.save(u);
        }
        catch(Exception e){
            log.error("Exception: ",e);
        }
    }
    public void saveAdmin(User u){
        try{
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            u.setRoles(Arrays.asList("USER","ADMIN"));
            userRepository.save(u);
        }
        catch(Exception e){
            log.error("Exception: ",e);
        }
    }
    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username){
        return userRepository.findByUserName(username);
    }
}

//controller->service->repository
