package org.project.currencyexchangeapi.repository;

import org.project.currencyexchangeapi.entity.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCurrencyCode(String currencyCode);

    Optional<Country> findCountryByName(String name);

    void deleteCountryByName(String name);

    List<Country> findAllByCurrencyCode(String currencyCode, Sort sort);

    List<Country> findAllByRegion(String region, Sort sort);

    List<Country> findAllByRegionAndCurrencyCode(String region, String currencyCode, Sort sort);
}
