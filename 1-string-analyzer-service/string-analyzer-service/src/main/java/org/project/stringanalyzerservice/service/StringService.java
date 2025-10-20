package org.project.stringanalyzerservice.service;

import org.project.stringanalyzerservice.dto.request.StringAnalyzerRequest;
import org.project.stringanalyzerservice.dto.response.FilteredStringResponse;
import org.project.stringanalyzerservice.dto.response.NLPStringResponse;
import org.project.stringanalyzerservice.dto.response.StringAnalyzerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StringService {
    public ResponseEntity<StringAnalyzerResponse> analyzeString(StringAnalyzerRequest request) {
        return null;
    }

    public ResponseEntity<StringAnalyzerResponse> getString(String stringValue) {
        return null;
    }

    public ResponseEntity<FilteredStringResponse> getFilteredStrings(String isPalindrome, String minLength, String maxLength, String wordCount, String containsCharacter) {
        return null;
    }

    public ResponseEntity<NLPStringResponse> getNLPString(String query) {
        return null;
    }

    public ResponseEntity<?> deleteString(String stringValue) {
        return null;
    }
}
