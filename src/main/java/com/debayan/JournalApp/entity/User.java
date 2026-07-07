package com.debayan.JournalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor  // ✅ CRITICAL: Lets Spring Data instantiate empty objects cleanly!
@AllArgsConstructor // ✅ Required when using @Builder alongside @NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String userName;

    private String email;
    private boolean sentimentAnalysis;

    @NonNull
    private String password;

    @Builder.Default // ✅ Tells Lombok: "If roles is missing in DB, use this empty list, DO NOT set to null!"
    private List<String> roles = new ArrayList<>();

    @DBRef
    @Builder.Default // ✅ Tells Lombok: "Keep this empty ArrayList instead of setting to null!"
    private List<JournalEntry> journalEntries = new ArrayList<>();
}