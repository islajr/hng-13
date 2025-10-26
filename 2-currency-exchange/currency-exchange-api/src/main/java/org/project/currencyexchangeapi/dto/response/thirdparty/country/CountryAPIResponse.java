package org.project.currencyexchangeapi.dto.response.thirdparty.country;

import lombok.Builder;
import org.project.currencyexchangeapi.entity.Country;
import org.project.currencyexchangeapi.util.CountryUtil;

import java.util.List;
import java.util.Map;

@Builder
public record CountryAPIResponse(
        List<CountryResponse> countryResponses
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
