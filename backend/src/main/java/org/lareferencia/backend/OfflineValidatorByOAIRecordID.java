package org.lareferencia.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.InvalidOccurrenceLogEntry;
import org.lareferencia.backend.domain.ValidationType;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.repositories.InvalidOccurrenceLogRepository;
import org.lareferencia.backend.stats.StatsManager;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.FieldValidationResult;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public  class OfflineValidatorByOAIRecordID {

	private static final int PAGE_SIZE = 250;

	@Autowired
	public NationalNetworkRepository repository;
	
	@Autowired
	public OAIRecordRepository recordRepository;
	
	
	@Autowired
	public InvalidOccurrenceLogRepository rlogRepository;
	
	
	public OfflineValidatorByOAIRecordID() {
		
	}
	
	public OAIRecordRepository getRecordRepository() {
		return recordRepository;
	}
	
	public NationalNetworkRepository getRepository() {
		return repository;
	}

	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("command IDRegistro(Long)");
			System.exit(1);
		}
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		OfflineValidatorByOAIRecordID m = context.getBean("offlineValidatorByOAIRecordID",OfflineValidatorByOAIRecordID.class);
		IValidator validator = context.getBean("validator", IValidator.class);
		ITransformer trasnformer = context.getBean("transformer", ITransformer.class);
		
		
		Map<Long, String> networksBySnap = new HashMap<Long, String>();
		for (NationalNetwork network: m.repository.findAll() ) {
			for (NetworkSnapshot snapshot: network.getSnapshots() ) {
				networksBySnap.put(snapshot.getId(), network.getName());
			}
		}
					
		OAIRecord orecord = m.recordRepository.findOne( Long.parseLong( args[0]) );	
		NetworkSnapshot snap = orecord.getSnapshot();
		
		System.out.println("\n*********************************** registro Original\n");
		System.out.println(orecord.getOriginalXML());
		System.out.println("\n*********************************** fin - registro Original\n");
		try {
			HarvesterRecord hrecord = new HarvesterRecord(orecord.getIdentifier(), 
					MedatadaDOMHelper.parseXML(orecord.getOriginalXML().replace("&#", "#")));
			
			// Log de la prevalidación
			ValidationResult result = validator.validate(hrecord);
			
			System.out.println("\n *********************************** validación registro Original\n");
			System.out.println(result);
			System.out.println("\n *********************************** fin validación registro Original\n");
			
			if ( !result.isValid() ) {
				hrecord = trasnformer.transform(hrecord);
			
				// Log de la postvalidación
				result = validator.validate(hrecord);		
					
				System.out.println("\n *********************************** registro trasnformado\n");
				System.out.println(hrecord.getMetadataXmlString());
				System.out.println("\n *********************************** fin - registro trasformado\n");
				
				System.out.println("\n *********************************** validación registro trasnformado\n");
				System.out.println(result);
				System.out.println("\n *********************************** fin validación registro transformado\n");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0); // Si hay un error no continua
		} 
		
	}
}
