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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshotLog;
import org.lareferencia.backend.domain.NetworkSnapshotStat;
import org.lareferencia.backend.domain.NetworkSnapshot;
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
import org.lareferencia.backend.repositories.NationalNetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotLogRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotStatRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.stats.StatsManager;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(value="prototype")
public class SnapshotWorker implements ISnapshotWorker, IHarvestingEventListener {
	
	private static int MAX_RETRIES = 15;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	private StatsManager statsManager; // se carga como prototype en cada harvesting
	
	@Autowired
	private NationalNetworkRepository networkRepository;
		
	@Autowired
	private NetworkSnapshotRepository snapshotRepository;
	
	@Autowired
	private NetworkSnapshotLogRepository snapshotLogRepository;

	@Autowired
	private NetworkSnapshotStatRepository statRepository;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	private IHarvester harvester;
	
	@Autowired
	public void setHarvester(IHarvester harvester) {
		this.harvester = harvester;
		
		// los eventos de harvesting serán manejados por el worker
		harvester.addEventListener(this);		
	}
	
	@Autowired
	private IValidator validator;
	
	@Autowired
	private ITransformer transformer;
	
	@Autowired
	private IIndexer indexer;
	
	@Setter
	private Long networkID;
	
	@Setter
	private Long snapshotID;

	private NationalNetwork network;
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
		
		
		// Se pide al contexto una nueva instacia del statsManager (es prototype) cada cosecha
		statsManager = applicationContext.getBean("statsManager", StatsManager.class);
		
		boolean newSnapshotCreated = true;
		
		if ( snapshotID != null ) { // Este es el caso en que se retorma un snapshot bloqueado 
	
			snapshot = snapshotRepository.findOne( snapshotID );
			
			if ( snapshot != null && snapshot.getStatus().equals(SnapshotStatus.HARVESTING_STOPPED) ) { // tiene que estar detenido
				
				network = snapshot.getNetwork(); 
				newSnapshotCreated = false;
				
				// cambia el status
				setSnapshotStatus(SnapshotStatus.HARVESTING);
				
			} else {
				System.err.println("El snapshot no existe o está ya está siendo procesado. El worker no puede continuar");
				return;
			}
						
	
		} else { // este es el caso donde se inicia un nuevo snapshot
			
			network = networkRepository.findOne( networkID );
				
			if ( network != null ) {			
				snapshot = new NetworkSnapshot();
				snapshot.setNetwork(network);		
				snapshotRepository.save(snapshot);
			} else {
				System.err.println("La Red no existe!! El worker no puede continuar");
				return;
			}
				
		}
		
		// Se registra el incicio de tareas en el manager
		manager.registerWorkerBeginSnapshot(snapshot.getId(), this);
		
		
		logMessage("Comenzando cosecha ..."); 
		setSnapshotStatus(SnapshotStatus.HARVESTING);

		// harvest de la red
		
		// inicialización del harvester
		harvester.reset();
		
		if ( newSnapshotCreated ) // caso de harvesting desde cero
			
			if ( harvestBySet )
				harvestEntireNetworkBySet();
			else
				harvestEntireNetwork();
		
