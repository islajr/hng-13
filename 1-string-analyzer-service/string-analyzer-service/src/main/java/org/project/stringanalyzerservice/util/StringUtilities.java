package org.project.stringanalyzerservice.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StringUtilities {

    /* checks if given string is a palindrome */
    public boolean is_palindrome(String str) {
        String cleaned = str.replaceAll("\\s+", "").toLowerCase();
        int left = 0, right = cleaned.length() - 1;
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /* returns the count of words separated by whitespace in the provided string */
    public int word_count(String str) {
        if (str == null || str.trim().isEmpty())
            return 0;
        String[] words = str.trim().split("\\s+");
        return words.length;
    }

    /* returns the number of distinct characters in a provided string */
    public int distinct_character_count(String str) {
        Set<Character> uniqueChars = new HashSet<>();
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                uniqueChars.add(c);
            }
        }
        return uniqueChars.size();
    }

    /* returns the sha-256 hash of the provided string */
    public String sha256_hash(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    public Map<Character, Integer> charcter_frequency_map(String str) {
        Map<Character, Integer> freqMap = new LinkedHashMap<>();
        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c))
                continue; // skip spaces
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }
}
