package org.project.currencyexchangeapi.dto.response.thirdparty.country;

import java.util.List;
import java.util.Map;

public record CountryResponse(
        String name,
        String capital,
        String region,
        Long population,
        List<Map<String, String>> currencies,
        String flag,
        boolean independent
) {
}
