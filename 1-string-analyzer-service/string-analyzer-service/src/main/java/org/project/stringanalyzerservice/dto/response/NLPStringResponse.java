package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record NLPStringResponse(
        List<StringAnalyzerResponse> data,
        int count,
        InterpretedQuery interpreted_query
) {
}