package com.rbc.rbcone.hackaduck.model.country.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rbc.rbcone.hackaduck.model.country.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, String> {
	
	
}
