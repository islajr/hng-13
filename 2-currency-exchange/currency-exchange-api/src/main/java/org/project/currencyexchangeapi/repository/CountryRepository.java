package org.project.currencyexchangeapi.repository;

import org.project.currencyexchangeapi.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCurrencyCode(String currencyCode);

    Optional<Country> findCountryByName(String name);

    void deleteCountryByName(String name);


    Country findCountryByLastRefreshed_max();
}
