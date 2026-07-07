package com.debayan.JournalApp.cache;

import com.debayan.JournalApp.entity.ConfigJournalAppEntity;
import com.debayan.JournalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        WEATHER_API;
    }


    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> APP_CACHE;

    @PostConstruct
    public void init(){
        APP_CACHE= new HashMap<>();
        List<ConfigJournalAppEntity>  all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity: all){
            if (configJournalAppEntity.getKey() != null) {
                APP_CACHE.put(configJournalAppEntity.getKey().trim(), configJournalAppEntity.getValue());
            }
        }

    }

}