		else // caso de retomar desde un rt anterior
			harvestNetworkFromRT(snapshot.getResumptionToken());

		
		// si no fue detenido
		if ( snapshot.getStatus() != SnapshotStatus.HARVESTING_STOPPED ) {
		
			// Si no generó errores, entonces terminó exitosa la cosecha
			if ( snapshot.getStatus() != SnapshotStatus.HARVESTING_FINISHED_ERROR ) {
				
				logMessage("Cosecha terminada en forma exitosa");			
				logMessage("Comenzando indexación ...");
	
				// Graba el status
				snapshot.setEndTime( new Date() );
				setSnapshotStatus(SnapshotStatus.INDEXING);
				
				// Si está publicada la red y es una red que se indexa
				if ( network.isRunIndexing() && network.isPublished() ) {
					
					// Indexa
					boolean isSuccesfullyIndexed = indexer.index(snapshot);
					
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
		
		// Persistencia de las estadísticas en caso de snap exitoso
		if ( snapshot.getStatus() == SnapshotStatus.VALID) {
			
			for ( NetworkSnapshotStat stat: statsManager.getResults() ) {
				stat.setSnapshot(snapshot);
				statRepository.save(stat);
			}
			
			statRepository.flush();
		}
			
		// Flush y llamados al GC
		snapshotRepository.flush();
		
		// Se registra el fin de tareas en el manager
		manager.registerWorkerEndSnapshot(snapshot.getId());
				
		System.gc();
	}
	
	/*************************************************************/
	
	
	
	private void harvestNetworkFromRT(String resumptionToken) {	
		// Se recorren los orígenes evaluando de cual era el rt TODO: Hay que guardar el origen corriente en el snapshot
		for ( OAIOrigin origin:network.getOrigins() ) {
			harvester.harvest(origin.getUri(), null, null, null, origin.getMetadataPrefix(), resumptionToken, MAX_RETRIES);
		}
	}
	
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
					harvester.harvest(origin.getUri(), null, null, setName, origin.getMetadataPrefix(), null, 5);
				}
		}	
		
	}

	
	@Override
	@Transactional
	public void harvestingEventOccurred(HarvestingEvent event) {
		
		System.out.println( network.getName() + "  HarvestingEvent recibido: " + event.getStatus() );
			
		switch ( event.getStatus() ) {
			
			case OK:			
					
				List<OAIRecord> records = new ArrayList<OAIRecord>(100);
			
				// Agrega los records al snapshot actual			
				for (OAIRecordMetadata  metadata:event.getRecords() ) {
					
					try {
						OAIRecord record = new OAIRecord(metadata.getIdentifier(), metadata.toString());
						// registra el snapshot al que pertenece
						record.setSnapshot(snapshot);
						
						snapshot.incrementSize();
						
						if ( network.isRunValidation() ) {
						
							// prevalidación
							ValidationResult validationResult = validator.validate(metadata);
							
							// si no es válido lo transforma
							if ( !validationResult.isValid() && network.isRunTransformation() ) {
								
								// transforma y si hubo transformación la registra
								if ( transformer.transform(metadata, validationResult) ) {
									snapshot.incrementTransformedSize();
									
									// marca si el registro fue transformado
									record.setWasTransformed(true);
								}
								
								// lo vuelve a validar
								validationResult = validator.validate(metadata);
							}
							
							// registra si resultó válido o es irrecuperable (inválido)
							if ( validationResult.isValid() ) {
								record.setStatus( RecordStatus.VALID );
								snapshot.incrementValidSize();
							} else {
								record.setStatus( RecordStatus.INVALID );
							}
								
							// Se almacena la metadata transformada para los registros válidos
							if ( validationResult.isValid() ) 
								record.setPublishedXML( metadata.toString() );
							
							
							///////// Cálculo de estadísticas
							statsManager.process(metadata, validationResult);
							
							///////// Test de pertenencia a la colección del registro final
							ValidationResult btcValidationResult = validator.testIfBelongsToCollection(metadata);
							record.setBelongsToCollection( btcValidationResult.isValid() );
							record.setBelongsToCollectionDetails( btcValidationResult.getValidationContentDetails() );
							
						} 
						
						else { // Si no se valida entonces todo puesto para publicación
							// TODO: Chequear esto
							record.setStatus( RecordStatus.UNTESTED );
							record.setPublishedXML( metadata.toString() );
						}
						
						records.add(record);
						recordRepository.save(records);
						recordRepository.flush();

					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			
				snapshot.setEndTime( new Date() );
				snapshot.setResumptionToken( event.getResumptionToken() );

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
		snapshotLogRepository.save( new NetworkSnapshotLog(message, this.snapshot) );
		snapshotLogRepository.flush();
	}
	
	
}
