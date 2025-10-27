package org.project.currencyexchangeapi.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.currencyexchangeapi.dto.response.thirdparty.country.CountryResponse;
import org.project.currencyexchangeapi.dto.response.thirdparty.exchange.USDRatesResponse;
import org.project.currencyexchangeapi.entity.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CountryUtil {


    public List<Country> matchCurrencies(ResponseEntity<List<CountryResponse>> countryAPIResponse, ResponseEntity<USDRatesResponse> USDRatesResponse) {

        log.info("[Refresh Countries] Matching countries and USD rates");
        assert countryAPIResponse.getBody() != null;
        assert USDRatesResponse.getBody() != null;

        List<Country> countries = new ArrayList<>();

        for (CountryResponse response : countryAPIResponse.getBody()) {

            Country country = CountryResponse.generateCountry(response);

            if (country.getCurrencyCode() != null) {
                Double rate =  USDRatesResponse.getBody().rates().get(country.getCurrencyCode());

                if (rate != null && rate != 0) {
                    country.setExchangeRate(rate);
                } else {
                    country.setExchangeRate(null);
                }
            } else {
                country.setExchangeRate(null);
            }

            countries.add(country);
        }

        return countries;

    }

    public Double getUpdateValues(String currencyCode, ResponseEntity<USDRatesResponse> USDRatesResponse) {

        log.info("[Refresh Countries] Get currency updates");

        if (currencyCode == null) {
            log.error("[Refresh Countries] Currency code is null");
            return null;
        }

        assert USDRatesResponse.getBody() != null;
        Double rate = USDRatesResponse.getBody().rates().get(currencyCode);

        if (rate != null && rate != 0) {
            log.info("[Refresh Countries] Successfully obtained currency updated rate");
            return rate;
        }

        log.error("[Refresh Countries] Failed to get currency updates");
        return null;

    }

    public static String sortCurrency(List<Map<String, String>> currencies) {

        log.info("[Refresh Countries] Sorting currencies");

        if (currencies == null) {
            return null;
        }

        if (currencies.size() > 1) {
            log.info("[Refresh Countries] Found multiple currencies in response. Proceeding with first one");
            return currencies.getFirst().get("code");

        } else if (currencies.size() == 1) {
            return currencies.getFirst().get("code");

        } else {
            log.warn("[Refresh Countries] No currencies found in response");
            return null;
        }
    }

    public Double computeGDP(Country country) {
        if (country.getExchangeRate() == null || country.getCurrencyCode() == null) {
            return null;
        }

        return (country.getPopulation() * getRandom()) / country.getExchangeRate();
    }

    private int getRandom() {
        int min = 1000;
        int max = 2000;

        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
