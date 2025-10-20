package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record StringProperties(
        long length,
        boolean is_palindrome,
        int unique_characters,
        int word_count,
        String sha256_hash,
        Map<Character, Integer> character_frequency_map
) {
}
