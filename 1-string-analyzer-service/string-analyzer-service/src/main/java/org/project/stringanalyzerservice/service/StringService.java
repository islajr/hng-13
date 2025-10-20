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

    public ResponseEntity<FilteredStringResponse> getFilteredStrings(String isPalindrome, String minLength, String maxLength, String wordCount, String containsCharacter) {
        return null;
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
}
