package org.project.currencyexchangeapi.controller;

import lombok.RequiredArgsConstructor;
import org.project.currencyexchangeapi.dto.response.GetCountryResponse;
import org.project.currencyexchangeapi.dto.response.StatusResponse;
import org.project.currencyexchangeapi.dto.response.thirdparty.country.CountryResponse;
import org.project.currencyexchangeapi.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping("/countries/refresh")
    public ResponseEntity<?> refreshCountries() {
        return countryService.refreshCountries();
    }

    @GetMapping("/countries")
    public ResponseEntity<List<GetCountryResponse>> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping("/countries/{name}")
    public ResponseEntity<GetCountryResponse> getCountry(@PathVariable String name) {
        return countryService.getCountry(name);
    }

    @DeleteMapping("/countries/{name}")
    public ResponseEntity<?> deleteCountry(@PathVariable String name) {
        return countryService.deleteCountry(name);
    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> getStatus() {
        return countryService.getStatus();
    }

    @GetMapping("/countries/image")
    public ResponseEntity<?> getImage() {
        return countryService.getImage();
    }

}
