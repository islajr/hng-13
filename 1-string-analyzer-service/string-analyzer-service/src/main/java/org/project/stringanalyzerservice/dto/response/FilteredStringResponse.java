package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FilteredStringResponse(
        List<StringAnalyzerResponse> data,
        int count,
        FiltersApplied filtersApplied
) {
}
