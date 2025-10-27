package org.project.currencyexchangeapi.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.currencyexchangeapi.dto.response.GetCountryResponse;
import org.project.currencyexchangeapi.dto.response.StatusResponse;
import org.project.currencyexchangeapi.dto.response.thirdparty.country.CountryResponse;
import org.project.currencyexchangeapi.dto.response.thirdparty.exchange.USDRatesResponse;
import org.project.currencyexchangeapi.entity.Country;
import org.project.currencyexchangeapi.exception.exceptions.CountryNotFoundException;
import org.project.currencyexchangeapi.repository.CountryRepository;
import org.project.currencyexchangeapi.util.CountryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService {

    private final RestTemplate restTemplate;
    private final CountryRepository countryRepository;
    private final CountryUtil countryUtil;

    @Value("${country.api.url}")
    private String countryApiURL;

    @Value("${currency.api.url}")
    private String currencyExchangeApiURL;

    private final Cache<String, String> statusCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(24))
            .build();

    @Transactional
    public ResponseEntity<?> refreshCountries() {

        log.info("[Refresh Countries] Querying third-party APIs for relevant data");
        ResponseEntity<List<CountryResponse>> countryAPIResponse = restTemplate.exchange(
                countryApiURL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        ResponseEntity<USDRatesResponse> USDRatesResponse = restTemplate.getForEntity(currencyExchangeApiURL, USDRatesResponse.class);

        if (countryAPIResponse.getStatusCode() != HttpStatus.OK || USDRatesResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("""
                    {
                        "error": "External data source unavailable",
                        "details": "Could not fetch data from [API Name]"
                    }
                    """);
        }

        log.info("[Refresh Countries] Checking for existing data");
        if (countryRepository.existsByCurrencyCode("USD")) {
            log.info("[Refresh Countries] Existing data found. Performing refresh operation");

            List<Country> countryList = countryRepository.findAll();

            for (Country country : countryList) {
                Double rate = null;

                if (country.getCurrencyCode() != null) {
                    rate = countryUtil.getUpdateValues(country.getCurrencyCode(), USDRatesResponse);
                }

                log.info("[Refresh Countries] Setting rate for currency: {}", country.getCurrencyCode());
                country.setExchangeRate(rate);

                log.info("[Refresh Countries] Re-computing GDP for country: {}", country.getName());
                country.setEstimatedGDP(countryUtil.computeGDP(country));

                log.info("[Refresh Countries] Saving data to DB");
                countryRepository.save(country);

                log.info("[Refresh Countries] Updating status cache");
                updateCache(country);

                return new ResponseEntity<>(HttpStatus.OK);
            }

        } else {
            log.info("[Refresh Countries] No data found. Creating new instances");
            List<Country> countryList = countryUtil.matchCurrencies(countryAPIResponse, USDRatesResponse);

            if (countryList.isEmpty()) {
                log.error("[Refresh Countries] New instance match Failed. Throwing exception");
                throw new RuntimeException("Failed to match new currencies");
            }

            for (Country country : countryList) {

                log.info("[Refresh Countries] Computing estimated GDP for new country: {}", country.getName());
                country.setEstimatedGDP(countryUtil.computeGDP(country));

            }

            log.info("[Refresh Countries] Persisting new data");
            countryRepository.saveAll(countryList);

            log.info("[Refresh Countries] Update status cache");
            updateCache(countryList.getLast());

            return new ResponseEntity<>(HttpStatus.CREATED);
        }


        return null;
    }

    public ResponseEntity<List<GetCountryResponse>> getCountries() {
        return null;
    }

    public ResponseEntity<GetCountryResponse> getCountry(String name) {

        log.info("[Get Country] Querying for country: {}", name);
        Country country = countryRepository.findCountryByName(name).orElseThrow(
                () -> new CountryNotFoundException("Country not found"));

        log.info("[Get Country] Retrieving data for country: {}", country.getName());
        return ResponseEntity.ok(GetCountryResponse.generateResponse(country));

    }

    public ResponseEntity<?> deleteCountry(String name) {

        log.info("[Delete Country] Querying for country: {}", name);
        Country country = countryRepository.findCountryByName(name).orElseThrow(
                () -> new CountryNotFoundException("Country not found"));

        log.info("[Delete Country] Deleting data for country: {}", country.getName());
        countryRepository.deleteCountryByName(name);

        log.info("[Delete Country] Successfully deleted data for country: {}", country.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<StatusResponse> getStatus() {
        Long count = Long.parseLong(statusCache.getIfPresent("count"));
        String lastRefreshed = statusCache.getIfPresent("last_updated");


        if (lastRefreshed != null && count != null) {
            return ResponseEntity.ok(StatusResponse.builder()
                            .total_countries(count)
                            .last_refreshed_at(lastRefreshed)
                    .build());
        }

        return ResponseEntity.ok(StatusResponse.builder()
                        .total_countries(0L)
                        .last_refreshed_at("")
                .build());
    }

    public ResponseEntity<?> getImage() {
        return null;
    }

    void updateCache(Country country) {
        statusCache.put("count", "250");
        statusCache.put("last_updated", country.getLastRefreshed().toString());
    }
}
