package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record InterpretedQuery(
        String original,
        Map<String, String> parsed_filters
) {
}
