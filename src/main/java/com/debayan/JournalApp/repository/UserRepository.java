package com.debayan.JournalApp.repository;

import com.debayan.JournalApp.entity.JournalEntry;
import com.debayan.JournalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);

    void deleteByUserName(String username);
}
