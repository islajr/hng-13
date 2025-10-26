package org.project.currencyexchangeapi.controller;

import lombok.RequiredArgsConstructor;
import org.project.currencyexchangeapi.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping("/countries/refresh")
    public ResponseEntity<?> refreshCountries() {
        return countryService.refreshCountries();
    }

    @GetMapping("/countries")
    public ResponseEntity<?> getCountries() {
        return countryService.getCountries();
    }

    @GetMapping("/countries/{name}")
    public ResponseEntity<?> getCountry(@PathVariable String name) {
        return countryService.getCountry(name);
    }

    @DeleteMapping("/countries/{name}")
    public ResponseEntity<?> deleteCountry(@PathVariable String name) {
        return countryService.deleteCountry(name);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return countryService.getStatus();
    }

    @GetMapping("/countries/image")
    public ResponseEntity<?> getImage() {
        return countryService.getImage();
    }

}
