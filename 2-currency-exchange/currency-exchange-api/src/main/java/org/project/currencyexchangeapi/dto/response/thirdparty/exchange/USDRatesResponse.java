package org.project.currencyexchangeapi.dto.response.thirdparty.exchange;

import lombok.Builder;

import java.util.Map;

@Builder
public record USDRatesResponse(
        String result,
        String provider,
        String documentation,
        String terms_of_use,
        Long time_last_update_unix,
        String time_last_update_utc,
        Long time_next_update_unix,
        String time_next_update_utc,
        Long time_eol_unix,
        String base_code,
        Map<String, Double> rates
) {}
