package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

@Builder
public record FiltersApplied(
        boolean is_palindrome,
        int min_length,
        int max_length,
        int word_count,
        String contains_character
) {
}
