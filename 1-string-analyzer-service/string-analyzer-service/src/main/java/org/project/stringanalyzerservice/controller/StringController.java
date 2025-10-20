package org.project.stringanalyzerservice.controller;

import lombok.RequiredArgsConstructor;
import org.project.stringanalyzerservice.dto.request.StringAnalyzerRequest;
import org.project.stringanalyzerservice.dto.response.FilteredStringResponse;
import org.project.stringanalyzerservice.dto.response.NLPStringResponse;
import org.project.stringanalyzerservice.dto.response.StringAnalyzerResponse;
import org.project.stringanalyzerservice.service.StringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StringController {

    private final StringService stringService;

    @PostMapping("/strings")
    public ResponseEntity<StringAnalyzerResponse> analyzeString(@RequestBody StringAnalyzerRequest request) {
        return stringService.analyzeString(request);
    }

    @GetMapping("/strings/{string_value}")
    public ResponseEntity<StringAnalyzerResponse> getString(@PathVariable String string_value) {
        return stringService.getString(string_value);
    }

    @GetMapping("/strings")
    public ResponseEntity<FilteredStringResponse> getFilteredStrings(
            @RequestParam(defaultValue = "false", required = false) String is_palindrome,
            @RequestParam(defaultValue = "false", required = false) String min_length,
            @RequestParam(defaultValue = "false", required = false) String max_length,
            @RequestParam(defaultValue = "false", required = false) String word_count,
            @RequestParam(defaultValue = "false", required = false) String contains_character
    ) {
        return stringService.getFilteredStrings(is_palindrome, min_length, max_length, word_count, contains_character);
    }

    @GetMapping("/strings/filter-by-natural-language")
    public ResponseEntity<NLPStringResponse> getNLPString(
            @RequestParam(defaultValue = "", required = true) String query
    ) {
        return stringService.getNLPString(query);
    }

    @DeleteMapping("/strings/{string_value}")
    public ResponseEntity<?> deleteString(@PathVariable String string_value) {
        return stringService.deleteString(string_value);
    }
}
