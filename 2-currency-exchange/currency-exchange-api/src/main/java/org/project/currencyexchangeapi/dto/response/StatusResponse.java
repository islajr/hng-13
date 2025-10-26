package org.project.currencyexchangeapi.dto.response;

import lombok.Builder;

@Builder
public record StatusResponse(
        Long total_countries,
        String last_refreshed_at
) {}
