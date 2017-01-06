package com.rbc.rbcone.hackaduck.model.incoming.repository;

import org.springframework.data.repository.CrudRepository;

import com.rbc.rbcone.hackaduck.model.incoming.CountryRiskDB;

public interface CountryRiskRepository  extends CrudRepository<CountryRiskDB, String>  {

}
