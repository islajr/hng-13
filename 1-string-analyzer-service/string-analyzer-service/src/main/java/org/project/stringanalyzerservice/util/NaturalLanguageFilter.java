package org.project.stringanalyzerservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class NaturalLanguageFilter {

    public FilterResult parse(String query) {
        Predicate<String> predicate = x -> true;
        Map<String, String> parsed_filters = new HashMap<>();

        String lower = query.toLowerCase();

        //  Detect "palindromic"
        if (lower.contains("palindrome") || lower.contains("palindromic")) {
            predicate = predicate.and(this::isPalindrome);
            parsed_filters.put("is_palindrome", "true");
        }

        //  Detect "single word"
        if (lower.contains("single word")) {
            predicate = predicate.and(s -> !s.contains(" "));
            parsed_filters.put("word_count", "1");
        }

        //  Detect "containing the letter X"
        Matcher containsLetter = Pattern.compile("containing the letter ([a-z])").matcher(lower);
        if (containsLetter.find()) {
            char letter = containsLetter.group(1).charAt(0);
            predicate = predicate.and(s -> s.toLowerCase().indexOf(letter) != -1);
            parsed_filters.put("contains_letter", String.valueOf(letter));
        }

        //  Detect "longer than N characters"
        Matcher longerThan = Pattern.compile("longer than (\\d+) characters").matcher(lower);
        if (longerThan.find()) {
            int length = Integer.parseInt(longerThan.group(1));
            predicate = predicate.and(s -> s.length() > length);
            parsed_filters.put("min_length", String.valueOf(length));
        }

        //  Detect "contain the first vowel"
        if (lower.contains("contain the first vowel")) {
            predicate = predicate.and(this::containsFirstVowel);
            parsed_filters.put("contains_first_vowel", "true");
        }

        log.info("[NL Filter] Successfully parsed query: {}", query);
        return new FilterResult(predicate, parsed_filters);

    }

    private boolean isPalindrome(String s) {
        String cleaned = s.replaceAll("\\s+", "").toLowerCase();
        return new StringBuilder(cleaned).reverse().toString().equals(cleaned);
    }

    private boolean containsFirstVowel(String s) {
        // Find first vowel in the string (if any)
        String vowels = "aeiou";
        for (char c : s.toLowerCase().toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                char firstVowel = c;
                return s.toLowerCase().indexOf(firstVowel) != -1;
            }
        }
        return false;
    }

}
