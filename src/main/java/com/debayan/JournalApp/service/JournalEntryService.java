package com.debayan.JournalApp.service;

import com.debayan.JournalApp.entity.JournalEntry;
import com.debayan.JournalApp.entity.User;
import com.debayan.JournalApp.enums.Sentiment;
import com.debayan.JournalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Transactional
    public void saveEntry(JournalEntry je, String userName){
        try{
            User user= userService.findByUserName(userName);
            je.setDate(LocalDateTime.now());
            Sentiment detectedMood = sentimentAnalysisService.getSentiment(je.getContent());
            je.setSentiment(detectedMood);
            JournalEntry saved = journalEntryRepository.save(je);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        }
        catch(Exception e){
           throw new RuntimeException("An error occurred while saving..", e);
        }
    }
    public void saveEntry(JournalEntry je){
        try{

           journalEntryRepository.save(je);
        }
        catch(Exception e){
            log.error("Exception: ",e);
        }
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed=false;
        try{
            User user= userService.findByUserName(userName);
             removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }
        catch (Exception e){
            log.error("error",e);
            throw new RuntimeException("An error occurred while deleting the entry.",e);
        }
        return removed;
    }
}

//controller->service->repository
