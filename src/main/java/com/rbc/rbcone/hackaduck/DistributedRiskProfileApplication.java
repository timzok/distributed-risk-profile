package com.rbc.rbcone.hackaduck;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbc.rbcone.hackaduck.model.country.Country;
import com.rbc.rbcone.hackaduck.model.country.Region;
import com.rbc.rbcone.hackaduck.model.country.repository.RegionRepository;

@SpringBootApplication
public class DistributedRiskProfileApplication {

	@Autowired
	private RegionRepository regionRepo;
	
	
	private static final Logger log = LoggerFactory.getLogger(DistributedRiskProfileApplication.class);
	
	public static void main(String[] args) {
		log.info("main method");
		SpringApplication.run(DistributedRiskProfileApplication.class, args);
	}
	
	@Transactional
	@PostConstruct
	public void postConstruct() throws JsonParseException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		TypeReference<List<Region>> mapType = new TypeReference<List<Region>>() {};
		
		List<Region> regions = mapper.readValue(this.getClass().getResourceAsStream("/Regions.json"),mapType);

		log.info("POSTCONSTRUCT");
		
		int i=0;
		for (Region cr: regions){
			
			cr.setId(i++);
			
			log.info(cr.getName());
			for (Country ctry: cr.getCountry())
			{
				log.info(ctry.getLabel()+"<->"+ctry.getType());
			}
		}
		
		log.info("Persisting the regions");
		regionRepo.save(regions);
		log.info("Region is persisted");	
		for (Region test: regionRepo.findAll())
		{
			log.info(test.getName() + "<>"+test.getId());
		}
	
	
		
		
	}
	
}
