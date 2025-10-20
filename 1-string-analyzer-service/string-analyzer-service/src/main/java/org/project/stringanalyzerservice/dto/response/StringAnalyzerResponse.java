package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

@Builder
public record StringAnalyzerResponse(
        String id,
        String value,
        StringProperties properties,
        String created_at
) {
}
