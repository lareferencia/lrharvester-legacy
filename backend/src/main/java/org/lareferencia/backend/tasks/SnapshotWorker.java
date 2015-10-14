/*******************************************************************************
 * Copyright (c) 2013 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 *     Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
 * 
 * Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
 ******************************************************************************/
package org.lareferencia.backend.tasks;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.NetworkSnapshotLog;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.OAISet;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.harvester.HarvestingEvent;
import org.lareferencia.backend.harvester.IHarvester;
import org.lareferencia.backend.harvester.IHarvestingEventListener;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.repositories.NetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotLogRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.util.RepositoryNameHelper;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.util.datatable.JsonRenderer;
import org.lareferencia.backend.validation.ValidationManager;
import org.lareferencia.backend.validation.transformer.ITransformer;
import org.lareferencia.backend.validation.validator.IValidator;
import org.lareferencia.backend.validation.validator.ValidatorResult;
import org.lareferencia.backend.validation.validator.ValidatorRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Component
@Scope(value="prototype")
public class SnapshotWorker implements ISnapshotWorker, IHarvestingEventListener {
	
	@Value("${harvester.max.retries}")
	private int MAX_RETRIES;
	
	////// Variables de detección de repositorios tomadas desde la configuración 
	private RepositoryNameHelper repositoryNameHelper;

	
	@Value("${reponame.detection}")
	private Boolean runRepoNameDetection;
	
	@Value("${reponame.pattern}")
	private String repoNameDetectionPatternString;

	@Value("${reponame.append}")
	private Boolean doRepoNameAppend;
	
	@Value("${reponame.replaceExisting}")
	private Boolean doRepoNameReplace;
	
	@Value("${reponame.field}")
	private String repoNameField;
	
	@Value("${reponame.prefix}")
	private String repoNamePrefix;
	
	@Value("${instname.append}")
	private Boolean doInstNameAppend;
	
	@Value("${instname.replaceExisting}")
	private Boolean doInstNameReplace;
	
	@Value("${instname.field}")
	private String instNameField;
	
	@Value("${instname.prefix}")
	private String instNamePrefix;
	//// fin de variables de detección de nombres de repositorios
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	@Autowired
	private ValidationManager validationManager;
		
	@Autowired
	private NetworkRepository networkRepository;
		
	@Autowired
	private NetworkSnapshotRepository snapshotRepository;
	
	@Autowired
	private NetworkSnapshotLogRepository snapshotLogRepository;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	private IHarvester harvester;
	
