package org.project.stringanalyzerservice.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record FilteredStringResponse(
        List<StringAnalyzerResponse> data,
        int count,
//        FiltersApplied filtersApplied
        Map<String, String> filtersApplied
) {
}
