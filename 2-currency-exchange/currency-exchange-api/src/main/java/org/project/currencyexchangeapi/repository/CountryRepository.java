package org.project.currencyexchangeapi.repository;

import org.project.currencyexchangeapi.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCurrencyCode(String currencyCode);
}
