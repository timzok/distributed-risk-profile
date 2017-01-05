package com.rbc.rbcone.hackaduck.model.incoming.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.rbc.rbcone.hackaduck.model.incoming.SaraEntity;

public interface SaraEntityRepository extends CrudRepository<SaraEntity, String> { 

}

