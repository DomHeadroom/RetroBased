package com.retrobased.market.services;

import com.retrobased.market.entities.Country;
import com.retrobased.market.repositories.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Country> get(Long id) {
        return countryRepository.findById(id);
    }
}
