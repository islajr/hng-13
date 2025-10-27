package org.project.currencyexchangeapi.dto.response.thirdparty.country;

import org.project.currencyexchangeapi.entity.Country;
import org.project.currencyexchangeapi.util.CountryUtil;

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
    public static Country generateCountry(CountryResponse response) {
        return Country.builder()
                .capital(response.capital())
                .name(response.name())
                .region(response.region())
                .population(response.population())
                .currencyCode(sortCurrency(response.currencies()))
                .flagURL(response.flag())
                .build();
    }

    public static String sortCurrency(List<Map<String, String>> currencies) {
        return CountryUtil.sortCurrency(currencies);
    }
}
