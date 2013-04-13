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
public  class OfflineValidator {

	private static final int PAGE_SIZE = 250;

	@Autowired
	public NationalNetworkRepository repository;
	
	@Autowired
	public OAIRecordRepository recordRepository;
	
	
	@Autowired
	public InvalidOccurrenceLogRepository rlogRepository;
	
	
	public OfflineValidator() {
		
	}
	
	public OAIRecordRepository getRecordRepository() {
		return recordRepository;
	}
	
	public NationalNetworkRepository getRepository() {
		return repository;
	}

	
	public static void main(String[] args) {
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		ApplicationContext context = 
	             new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		OfflineValidator m = context.getBean("offlineValidator",OfflineValidator.class);
		IValidator validator = context.getBean("validator", IValidator.class);
		ITransformer trasnformer = context.getBean("transformer", ITransformer.class);
		
		
		Map<Long, String> networksBySnap = new HashMap<Long, String>();
		for (NationalNetwork network: m.repository.findAll() ) {
			for (NetworkSnapshot snapshot: network.getSnapshots() ) {
				networksBySnap.put(snapshot.getId(), network.getName());
			}
		}
		
	
		StatsManager stats = new StatsManager(networksBySnap);
		
		Page<OAIRecord> page = m.recordRepository.findAll( new PageRequest(0, PAGE_SIZE) ); 
		int totalPages = page.getTotalPages();
		
		for (int i=0; i<totalPages; i++) {
			page =  m.recordRepository.findAll( new PageRequest(i, PAGE_SIZE) );
			System.out.println( "Página: " + i + " de: " + totalPages);
			
			for (OAIRecord orecord:page.getContent() ) {
				
				NetworkSnapshot snap = orecord.getSnapshot();
				
				try {
					HarvesterRecord hrecord = new HarvesterRecord(orecord.getIdentifier(), 
							MedatadaDOMHelper.parseXML(orecord.getOriginalXML().replace("&#", "#")));
					
					// Log de la prevalidación
					ValidationResult result = validator.validate(hrecord);
					
					stats.addToStats(orecord, hrecord, result, ValidationType.PREVALIDATION);
					
					if ( !result.isValid() ) {
						hrecord = trasnformer.transform(hrecord);
					}
					
					// Log de la postvalidación
					result = validator.validate(hrecord);
					stats.addToStats(orecord, hrecord, result, ValidationType.POSTVALIDATION);
					
					/** En caso de no se válido loguea las reglas de los campos responsables */
					if ( !result.isValid() ) {
						
						for ( Entry<String, FieldValidationResult> entry:result.getFieldResults().entrySet() ) {

							String fname = entry.getKey();
							FieldValidationResult fvresult = entry.getValue();

							// Loguea solo los campos invalidos
							if ( (!fvresult.isValid() && fvresult.isMandatory()) ) {
								
								// Loguea solo las ocurrencias invalidas
								for (ContentValidationResult cvr: fvresult.getContentResults() ) {
									if ( !cvr.isValid()  )
										m.rlogRepository.save( 
												new InvalidOccurrenceLogEntry( snap.getId(), fname, cvr.getExpectedValue(), cvr.getReceivedValue()));
								}
							}
						} 
					}
					
					
					/*if (!result.isValid() && snap.getId().equals(1L))
						System.out.println(orecord.getIdentifier());*/


				
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0); // Si hay un error no continua
				} 
				
				//System.out.println( orecord.getIdentifier() ); 			
			}
				
			m.rlogRepository.flush();
		}
		
		System.out.print( stats.toString() );
	}	


}
