package org.project.currencyexchangeapi.dto.response;

import lombok.Builder;
import org.project.currencyexchangeapi.entity.Country;

@Builder
public record GetCountryResponse(
        Long id,
        String name,
        String capital,
        String region,
        Long population,
        String currency_code,
        Double exchange_rate,
        Double estimated_gdp,
        String flag_url,
        String last_refreshed_at
) {
    public static GetCountryResponse generateResponse(Country country) {
        return GetCountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .capital(country.getCapital())
                .region(country.getRegion())
                .population(country.getPopulation())
                .currency_code(country.getCurrencyCode())
                .exchange_rate(country.getExchangeRate())
                .estimated_gdp(country.getEstimatedGDP())
                .flag_url(country.getFlagURL())
                .last_refreshed_at(country.getLastRefreshed().toString())
                .build();
    }
}
