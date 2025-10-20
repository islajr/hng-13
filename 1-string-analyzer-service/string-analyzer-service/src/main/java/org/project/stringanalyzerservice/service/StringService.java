package org.project.stringanalyzerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.stringanalyzerservice.dto.request.StringAnalyzerRequest;
import org.project.stringanalyzerservice.dto.response.FilteredStringResponse;
import org.project.stringanalyzerservice.dto.response.NLPStringResponse;
import org.project.stringanalyzerservice.dto.response.StringAnalyzerResponse;
import org.project.stringanalyzerservice.dto.response.StringProperties;
import org.project.stringanalyzerservice.util.StringUtilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class StringService {

    private final StringUtilities util;

    Map<String, StringAnalyzerResponse> strings = new ConcurrentHashMap<>();

    public ResponseEntity<StringAnalyzerResponse> analyzeString(StringAnalyzerRequest request) {

        if (request.value() == null || request.value().isEmpty() || request.value().equals(" ")) {
            log.error("[Analyze String] Error: Value does not exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if (strings.containsKey(request.value())) {
            log.error("[Analyze String] Error: String already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        log.info("[Analyze String] Generating properties for string: {}", request.value());
        StringProperties properties = getStringProperties(request.value());

        log.info("[Analyze String] Building response for string: {}", request.value());
        StringAnalyzerResponse response = StringAnalyzerResponse.builder()
                    .id(properties.sha256_hash())
                    .value(request.value())
                    .properties(properties)
                    .created_at(Instant.now().toString())
                .build();

        log.info("[Analyze String] Persisting information for string: {}", response.value());
        strings.put(response.value(), response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<StringAnalyzerResponse> getString(String stringValue) {

        if (stringValue == null || stringValue.isEmpty()) {
            log.error("[Get String] Error: Value does not exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("[Get String] Querying cache for string: {}", stringValue);
        StringAnalyzerResponse response = strings.get(stringValue);

        if (response == null) {
            log.error("[Get String] No string found for string: {}", stringValue);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<FilteredStringResponse> getFilteredStrings(Boolean isPalindrome, Integer minLength, Integer maxLength, Integer wordCount, Character containsCharacter) {

        List<StringAnalyzerResponse> filteredData = strings.values().stream()
                .filter(stringAnalyzerResponse -> isPalindrome == null || stringAnalyzerResponse.properties().is_palindrome() == isPalindrome)
                .filter(stringAnalyzerResponse -> minLength == null || stringAnalyzerResponse.properties().length() >= minLength)
                .filter(stringAnalyzerResponse -> maxLength == null || stringAnalyzerResponse.properties().length() <= maxLength)
                .filter(stringAnalyzerResponse -> wordCount == null || stringAnalyzerResponse.properties().word_count() == wordCount)
                .filter(stringAnalyzerResponse -> containsCharacter == null || stringAnalyzerResponse.value().contains(Character.toString(containsCharacter)))
                .toList();

        if (filteredData.isEmpty()) {
            log.error("[Filter Strings] Error: No data found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                FilteredStringResponse.builder()
                        .data(filteredData)
                        .count(filteredData.size())
                        .filtersApplied(getFilters(isPalindrome, minLength, maxLength, wordCount, containsCharacter))
                        .build()
        );

    }

    public ResponseEntity<NLPStringResponse> getNLPString(String query) {
        return null;
    }

    public ResponseEntity<?> deleteString(String stringValue) {
        if (!strings.containsKey(stringValue)) {
            log.error("[Delete String] Error: String: {} does not exist", stringValue);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        strings.remove(stringValue);
        log.info("[Delete String] Deleted string: {}", stringValue);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private StringProperties getStringProperties(String str) {
        return StringProperties
                .builder()
                .is_palindrome(util.is_palindrome(str))
                .sha256_hash(util.sha256_hash(str))
                .character_frequency_map(util.charcter_frequency_map(str))
                .length(str.length())
                .unique_characters(util.distinct_character_count(str))
                .word_count(util.word_count(str))
                .build();
    }

    private List<String> getFilters(Boolean isPalindrome, Integer minLength, Integer maxLength, Integer wordCount, Character containsCharacter) {
        List<String> filters = new ArrayList<>();

        if (isPalindrome != null) {
            filters.add("is_palindrome");
        }
        if (minLength != null) {
            filters.add("min_length");
        }
        if (maxLength != null) {
            filters.add("max_length");
        }
        if (wordCount != null) {
            filters.add("word_count");
        }
        if (containsCharacter != null) {
            filters.add("contains_character");
        }

        return filters;
    }
}
