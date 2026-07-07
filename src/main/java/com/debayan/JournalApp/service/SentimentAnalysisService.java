package com.debayan.JournalApp.service;

import com.debayan.JournalApp.enums.Sentiment;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public Sentiment getSentiment(String text) {
        // 1. Safety check: if text is empty or null, default to HAPPY so we never save null!
        if (text == null || text.trim().isEmpty()) {
            return Sentiment.HAPPY;
        }

        // Convert to lowercase for case-insensitive searching
        String lowerCaseText = text.toLowerCase();

        // 2. Check for ANXIOUS (Great for coding bugs, exam stress, and overwhelm!)
        if (lowerCaseText.contains("anxious") || lowerCaseText.contains("nervous") ||
                lowerCaseText.contains("stress") || lowerCaseText.contains("worry") ||
                lowerCaseText.contains("overwhelmed") || lowerCaseText.contains("error") ||
                lowerCaseText.contains("bug") || lowerCaseText.contains("exam")) {
            return Sentiment.ANXIOUS;
        }
        // 3. Check for ANGRY
        else if (lowerCaseText.contains("angry") || lowerCaseText.contains("mad") ||
                lowerCaseText.contains("frustrated") || lowerCaseText.contains("annoyed") ||
                lowerCaseText.contains("hate") || lowerCaseText.contains("furious")) {
            return Sentiment.ANGRY;
        }
        // 4. Check for SAD
        else if (lowerCaseText.contains("sad") || lowerCaseText.contains("bad") ||
                lowerCaseText.contains("cry") || lowerCaseText.contains("depressed") ||
                lowerCaseText.contains("lonely") || lowerCaseText.contains("unhappy")) {
            return Sentiment.SAD;
        }

        // 5. Default to HAPPY for words like "good", "great", "solved", or any unmatched text!
        return Sentiment.HAPPY;
    }
}