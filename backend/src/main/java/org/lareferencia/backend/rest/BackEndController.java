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
package org.lareferencia.backend.rest;


import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.NetworkSnapshotMetadataStat;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIProviderStat;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.domain.SnapshotStatus;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.indexer.IIndexer;
import org.lareferencia.backend.indexer.IndexerWorker;
import org.lareferencia.backend.repositories.NetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotLogRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotMetadataStatRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIOriginRepository;
import org.lareferencia.backend.repositories.OAIProviderStatRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.repositories.OAIRecordValidationRepository;
import org.lareferencia.backend.repositories.OAISetRepository;
import org.lareferencia.backend.tasks.SnapshotManager;
import org.lareferencia.backend.transformer.ITransformer;
import org.lareferencia.backend.util.JsonDateSerializer;
import org.lareferencia.backend.validator.IValidator;
import org.lareferencia.backend.validator.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BackEndController {
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	@Autowired
	private NetworkRepository networkRepository;
	
	@Autowired
	private OAIOriginRepository originRepository;
	
	@Autowired 
	private OAISetRepository setRepository;
	
	@Autowired
	private NetworkSnapshotMetadataStatRepository statRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	@Autowired
	private NetworkSnapshotLogRepository networkSnapshotLogRepository;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private OAIRecordValidationRepository recordValidationRepository;
	
	@Autowired 
	private OAIProviderStatRepository oaiProviderStatRepository;
	
	@Autowired
	@Qualifier("indexer")
	IIndexer indexer;
	
	@Autowired
	@Qualifier("indexerXOAI")
	IIndexer indexerXOAI;
	
	
	@Autowired
	TaskScheduler scheduler;
	
	@Autowired
	SnapshotManager snapshotManager;
	
	
	//private static final Logger logger = LoggerFactory.getLogger(BackEndController.class);
	
	//private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	
	/**
	 * Login Services
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {	
		return "home";
	}
	
	@RequestMapping(value = "/diagnose/{networkISO}/{snapID}", method = RequestMethod.GET)
	public String diagnose(@PathVariable Long snapID, @PathVariable String networkISO, Locale locale, Model model) {	
		
		model.addAttribute("snapID", snapID);
		model.addAttribute("networkISO", networkISO);
		
		return "diagnose";
	}
	
	@RequestMapping(value = "/diagnose/{networkAcronym}", method = RequestMethod.GET)
	public String diagnose(@PathVariable String networkAcronym, Locale locale, Model model) throws Exception {	
		
		
		Network network = networkRepository.findByAcronym(networkAcronym);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		
		NetworkSnapshot lgkSnapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
		if ( lgkSnapshot == null )
			throw new Exception("No se encontró LGKSnapshot");
		
		model.addAttribute("snapID", lgkSnapshot.getId());
		model.addAttribute("networkAcronym", networkAcronym);
		
		return "diagnose";
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {	
		return "login";
	}
	
	@RequestMapping(value="/login", params="errorLogin", method = RequestMethod.GET)
	public String loginFailed(Locale locale, Model model) {
		model.addAttribute("loginFailed", true);
		return "login";
	}
	
	
	/************************** Backend *************************************/
	
	@ResponseBody
	@RequestMapping(value="/private/startHarvestingByNetworkID/{networkID}", method=RequestMethod.GET)
	public ResponseEntity<String> startHarvesting(@PathVariable Long networkID) throws Exception {
		
		Network network = networkRepository.findOne(networkID);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		snapshotManager.lauchHarvesting(networkID);
		
		return new ResponseEntity<String>("Havesting iniciado red:" + networkID, HttpStatus.OK);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/private/stopHarvestingBySnapshotID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> stopHarvesting(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
		
		snapshotManager.stopHarvesting(id);
		
		return new ResponseEntity<String>("Havesting detenido Snapshot:" + id, HttpStatus.OK);
	}
	
	
	/*** 
	@ResponseBody
	@RequestMapping(value="/private/resumeHarvestingBySnapshotID/{snapshotID}", method=RequestMethod.GET)
	public ResponseEntity<String> resumeHarvestingBySnapshotID(@PathVariable Long snapshotID) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(snapshotID);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + snapshotID);
		
		snapshotManager.relauchHarvesting(snapshotID);
		
		return new ResponseEntity<String>("Relauch havesting:" + snapshotID, HttpStatus.OK);
	}***/
	
	/**
	 * Este servicio para cada origen explora los sets (no los almacenados sino los provistos por ListSets)
	 * y para cada uno de ellos realiza una cosecha. Si los sets son disjuntos la coschecha final es completa y
	 * sin repeticiones
	 * @param networkID
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/private/harvestSetBySet/{networkID}", method=RequestMethod.GET)
	public ResponseEntity<String> harvestSetBySet(@PathVariable Long networkID) throws Exception {
		
		Network network = networkRepository.findOne(networkID);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		snapshotManager.lauchSetBySetHarvesting(networkID);
		
		return new ResponseEntity<String>("Havesting:" + networkID, HttpStatus.OK);
	}
	
	
	
	private void cleanSnapshot(NetworkSnapshot snapshot) {
		
		System.out.println("Limpiando Snapshot: " + snapshot.getId());

		
		// borra los resultados de validación
	    System.out.println("Borrando registros de validaciones");
		recordValidationRepository.deleteBySnapshotID(snapshot.getId());
		
		// borra las estadisticas
	    System.out.println("Borrando stadísticas de metadatos");
	    statRepository.deleteBySnapshotID(snapshot.getId());
		
		// borra el log de cosechas
	    System.out.println("Borrando registros de log");
		networkSnapshotLogRepository.deleteBySnapshotID(snapshot.getId());
		
		// borra los registros
	    System.out.println("Borrando registros de metadatos");
		recordRepository.deleteBySnapshotID(snapshot.getId());
		
		// marcando snapshot borrado
		snapshot.setDeleted(true);
		networkSnapshotRepository.save(snapshot);


	}
	
	private void deleteSnapshot(NetworkSnapshot snapshot) {
		
		System.out.println("Borrando Snapshot: " + snapshot.getId());
		
		// limpiando snapshot
		cleanSnapshot(snapshot);
			
		// borra snapshot
		networkSnapshotRepository.delete(snapshot);
	}
	
	private void deleteNetwork(Network network) throws Exception {
		
		System.out.println("Comenzando proceso de borrando Red: " + network.getName() );

		
		System.out.println("Borrando la red del índice Solr");
		launchIndexerWorker(network.getId(), indexer, true);
		
		
		for ( NetworkSnapshot snapshot:network.getSnapshots() ) {
			deleteSnapshot(snapshot);
		}
		
		System.out.println("Borrando Origenes/Sets" );
		
		for ( OAIOrigin origin : network.getOrigins() ) {
			setRepository.deleteInBatch( origin.getSets() );
		}
		
		originRepository.deleteInBatch( network.getOrigins() );
		networkRepository.delete(network);
		
		System.out.println("Finalizando borrado red: " + network.getName());
	}
	
	
	
	@Transactional
	@ResponseBody
	@RequestMapping(value="/private/deleteNetworkByID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> deleteNetworkByID(@PathVariable Long id) throws Exception {
		
		Network network = networkRepository.findOne(id);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		deleteNetwork(network);

		return new ResponseEntity<String>("Borrada la red:" + network.getName(), HttpStatus.OK);

	}
		
	
	@Transactional
	@ResponseBody
	@RequestMapping(value="/private/deleteNetworkFromIndexByID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> deleteNetworkFromIndexByID(@PathVariable Long id) throws Exception {
		
		Network network = networkRepository.findOne(id);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		System.out.println("Comenzando proceso de borrando Red del índice: " + network.getName() );
		
		launchIndexerWorker(id, indexer, true);
				
		System.out.println("Finalizando borrado red: " + network.getName() + " del índice");

	
		return new ResponseEntity<String>("Borrada del índice la red :" + network.getName(), HttpStatus.OK);

	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(value="/private/deleteNetworkFromXOAIIndexByID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> deleteNetworkFromXOAIIndexByID(@PathVariable Long id) throws Exception {
		
		Network network = networkRepository.findOne(id);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		System.out.println("Comenzando proceso de borrando Red del índice XOAI: " + network.getName() );
		
		launchIndexerWorker(id, indexerXOAI, true);
				
		System.out.println("Finalizando borrado red: " + network.getName() + " del índice XOAI");

	
		return new ResponseEntity<String>("Borrada del índice XOAI la red :" + network.getName(), HttpStatus.OK);

	}
	
	
	
	@Transactional
	@ResponseBody
	@RequestMapping(value="/private/deleteAllButLGKSnapshot/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> deleteAllButLGKSnapshot(@PathVariable Long id) throws Exception {
		
		Network network = networkRepository.findOne(id);
		if ( network == null )
			throw new Exception("No se encontró RED");
		
		
		NetworkSnapshot lgkSnapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(id);
		
		
		for ( NetworkSnapshot snapshot:network.getSnapshots() ) {
			
			System.out.println("Evaluando para borrado: " + snapshot.getId());
			
			if ( (lgkSnapshot == null || !snapshot.getId().equals(lgkSnapshot.getId())) && !snapshot.isDeleted() 
					&& snapshot.getStatus() != SnapshotStatus.HARVESTING && snapshot.getStatus() != SnapshotStatus.RETRYING 
					&& snapshot.getStatus() != SnapshotStatus.INDEXING) { // previene el borrado de harvestings en proceso
				
				System.out.println("Borrando ... " + snapshot.getId());
				
				
				cleanSnapshot(snapshot);
			}
		}
		
		
		return new ResponseEntity<String>("Borrados snapshots excedentes de:" + network.getName(), HttpStatus.OK);
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(value="/private/deleteRecordsBySnapshotID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> deleteRecordsBySnapshotID(@PathVariable Long id) {
		
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		cleanSnapshot(snapshot);
		
		return new ResponseEntity<String>("Registros borrados snapshot: " + id.toString(), HttpStatus.OK);
		
	}
	
	
	/**
	@ResponseBody
	@RequestMapping(value="/private/indexValidRecordsBySnapshotID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> indexRecordsBySnapshotID(@PathVariable Long id) {
		
		// Se crea un proceso separado para la indexación
		callIndexerWorker(indexer, false);
		
		
		return new ResponseEntity<String>("Indexando Snapshot: " + id, HttpStatus.OK);
	}*/
	
	
	@ResponseBody
	@RequestMapping(value="/private/indexLGKSnapshotByNetworkID/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> indexLGKByNetworkID(@PathVariable Long id) {
		
		
		
		try {
			launchIndexerWorker(id, indexer, false);
			return new ResponseEntity<String>("XOAI LGK Snapshot RED: " + id, HttpStatus.OK);

		}
		catch (Exception e) {	
			return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.OK);
		} 
	
	}
	
	@ResponseBody
	@RequestMapping(value="/private/indexLGKSnapshotByNetworkID2XOAI/{id}", method=RequestMethod.GET)
	public ResponseEntity<String> indexLGKByNetworkID2XOAI(@PathVariable Long id) {
		
		try {
			launchIndexerWorker(id, indexerXOAI, false);
			return new ResponseEntity<String>("XOAI LGK Snapshot RED: " + id, HttpStatus.OK);

		}
		catch (Exception e) {	
			return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.OK);
		} 
	}

	/**************************** FrontEnd  ************************************/

	@ResponseBody
	@RequestMapping(value="/public/validateOriginalRecordByID/{id}", method=RequestMethod.GET)
	public ResponseEntity<ValidationResult> validateOriginalRecordByID(@PathVariable Long id) throws Exception {
		
		OAIRecord record = recordRepository.findOne(id);
		
		
		if ( record != null ) {
			
			Network network = record.getSnapshot().getNetwork();
			IValidator validator = applicationContext.getBean(network.getValidatorName(), IValidator.class);
			
			OAIRecordMetadata metadata = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML());
			ValidationResult result = validator.validate(metadata);
			ResponseEntity<ValidationResult> response = new ResponseEntity<ValidationResult>(result, HttpStatus.OK);
			return response; 
		}	
		else
			throw new Exception("Registro inexistente");
		
	}
	
	@ResponseBody
	@RequestMapping(value="/public/validateTransformedRecordByID/{id}", method=RequestMethod.GET)
	public ResponseEntity<ValidationResult> validateTransformedRecordByID(@PathVariable Long id) throws Exception {
		
		OAIRecord record = recordRepository.findOne(id);	
		
		if ( record != null ) {
			
			Network network = record.getSnapshot().getNetwork();
			IValidator validator = applicationContext.getBean(network.getValidatorName(), IValidator.class);
			ITransformer transformer = applicationContext.getBean(network.getTransformerName(), ITransformer.class);

			OAIRecordMetadata metadata = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML());
			
			ValidationResult preValidationResult = validator.validate(metadata);
			transformer.transform(metadata, preValidationResult);
			ValidationResult posValidationResult = validator.validate(metadata);
	
			ResponseEntity<ValidationResult> response = new ResponseEntity<ValidationResult>(posValidationResult, HttpStatus.OK);
		
			return response;
		}
		else
			throw new Exception("Registro inexistente");
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/public/harvestMetadataByRecordID/{id}", method=RequestMethod.GET)
	public String harvestyMetadataByRecordID(@PathVariable Long id) throws Exception {
		
		
		OAIRecord record = recordRepository.findOne( id );	
		String result = "";
		
		if ( record != null ) {
			
			Network network = record.getSnapshot().getNetwork();
		
			ArrayList<OAIOrigin> origins =  new ArrayList<>( network.getOrigins() );
			String oaiURLBase = origins.get(0).getUri();
			String recordURL = oaiURLBase +  "?verb=GetRecord&metadataPrefix=oai_dc&identifier=" + record.getIdentifier();
			
		
			HttpClient client = new HttpClient();
			client.getParams().setParameter("http.protocol.content-charset", "UTF-8");

			HttpMethod method = new GetMethod(recordURL);
			int responseCode = client.executeMethod(method);
			if (responseCode != 200) {
			    throw new HttpException("HttpMethod Returned Status Code: " + responseCode + " when attempting: " + recordURL);
			}
			
			result = new String( method.getResponseBody(), "UTF-8"); 
			
		}
		
		
		return result;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/public/transformInfoByRecordID/{id}", method=RequestMethod.GET)
	public ResponseEntity<OAIRecordTransformationInfo> transformInfoByRecordID(@PathVariable Long id) throws Exception {
		
		OAIRecordTransformationInfo result = new OAIRecordTransformationInfo();
		
		OAIRecord record = recordRepository.findOne( id );	
		
		if ( record != null ) {
			
			Network network = record.getSnapshot().getNetwork();
			IValidator validator = applicationContext.getBean(network.getValidatorName(), IValidator.class);
			ITransformer transformer = applicationContext.getBean(network.getTransformerName(), ITransformer.class);
			
			OAIRecordMetadata metadata = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML());
			
			ValidationResult preValidationResult = validator.validate(metadata);
			transformer.transform(metadata, preValidationResult);
			ValidationResult posValidationResult = validator.validate(metadata);
	
			result.id = id;
			result.originalHeaderId = record.getIdentifier();
			result.originalMetadata = record.getPublishedXML();
			result.transformedMetadata = metadata.toString();
			result.isOriginalValid = preValidationResult.isValid();
			result.isTransformedValid = posValidationResult.isValid();
			result.preValidationResult = preValidationResult;
			result.posValidationResult = posValidationResult;
			
			ResponseEntity<OAIRecordTransformationInfo> response = new ResponseEntity<OAIRecordTransformationInfo>(result, HttpStatus.OK);
			
			return response;
		}
			else
				throw new Exception("Registro inexistente");
			
	}
	
	@ResponseBody
	@RequestMapping(value="/public/transformRecordByID/{id}", method=RequestMethod.GET)
	public String transformRecordByID(@PathVariable Long id) throws Exception {
		
		
		OAIRecord record = recordRepository.findOne( id );	
		
		if ( record != null ) {
			
			Network network = record.getSnapshot().getNetwork();
			IValidator validator = applicationContext.getBean(network.getValidatorName(), IValidator.class);
			ITransformer transformer = applicationContext.getBean(network.getTransformerName(), ITransformer.class);
			
			OAIRecordMetadata metadata = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML());
			
			ValidationResult preValidationResult = validator.validate(metadata);
			transformer.transform(metadata, preValidationResult);
	
			
			return metadata.toString();
		
		}
			else
				throw new Exception("Registro inexistente");
			
	}
	
	
	@ResponseBody
	@RequestMapping(value="/public/lastGoodKnowSnapshotByNetworkID/{id}", method=RequestMethod.GET)
	public ResponseEntity<NetworkSnapshot> getLGKSnapshot(@PathVariable Long id) {
			
		NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(id);
		ResponseEntity<NetworkSnapshot> response = new ResponseEntity<NetworkSnapshot>(
			snapshot,
			snapshot == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
		);
		return response;
	}

	@ResponseBody
	@RequestMapping(value="/public/getSnapshotByID/{id}", method=RequestMethod.GET)
	public ResponseEntity<NetworkSnapshot> getSnapshotByID(@PathVariable Long id) {
			
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		ResponseEntity<NetworkSnapshot> response = new ResponseEntity<NetworkSnapshot>(
			snapshot,
			snapshot == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
		);
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/getSnapshotInfoByID/{id}", method=RequestMethod.GET)
	public NetworkInfo getSnapshotInfoByID(@PathVariable Long id) throws Exception {
			
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
		
		
		Network network = snapshot.getNetwork();
		
		NetworkInfo ninfo = new NetworkInfo();
		ninfo.networkID = network.getId();
		ninfo.acronym = network.getAcronym();
		ninfo.name = network.getName();
		
		ninfo.snapshotID = snapshot.getId();
		ninfo.datestamp = snapshot.getEndTime();
		ninfo.size = snapshot.getSize();
		ninfo.validSize = snapshot.getValidSize();
		ninfo.transformedSize = snapshot.getTransformedSize();
		
		return ninfo;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/public/lastGoodKnowSnapshotByNetworkAcronym/{acronym}", method=RequestMethod.GET)
	public ResponseEntity<NetworkSnapshot> getLGKSnapshot(@PathVariable String acronym) throws Exception {
		
		Network network = networkRepository.findByAcronym(acronym);
		if ( network == null ) // TODO: Implementar Exc
			throw new Exception("No se encontró RED: " + acronym);
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot válido de la RED: " + acronym);
		
		ResponseEntity<NetworkSnapshot> response = new ResponseEntity<NetworkSnapshot>(
			snapshot,
			snapshot == null ? HttpStatus.NOT_FOUND : HttpStatus.OK
		);
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/listSnapshotsByNetworkAcronym/{acronym}", method=RequestMethod.GET)
	public ResponseEntity<List<NetworkSnapshot>> listSnapshotsByAcronym(@PathVariable String acronym) throws Exception {
		
		Network network = networkRepository.findByAcronym(acronym);
		if ( network == null )
			throw new Exception("No se encontró RED: " + acronym);
		
		ResponseEntity<List<NetworkSnapshot>> response = new ResponseEntity<List<NetworkSnapshot>>(networkSnapshotRepository.findByNetworkOrderByEndTimeAsc(network), HttpStatus.OK);
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/listNetworks", method=RequestMethod.GET)
	public ResponseEntity<List<NetworkInfo>> listNetworks() {
		
				
		List<Network> allNetworks = networkRepository.findByPublishedOrderByNameAsc(true);//OrderByName();
		List<NetworkInfo> NInfoList = new ArrayList<NetworkInfo>();

		for (Network network:allNetworks) {
			
			NetworkInfo ninfo = new NetworkInfo();
			ninfo.networkID = network.getId();
			ninfo.acronym = network.getAcronym();
			ninfo.name = network.getName();
			
			NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
			
			if ( snapshot != null) {
				
				ninfo.snapshotID = snapshot.getId();
				ninfo.datestamp = snapshot.getEndTime();
				ninfo.size = snapshot.getSize();
				ninfo.validSize = snapshot.getValidSize();
				ninfo.transformedSize = snapshot.getTransformedSize();
				
			}		
			NInfoList.add( ninfo );		
		}
	
		ResponseEntity<List<NetworkInfo>> response = new ResponseEntity<List<NetworkInfo>>(NInfoList, HttpStatus.OK);
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/listNetworksHistory", method=RequestMethod.GET)
	public ResponseEntity<List<NetworkHistory>> listNetworksHistory() {
		
		List<Network> allNetworks = networkRepository.findByPublishedOrderByNameAsc(true);//OrderByName();
		List<NetworkHistory> NHistoryList = new ArrayList<NetworkHistory>();

		for (Network network:allNetworks) {	
			NetworkHistory nhistory = new NetworkHistory();
			nhistory.name = network.getName();
			nhistory.networkID = network.getId();
			nhistory.acronym = network.getAcronym();
			nhistory.validSnapshots =  networkSnapshotRepository.findByNetworkAndStatusOrderByEndTimeAsc(network, SnapshotStatus.VALID);
			NHistoryList.add( nhistory );		
		}
	
		ResponseEntity<List<NetworkHistory>> response = new ResponseEntity<List<NetworkHistory>>(NHistoryList, HttpStatus.OK);
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/listValidPublicSnapshotsStats", method=RequestMethod.GET)
	public List<SnapshotStats> listValidPublicSnapshotHistory() {
		
		List<Network> allNetworks = networkRepository.findByPublishedOrderByNameAsc(true);//OrderByName();
		List<SnapshotStats> snapshotStatsList = new ArrayList<SnapshotStats>();

		for (Network network:allNetworks) {	
			
			
			for (NetworkSnapshot snapshot: networkSnapshotRepository.findByNetworkAndStatusOrderByEndTimeAsc(network, SnapshotStatus.VALID) ) {
				
				SnapshotStats snapshotStats = new SnapshotStats();
				snapshotStats.setAcronym( network.getAcronym() );
				snapshotStats.setName( network.getName() );

				snapshotStats.setDatestamp( snapshot.getEndTime() );
				snapshotStats.setSize( snapshot.getSize() );	
				snapshotStats.setValidSize( snapshot.getValidSize() );	
				snapshotStats.setTransformedSize( snapshot.getTransformedSize() );	
				
				snapshotStatsList.add( snapshotStats );
				
			}
		}
	
		
		return snapshotStatsList;
	}
	
	
	
	@RequestMapping(value="/public/listProviderStats", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIProviderStat> listProviderStats(@RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) {
		
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIProviderStat> pageResult = oaiProviderStatRepository.findAll( new PageRequest(page, size, new Sort(Sort.Direction.DESC,"requestCount")));	
		
		return new PageResource<OAIProviderStat>(pageResult,"page","size");
	}
	
	
	@RequestMapping(value="/public/listOriginsBySnapshotID/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<OAIOrigin> listOriginsBySnapshotID(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
		
		return (List<OAIOrigin>) snapshot.getNetwork().getOrigins();
		
	}

	@RequestMapping(value="/public/listRepositoriesBySnapshotID/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<String> listRepositoriesBySnapshotID(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
		
		return recordRepository.listRepositoriesBySnapshotId(id);
		
	}
	
	
	@RequestMapping(value="/public/getMetadataStatsBySnapshotID/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<NetworkSnapshotMetadataStat> getMetadataStatsBySnapshotID(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
		
		
		return statRepository.findBySnapshot(snapshot);	
	}
	
	@RequestMapping(value="/public/getLGKMetadataStatsByNetworkAcronym/{acronym}", method=RequestMethod.GET)
	@ResponseBody
	public List<NetworkSnapshotMetadataStat> getLGKMetadataStatsByNetworkAcronym(@PathVariable String acronym) throws Exception {
		
		Network network = networkRepository.findByAcronym(acronym);
		if ( network == null ) // TODO: Implementar Exc
			throw new Exception("No se encontró RED: " + acronym);
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot válido de la RED: " + acronym);
				
		return statRepository.findBySnapshot(snapshot);	
	}
	
	
	@RequestMapping(value="/public/listInvalidRecordsInfoBySnapshotID/{id}", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIRecord> listInvalidRecordsInfoBySnapshotID(@PathVariable Long id, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
			
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIRecord> pageResult = recordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.INVALID, new PageRequest(page, size));	
		
		return new PageResource<OAIRecord>(pageResult,"page","size");
	}
	
	@RequestMapping(value="/public/listInvalidRecordsInfoByFieldAndSnapshotID/{field}/{id}", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIRecord> listInvalidRecordsInfoByFieldAndSnapshotID(@PathVariable String field, @PathVariable Long id, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
			
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIRecord> pageResult = recordRepository.findBySnapshotIdAndInvalidField(id, field, new PageRequest(page, size));	
		
		return new PageResource<OAIRecord>(pageResult,"page","size");
	}
	
	@RequestMapping(value="/public/listInvalidRecordsInfoBySnapshotIDAndRepositoryAndField/{id}/{repository}/{field}", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIRecord> listInvalidRecordsInfoBySnapshotIDAndRepositoryAndField(@PathVariable String field, @PathVariable String repository, @PathVariable Long id, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
			
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIRecord> pageResult = recordRepository.findBySnapshotIdAndRepositoyAndInvalidField(id, repository, field, new PageRequest(page, size));	
		
		return new PageResource<OAIRecord>(pageResult,"page","size");
	}
	
	@RequestMapping(value="/public/listValidRecordsInfoBySnapshotIDAndRepository/{id}/{repository}", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIRecord> listValidRecordsInfoBySnapshotIDAndRepository( @PathVariable Long id, @PathVariable String repository, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
			
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIRecord> pageResult = recordRepository.findValidBySnapshotIdAndRepository(id, repository, new PageRequest(page, size));	
		
		return new PageResource<OAIRecord>(pageResult,"page","size");
	}
	
	@RequestMapping(value="/public/listTransformedRecordsInfoBySnapshotIDAndRepository/{id}/{repository}", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIRecord> listTransformedRecordsInfoBySnapshotIDAndRepository( @PathVariable Long id, @PathVariable String repository, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
			
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIRecord> pageResult = recordRepository.findTransformedBySnapshotIdAndRepository(id, repository, new PageRequest(page, size));	
		
		return new PageResource<OAIRecord>(pageResult,"page","size");
	}
	
	
	
	
	
	
	@RequestMapping(value="/public/listTransformedRecordsInfoBySnapshotID/{id}", method=RequestMethod.GET)
	@ResponseBody
	public PageResource<OAIRecord> listTransformedRecordsInfoBySnapshotID(@PathVariable Long id, @RequestParam(required=false) Integer page, @RequestParam(required=false) Integer size) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot con id: " + id);
			
		if (page == null)
			page = 0;
		if (size == null)
			size = 100;
		
		Page<OAIRecord> pageResult = recordRepository.findBySnapshotAndWasTransformed(snapshot, true, new PageRequest(page, size));	
		
		return new PageResource<OAIRecord>(pageResult,"page","size");
	}
	
	/*
	@ResponseBody
	@RequestMapping(value="/public/metadataOccurrenceCountBySnapshotId/{id}", method=RequestMethod.GET)
	public List<NetworkSnapshotStat> metadataOccurrenceCountBySnapshotId(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		if (snapshot == null) 
			throw new Exception("No se encontró snapshot: " + id);
		
		List<NetworkSnapshotStat> stats = statsRepository.findBySnapshotAndStatId(snapshot, MetadataOccurrenceCountSnapshotStatProcessor.ID);
		
		return stats;
	}
	
	
	*/
	@ResponseBody
	@RequestMapping(value="/public/rejectedFieldCountBySnapshotId/{id}", method=RequestMethod.GET)
	public List<NetworkSnapshotStat> rejectedFieldCountBySnapshotId(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot: " + id);
		
		List<Object[]> oList = recordValidationRepository.invalidRecordCountByField(id);
		
		List<NetworkSnapshotStat> stats = new ArrayList<NetworkSnapshotStat>();
		
		for( Object[] o : oList ) {
			NetworkSnapshotStat s = new NetworkSnapshotStat( (String)o[0], (Long)o[1]);
			
			stats.add(s);
		}
		
			
		return stats;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/rejectedFieldCountBySnapshotIdAndRepository/{id}/{repository}", method=RequestMethod.GET)
	public List<NetworkSnapshotStat> rejectedFieldCountBySnapshotIdAndRepository(@PathVariable Long id, @PathVariable String repository) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot: " + id);
		
		List<Object[]> oList = recordValidationRepository.invalidRecordRepositoryCountByField(id, repository);
		
		//System.out.println( "Resultados: " + id + ": " + repository + " : " + oList.size() );
		
		List<NetworkSnapshotStat> stats = new ArrayList<NetworkSnapshotStat>();
		
		for( Object[] o : oList ) {
			NetworkSnapshotStat s = new NetworkSnapshotStat( (String)o[0], (Long)o[1]);
			
			stats.add(s);
		}
		
			
		return stats;
	}
	
	@ResponseBody
	@RequestMapping(value="/public/rejectedRepositoryCountBySnapshotId/{id}", method=RequestMethod.GET)
	public List<NetworkSnapshotStat> rejectedRepositoryCountBySnapshotId(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot: " + id);
		
		List<Object[]> oList = recordValidationRepository.invalidRecordCountByRepository(id);
		
		List<NetworkSnapshotStat> stats = new ArrayList<NetworkSnapshotStat>();
		
		for( Object[] o : oList ) {
			NetworkSnapshotStat s = new NetworkSnapshotStat( (String)o[0], (Long)o[1]);
			
			stats.add(s);
		}
		
			
		return stats;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/public/rejectedFieldAndRepositoryCountBySnapshotId/{id}", method=RequestMethod.GET)
	public Map<String, List<NetworkSnapshotStat>> rejectedFieldAndRepositoryCountBySnapshotId(@PathVariable Long id) throws Exception {
		
		NetworkSnapshot snapshot = networkSnapshotRepository.findOne(id);
		
		if (snapshot == null) // TODO: Implementar Exc
			throw new Exception("No se encontró snapshot: " + id);
		
		Map<String,List<NetworkSnapshotStat>> statMap = new Hashtable<String, List<NetworkSnapshotStat>>();
		
		List<Object[]> oList = recordValidationRepository.invalidRecordCountByFieldAndRepository(id);
		
		
		for( Object[] o : oList ) {
			
			String repository = (String)o[0];
			
			List<NetworkSnapshotStat> stats = statMap.get(repository);
			
			if (stats == null) {
				stats = new ArrayList<NetworkSnapshotStat>();
				statMap.put(repository, stats);
			}
			
			NetworkSnapshotStat s = new NetworkSnapshotStat( (String)o[1], (Long)o[2]);
			
			stats.add(s);
		}
		
			
		return statMap;
	}
	
	
////TODO: Funciones de servios web que deben ser encapsuladas en otra clase
	
	private void launchIndexerWorker(Long networkID, IIndexer specificIndexer, boolean deleteOnly) throws Exception {
		
			if ( !deleteOnly ) {
			// Si no es una acción de borrado hace un check de que exista un LGK Snapshot	
				NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(networkID);
			
				if ( snapshot == null ) 
					throw new Exception("Lanzamiento de Indexer Worker - No existe LGK para la red ID:" + networkID.toString() ); // TODO: Esta Exception tiene que ser de una jerarquía propia del controller rest
			}
	
			IndexerWorker worker = applicationContext.getBean("indexerWorker", IndexerWorker.class);
			worker.setNetworkID(networkID);
			worker.setIndexer(specificIndexer);
			worker.setDeleteNetworkWithoutReindexing(deleteOnly);
			
			// Esto encola el worker para que trabaje inmeditamente si es posible o cuando el scheduler decida
			scheduler.schedule(worker, new Date());
	}
	/**************  Clases de retorno de resultados *******************/
	
	@Getter
	@Setter
	class NetworkInfo {	
		public String acronym;
		private Long   networkID;
		private String name;
		
		private Long snapshotID;
		
		@JsonSerialize(using=JsonDateSerializer.class)
		private Date datestamp;
		private int size;
		private int validSize;
		private int transformedSize;

	}
	
	@Getter
	@Setter
	class NetworkHistory {	
		public String name;
		public String acronym;
		private Long   networkID;
		private List<NetworkSnapshot> validSnapshots;
	}
	
	@Getter
	@Setter
	class SnapshotStats {
		public Date datestamp;
		public String acronym;
		public String name;
		public Integer size;
		public Integer validSize;
		public Integer transformedSize;
	}
	
	@Getter
	@Setter
	class OAIRecordValidationInfo {	
		private Long   id;
		private String originalHeaderId;
		private boolean isValid;
		private boolean isDriverType;
		private String  dcTypeFieldContents;
	}
	
	@Getter
	@Setter
	class OAIRecordTransformationInfo {	
		private Long   id;
		private String originalHeaderId;
		private String originalMetadata;
		private String transformedMetadata;
		
		private ValidationResult preValidationResult;
		private ValidationResult posValidationResult;
		
		private boolean isOriginalValid;
		private boolean isTransformedValid;
	}
	
	
	@Getter
	@Setter
	public class NetworkSnapshotStat {
		
		private String field;		
		private Long value;
		
		public NetworkSnapshotStat() {
			super();
		}
		
		public NetworkSnapshotStat(String field, Long value) {
			
			this.field = field;
			this.value = value;
			
		}
	}
	
	
	
	
}