	@Autowired
	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		
		// los eventos de harvesting serán manejados por el worker
		harvester.addEventListener(this);		
	}
	
	// No son autowired, se cargan en cada ejecución de acuerdo a la conf de cada red
	private IValidator validator;	
	private ITransformer transformer;
	

	
	@Autowired
	@Qualifier("indexer")
	IIndexer indexer;
	
	@Autowired
	@Qualifier("indexerXOAI")
	IIndexer indexerXOAI;
	
	@Setter
	private Long networkID;
	
	@Setter
	private Long snapshotID;

	private Network network;
	private NetworkSnapshot snapshot;

	@Getter @Setter
	private boolean harvestBySet = false;
	
	@Getter @Setter
	private SnapshotManager manager;

	
	public SnapshotWorker() {};
	
	public NetworkSnapshot getSnapshot() {
		return snapshot;
	}
	
	@Override
	public void stop() {
		
		System.out.print("Señal de stopHarvesting recibida por el worker: ");		
		harvester.stop();
	}

	/**
	 * TODO: Podría ser Async, pero no tiene sentido empezar un nuevo proceso de harvesting para una misma red si el anterior
	 * no terminó. Hay que cuidar los bloqueos!!! TODO: Podría implemetarse la limpieza de procesos inactivos para evitar problemas
	 * @throws Exception 
	 */
	@Override
	public void run() {
						
		network = networkRepository.findOne( networkID );
			
		if ( network != null ) {			
			snapshot = new NetworkSnapshot();
			snapshot.setNetwork(network);		
			snapshotRepository.save(snapshot);
		} else {
			System.err.println("La Red no existe!! El worker no puede continuar");
			return;
		}
		
		// Se cargan el validador y el transformador de acuerdo a la configuración de la red
		try {
			logMessage("Cargando validador y transformador  ..."); 

			validator = validationManager.createValidatorFromModel( network.getValidator() );
			transformer = validationManager.createTransformerFromModel( network.getTransformer() );
			
		} catch (Exception e) {		
			logMessage("Error en la carga del validador o transformador, vea el log para más detalles."); 
			setSnapshotStatus(SnapshotStatus.HARVESTING_FINISHED_ERROR);
			return;
		}
		
		// Se carga el helper para la resolución de nombre de repositorios
		repositoryNameHelper = new RepositoryNameHelper();
		repositoryNameHelper.setDetectREPattern(repoNameDetectionPatternString);
		
		// Se registra el incicio de tareas en el manager
		manager.registerWorkerBeginSnapshot(snapshot.getId(), this);
		
		logMessage("Comenzando cosecha ..."); 
		setSnapshotStatus(SnapshotStatus.HARVESTING);

		// harvest de la red
		
		// inicialización del harvester
		harvester.reset();
		
		if ( harvestBySet )
			harvestEntireNetworkBySet();
		else
			harvestEntireNetwork();
		
		// Luego del harvesting el snapshot puede presentar estados diversos
		
		// si no fue detenido
		if ( snapshot.getStatus() != SnapshotStatus.HARVESTING_STOPPED ) {
		
			// Si no generó errores, entonces terminó exitosa la cosecha
			if ( snapshot.getStatus() != SnapshotStatus.HARVESTING_FINISHED_ERROR ) {
				
				logMessage("Cosecha terminada en forma exitosa");			
	

				// Graba el status
				snapshot.setEndTime( new Date() );
				
				// Si está publicada la red y es una red que se indexa
				if ( network.isPublished() ) {
					
					if ( network.isRunIndexing() ) {
					
					    logMessage("Comenzando indexación ...");
					    setSnapshotStatus(SnapshotStatus.INDEXING);
	
	                    // Indexa
						boolean isSuccesfullyIndexed = indexer.index(snapshot.getNetwork(), snapshot, false);
						
						// Si el indexado es exitoso marca el snap válido
						if ( isSuccesfullyIndexed ) {
							// Graba el status
							setSnapshotStatus(SnapshotStatus.VALID);
		
							logMessage("Indexación terminada con éxito.") ;
						}
						else {
							// Graba el status
							setSnapshotStatus(SnapshotStatus.INDEXING_FINISHED_ERROR);
							logMessage("Error en proceso de indexación.");
						}
					}
					
					if ( network.isRunXOAI()) {
						
					    logMessage("Comenzando indexación XOAI ...");
					    setSnapshotStatus(SnapshotStatus.INDEXING);
	
	                    // Indexa
						boolean isSuccesfullyXOAIIndexed = indexerXOAI.index(snapshot.getNetwork(), snapshot, false);
						
						// Si el indexado es exitoso marca el snap válido
						if ( isSuccesfullyXOAIIndexed ) {
							// Graba el status
							setSnapshotStatus(SnapshotStatus.VALID);
		
							logMessage("Indexación XOAI terminada con éxito.") ;
						}
						else {
							// Graba el status
							setSnapshotStatus(SnapshotStatus.INDEXING_FINISHED_ERROR);
							logMessage("Error en proceso de indexación XOAI.");
						}
					}
					
					
				} 
				else {
					// si no está publicada o no se indexa la marca como válida sin indexar
					// Graba el status
					setSnapshotStatus(SnapshotStatus.VALID);
				}
				
			
			} else {
				logMessage("Cosecha finalizada con errores");
			}
		}
	
		snapshot.setEndTime( new Date() );
		snapshotRepository.save(snapshot);
				
		// Flush y llamados al GC
		snapshotRepository.flush();
		
		// Se registra el fin de tareas en el manager
		manager.registerWorkerEndSnapshot(snapshot.getId());
				
		System.gc();
	}
	
	/*************************************************************/
	

	private void harvestEntireNetwork() {
		// Ciclo principal de procesamiento, dado por la estructura de la red nacional
		// Se recorren los orígenes 
		for ( OAIOrigin origin:network.getOrigins() ) {
			
			// si tiene sets declarados solo va a cosechar 
			if ( origin.getSets().size() > 0 ) {
				for ( OAISet set: origin.getSets() ) {
					harvester.harvest(origin.getUri(), null, null, set.getSpec(), origin.getMetadataPrefix(), null, MAX_RETRIES);
				}
			}
			// si no hay set declarado cosecha todo
			else {
				harvester.harvest(origin.getUri(), null, null, null, origin.getMetadataPrefix(), null, MAX_RETRIES);
			}	
		}
	}
	
	private void harvestEntireNetworkBySet() {
		// Ciclo principal de procesamiento, dado por la estructura de la red nacional
		// Se recorren los orígenes 
		for ( OAIOrigin origin:network.getOrigins() ) {
			
			List<String> setList = harvester.listSets( origin.getUri() ); 
			
				for ( String setName:setList) {
					System.out.println( "Cosechando: " + origin.getName() + " set: " + setName); 
					harvester.harvest(origin.getUri(), null, null, setName, origin.getMetadataPrefix(), null, MAX_RETRIES);
				}
		}	
		
	}

	
	@Override
	@Transactional
	public void harvestingEventOccurred(HarvestingEvent event) {
		
		System.out.println( network.getName() + "  HarvestingEvent recibido: " + event.getStatus() );
			
		switch ( event.getStatus() ) {
			
			case OK:			
					
				//List<OAIRecord> records = new ArrayList<OAIRecord>(100);
			
				// Agrega los records al snapshot actual			
				for (OAIRecordMetadata  metadata:event.getRecords() ) {
					
					try {
						OAIRecord record = new OAIRecord();
						// registra el snapshot al que pertenece
						record.setSnapshot(snapshot);
						record.setIdentifier(metadata.getIdentifier());
						
						snapshot.incrementSize();
						
						if ( network.isRunValidation() ) {
						
							// prevalidación
							ValidatorResult validationResult = validator.validate(metadata);
							
							
							// si corresponde lo transforma
							if ( network.isRunTransformation() ) {
								
								// transforma
								Boolean wasTransformed = transformer.transform(metadata, validationResult);
								
								// marca si el registro fue transformado
								record.setWasTransformed(wasTransformed);
								
								// lo vuelve a validar
								validationResult = validator.validate(metadata);
												
								// Si es válido y hubo transformación incrementa la cuenta de válidos transformados
								if ( wasTransformed && validationResult.isValid() ) 		
									snapshot.incrementTransformedSize();
						
							}
							
							// registra si resultó válido o es irrecuperable (inválido)
							if ( validationResult.isValid() ) {
								record.setStatus( RecordStatus.VALID );
								snapshot.incrementValidSize();
							} else {
								record.setStatus( RecordStatus.INVALID );
							}
								
							
							// En esta etapa se intenta obtener el nombre del repositorio y el de la institución
							String repoName = repositoryNameHelper.extractNameFromMetadata(metadata, repoNameField, repoNamePrefix);
							
							// Si no existe el repoName en la metadata se intenta recuperarlo usando una expresión regular
							if ( runRepoNameDetection && repoName.equals( RepositoryNameHelper.UNKNOWN  ) )
								repoName = repositoryNameHelper.detectRepositoryDomain( metadata.getIdentifier() );
							
							// Se establece el nombre del repositorio en el registro
							record.setRepositoryDomain(repoName);
							
							
							// Si está configurado agrega a la metadata el reponame y el instname
							if ( doRepoNameAppend )
								repositoryNameHelper.appendNameToMetadata(metadata, repoNameField, repoNamePrefix, network.getName(), doRepoNameReplace );
							
							if ( doInstNameAppend )
								repositoryNameHelper.appendNameToMetadata(metadata, instNameField, instNamePrefix, network.getInstitutionName(), doInstNameReplace );
							
							
							// Se almacena la metadata transformada 
							//if ( validationResult.isValid() ) 
							record.setPublishedXML( metadata.toString() );
							
							
							//// SE ALMACENA EL REGISTRO
							recordRepository.save(record);
							recordRepository.flush();
							
							
							
						} 
						
						else { // Si no se valida entonces todo puesto para publicación
							// TODO: Chequear esto
							record.setStatus( RecordStatus.UNTESTED );
							record.setPublishedXML( metadata.toString() );
						}
						

					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			
				recordRepository.flush();
				snapshot.setEndTime( new Date() );
				
				// FIXME: Esto evita el problema con la restricción de resumption token mayor a 255
				String resumptionToken = event.getResumptionToken();
				if (resumptionToken != null && resumptionToken.length() > 255)
					resumptionToken = resumptionToken.substring(0, 255);
				
				
				snapshot.setResumptionToken(resumptionToken);

				// Graba el status
				setSnapshotStatus(SnapshotStatus.HARVESTING);
								                       
			break;
			
			case ERROR_RETRY:
				
				logMessage("Retry:" + event.getMessage());
				System.out.println( event.getMessage() );			
				
				snapshot.setEndTime( new Date() );
				
				// Graba el status
				setSnapshotStatus(SnapshotStatus.RETRYING);
			break;
			
			case ERROR_FATAL:
				
				logMessage("Fatal:" + event.getMessage());
				System.out.println( event.getMessage() );

				snapshot.setEndTime( new Date() );
				
				// Graba el status
				setSnapshotStatus(SnapshotStatus.HARVESTING_FINISHED_ERROR);
				
			break;
			
			case STOP_SIGNAL_RECEIVED:
				
				logMessage("Detención:" + event.getMessage());
				System.out.println( event.getMessage() );
				
				// Graba el status
				setSnapshotStatus(SnapshotStatus.HARVESTING_STOPPED);
			break;

			default:
				/**
				 * TODO: Definir que se hace en caso de eventos sin status conocido
				 */	
			break;
		}	
		
	}
	
	private void setSnapshotStatus(SnapshotStatus status) {
			// metodo auxiliar para centralizar la forma de actualizar status
		
			snapshot.setStatus( status );
			snapshotRepository.save(snapshot);
			snapshotRepository.flush();
		
	}
	
	private void logMessage(String message) {
                System.out.println(message);
		snapshotLogRepository.save( new NetworkSnapshotLog(message, this.snapshot) );
		snapshotLogRepository.flush();
	}
	
	
	

	@Override
	public SnapshotStatus getStatus() {
		if ( snapshot != null )
			return snapshot.getStatus();
		else 
			return SnapshotStatus.UNKNOWN;
	}
	
	
}
