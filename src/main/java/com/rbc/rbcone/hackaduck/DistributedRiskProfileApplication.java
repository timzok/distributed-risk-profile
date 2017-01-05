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
import com.rbc.rbcone.hackaduck.model.country.repository.CountryRepository;
import com.rbc.rbcone.hackaduck.model.country.repository.RegionRepository;
import com.rbc.rbcone.hackaduck.model.incoming.SaraEntity;
import com.rbc.rbcone.hackaduck.model.incoming.SaraLegalFund;
import com.rbc.rbcone.hackaduck.model.incoming.SaraRelation;
import com.rbc.rbcone.hackaduck.model.incoming.SaraPeps;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraEntityRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraLegalFundRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraPepsRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;

@SpringBootApplication
public class DistributedRiskProfileApplication {

	@Autowired
	private SaraEntityRepository saraEntityRepo;
	
	@Autowired
	private SaraPepsRepository saraPepsRepo;
	
	@Autowired
	private SaraLegalFundRepository saraLegalFundRepo;
	
	@Autowired
	private SaraRelationRepository saraRelationRepo;
	
	@Autowired
	private RegionRepository regionRepo;
		
	private static final Logger log = LoggerFactory.getLogger(DistributedRiskProfileApplication.class);
	
	public static void main(String[] args) {
		log.info("main method");
		SpringApplication.run(DistributedRiskProfileApplication.class, args);
	}
	
	@Transactional
	@PostConstruct
	public void postConstruct() throws JsonParseException, JsonMappingException, IOException, InterruptedException{

		log.info("POSTCONSTRUCT");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		
		TypeReference<List<Region>> regionType = new TypeReference<List<Region>>() {};
		List<Region> regions = mapper.readValue(this.getClass().getResourceAsStream("/Regions.json"), regionType);

		for (Region cr: regions) {
			for (Country ctry: cr.getCountry()) {
				ctry.setRegion(cr);
			}
		}
		regionRepo.save(regions);
				
		TypeReference<List<SaraEntity>> mapType = new TypeReference<List<SaraEntity>>() {};
		List<SaraEntity> saraEntity = mapper.readValue(this.getClass().getResourceAsStream("/ttEntity.json"),mapType);
		saraEntityRepo.save(saraEntity);
		
		TypeReference<List<SaraPeps>> pepsType = new TypeReference<List<SaraPeps>>() {};
		List<SaraPeps> peps = mapper.readValue(this.getClass().getResourceAsStream("/ttPeps.json"),pepsType);
		saraPepsRepo.save(peps);
		
		TypeReference<List<SaraLegalFund>> saraLegalFundType = new TypeReference<List<SaraLegalFund>>() {};
		List<SaraLegalFund> saraLegalFunds = mapper.readValue(this.getClass().getResourceAsStream("/ttLegalFund.json"),saraLegalFundType);
		saraLegalFundRepo.save(saraLegalFunds);
	
		TypeReference<List<SaraRelation>> saraRelationType = new TypeReference<List<SaraRelation>>() {};
		List<SaraRelation> saraRelation = mapper.readValue(this.getClass().getResourceAsStream("/ttRelationEntLF.json"),saraRelationType);
		saraRelationRepo.save(saraRelation);

		Object[] a = saraRelationRepo.findRegionLevelRelations();
		for (int i=0; i<a.length;i++) {
			Object[] b = (Object[])a[i];
			System.out.println("<>>>" + b[0] + "  " + b[1]);
		}
		
	}
	
}
