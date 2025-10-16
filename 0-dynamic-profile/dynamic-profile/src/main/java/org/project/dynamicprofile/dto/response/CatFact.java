package org.project.dynamicprofile.dto.response;

import lombok.Builder;

@Builder
public record CatFact(
    String fact,
    int length
) {}
