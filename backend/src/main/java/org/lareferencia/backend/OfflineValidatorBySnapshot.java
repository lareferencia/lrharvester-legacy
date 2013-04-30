package org.lareferencia.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.lareferencia.backend.domain.InvalidOccurrenceLogEntry;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.ValidationType;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.InvalidOccurrenceLogRepository;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
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
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Component
public class OfflineValidatorBySnapshot {

	
	private static DocumentBuilderFactory factory;
	private static DocumentBuilder docBuilder;

	private static final int PAGE_SIZE = 1000;


	@Autowired
	public NationalNetworkRepository repository;

	@Autowired
	public OAIRecordRepository recordRepository;
	
	@Autowired
	public NetworkSnapshotRepository snapshotRepository;
		
	
	@Autowired
	public InvalidOccurrenceLogRepository rlogRepository;
	

	public OfflineValidatorBySnapshot() {

	}


	public static void main(String[] args) throws TransformerConfigurationException, TransformerFactoryConfigurationError {

		
		System.out.println("Iniciando ...");
		Logger.getRootLogger().setLevel(Level.OFF);
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/spring/app-context.xml");

		OfflineValidatorBySnapshot m = context.getBean("offlineValidatorBySnapshot",
				OfflineValidatorBySnapshot.class);

		IValidator validator = context.getBean("validator", IValidator.class);
		ITransformer trasnformer = context.getBean("transformer",ITransformer.class);
		
		
		
		if ( args.length != 1 ) {
			
			System.out.println( "command [snapshotID]" );
		
			for (NationalNetwork network : m.repository.findAll()) {
				for (NetworkSnapshot snapshot : network.getSnapshots()) {
					System.out.println( "Red: " + network.getName() + " \t\tSnapID: " + snapshot.getId() + "\t Records: " + snapshot.getSize() + "\tFinalizado en: " + snapshot.getEndTime() + " Status: " + snapshot.getStatus() );
				}
			}
			
		}
		else {
		
			
			
			Long snapID = Long.parseLong( args[0] );	
			NetworkSnapshot snapshot = m.snapshotRepository.findOne(snapID);
			NationalNetwork network = snapshot.getNetwork();
			
			System.out.println("Borrando registro de casos de rechazo por campo del snapshot: " + snapshot.getId());
			m.rlogRepository.delete( m.rlogRepository.findBySnapID(snapshot.getId()) );

			
			Map<Long,NetworkSnapshot> snapshotByID = new HashMap<Long, NetworkSnapshot>();
			Map<Long, String> networksBySnap = new HashMap<Long, String>();

			networksBySnap.put(snapshot.getId(), network.getName());
			snapshotByID.put(snapshot.getId(), snapshot);
			
			StatsManager stats = new StatsManager(networksBySnap);
			
			Page<OAIRecord> page = m.recordRepository.findBySnapshot(snapshot, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();

			for (int i = 0; i < totalPages; i++) {
				
				System.out.println( "Procesando Snapshot/Red: " + snapID + " / " + network.getName() + " (paquete: " + i+1 + " / " + totalPages );

				page = m.recordRepository.findBySnapshot(snapshot,
						new PageRequest(i, PAGE_SIZE));

				for (OAIRecord orecord : page.getContent()) {

					try {
						HarvesterRecord hrecord = new HarvesterRecord(
								orecord.getIdentifier(),
								MedatadaDOMHelper.parseXML(orecord
										.getOriginalXML()
										.replace("&#", "#")));
				
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
													new InvalidOccurrenceLogEntry( snapshot.getId(), fname, cvr.getExpectedValue(), cvr.getReceivedValue()));
									}
								}
							} 
						}			
						
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0); // Si hay un error no continua
					}
				}
			}
			
			m.rlogRepository.flush();
			System.out.print( stats.toString() );

		}
		
	}

}


