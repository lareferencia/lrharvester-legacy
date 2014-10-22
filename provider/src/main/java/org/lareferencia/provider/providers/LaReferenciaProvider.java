/*******************************************************************************
 * Copyright (c) 2013 lmatas.
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
package org.lareferencia.provider.providers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.repositories.NetworkRepository;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.provider.domain.MetadataFormat;
import org.lareferencia.provider.domain.Record;
import org.lareferencia.provider.domain.SetMembership;
import org.lareferencia.provider.exceptions.CannotDisseminateFormatException;
import org.lareferencia.provider.exceptions.IdDoesNotExistException;
import org.lareferencia.provider.exceptions.NoRecordsMatchException;
import org.lareferencia.provider.exceptions.NoSetHierarchyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class LaReferenciaProvider implements IProvider
{
   private static final int PAGE_SIZE = 300;;

   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

   private static final String DRIVER_SET_NAME = "DRIVER";
   
   
   
   private Log log = LogFactory.getLog(getClass());	

  
   /****************/
   @Autowired 
   NetworkRepository nationalNetworkRepository;
   
   @Autowired 
   NetworkSnapshotRepository networkSnapshotRepository;
   
   @Autowired 
   OAIRecordRepository oaiRecordRepository;
   /****************/
   

   /* Constructors
   ***************************************************************************/
   public LaReferenciaProvider()
   {
      
     
   }
  
   /* Provider Methods
   ***************************************************************************/

   public List<SetMembership> listSets() throws NoSetHierarchyException
   {
      final List<SetMembership> setMemberships = new ArrayList<SetMembership>();
      
      
      List<Network> networks = nationalNetworkRepository.findByPublishedOrderByNameAsc(true);
          
      for (Network network:networks) {
    	  
    	  NetworkSnapshot lgkSnapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
    	  
    	  // Solo se lista si existe un snapshot válido y si tiene al menos un registro válido
    	  if ( lgkSnapshot != null && lgkSnapshot.getValidSize() > 0) {
    	  
	    	  final SetMembership setMembership = new SetMembership();
	          setMembership.setSetSpec(network.getAcronym());
	          setMembership.setSetName(network.getName());
	          setMembership.setSetDescription( "Set of: " + network.getName() + " network");
	          setMemberships.add(setMembership);
          
    	  }
    	  
      }
  
      if(setMemberships.size() == 0)
         throw new NoSetHierarchyException();
         
      return setMemberships;
   }

   public String[] getMetadataPrefixes(final String identifier) throws IdDoesNotExistException
   {
      // TODO: Throw IdDoesNotExistException if the Object doesn't exist.
      
      String[] matchingPrefixes = new String[]{"oai_dc"};
   
      return matchingPrefixes;
   }

   public Record getRecord(final String identifier, final MetadataFormat metadataFormat) throws CannotDisseminateFormatException, IdDoesNotExistException
   {
	   final Record record = new Record();
	   OAIRecord oairecord = loadRecordFromIdentifier(identifier);
       
       // Identifier.
       record.setIdentifier( buildIdentifier(oairecord) );
       record.setDate(dateFormat.format(oairecord.getDatestamp()) );
       record.setDeleted(false);
       record.addSet( oairecord.getSnapshot().getNetwork().getAcronym());
       record.setMetadata( oairecord.getPublishedXML() );
    
       return record;
   }
   
   public List<Record> listRecords(String set, StateHolder state, boolean includeMetadata) throws CannotDisseminateFormatException, NoRecordsMatchException
   {
      if(oaiRecordRepository == null)
         throw new IllegalStateException("listRecords() expects a non-null oairecord repository");
      
      boolean emptyCollection = false;
         
      final List<Record> records = new ArrayList<Record>();
      
      if ( state.isFirstCall() ) {
    	  
    	  List<Long>    snapshotIdList = new ArrayList<Long>();
    	  List<Integer> totalPageList = new ArrayList<Integer>();
    	  
    	  // CASO DE SET DEFINIDO
    	  if ( set != null && !set.toUpperCase().equals( DRIVER_SET_NAME ) ) {
    		  
    		  Network network = nationalNetworkRepository.findByAcronym(set);
    		  
    		  if ( network == null ) {
    			  throw new NoRecordsMatchException("Set don´t exist");
    		  }
    		  else {
	    		  NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
	    		  
	    		  if ( snapshot != null ) {
	    			  
	    			  // obtiene la primera página de cada snapshot 
	    			  Page<OAIRecord> page = oaiRecordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
	    			  
	    			  // agrega los datos del snapshot a la lista
	    			  snapshotIdList.add( snapshot.getId() );
	    			  totalPageList.add( page.getTotalPages() );
	    		  }
    		  }
    		  
    	  }
    	  else { // CASO DE SET NULL
	    	  // Se recorren todas las redes publicadas
	    	  for (Network network:nationalNetworkRepository.findByPublishedOrderByNameAsc(true)) {
	    		  
	    		  NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
	    		  
	    		  // Si tiene snapshot válido con registros válidos > 0
	    		  if ( snapshot != null && snapshot.getValidSize() > 0 ) {
	    			  
	    			  // obtiene la primera página de cada snapshot 
	    			  Page<OAIRecord> page = oaiRecordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
	    			  
	    			  // agrega los datos del snapshot a la lista
	    			  snapshotIdList.add( snapshot.getId() );
	    			  totalPageList.add( page.getTotalPages() );
	    		  }
	    	  }
    	  }
    	  // se inicializa el estado
    	  
    	  emptyCollection = snapshotIdList.size() == 0;
    	  
    	  state.initialize(snapshotIdList, totalPageList);
    		 
    		  
      } 
    
      
      
      if ( !emptyCollection ) {
      
	      // obtiene la página actual
	      NetworkSnapshot snapshot = networkSnapshotRepository.findOne( state.obtainActualSnapshotID() );
		  Page<OAIRecord> page = oaiRecordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(state.obtainActualPage(), PAGE_SIZE));
	
		  // actualiza el estado
		  state.update();
	      
	  
	      try
	      {
	
	         if(page.getContent().size() == 0)
	            throw new NoRecordsMatchException();
	
	  
	         for(OAIRecord oairecord: page.getContent())
	         {
	            final Record record = new Record();
	            
	            // Identifier.
	            record.setIdentifier( buildIdentifier(oairecord) );
	            record.setDate( dateFormat.format(oairecord.getDatestamp()) );
	            record.setDeleted(false);
	            record.addSet( oairecord.getSnapshot().getNetwork().getAcronym() );
	            
	            if (includeMetadata)
	            	record.setMetadata( oairecord.getPublishedXML() );
	            
	            records.add(record);
	         }
	      }
	      finally
	      {
	         //session.setMemento(memento);
	      }
	       
      
      }
      
      return records;
      
      
   }
   
	@Override
	public List<String> listOrigins() {
		  final List<String> uris = new ArrayList<String>();
	      
	      List<Network> networks = nationalNetworkRepository.findByPublishedOrderByNameAsc(true);
	          
	      for (Network network:networks) {
	    	  for (OAIOrigin origin:network.getOrigins()) {   		  
	    		  uris.add(origin.getUri());
	    	  }
	      }
	  
	      return uris;
	}
   /*
   public byte[] getRecordMetadata(final String identifier, final MetadataFormat metadataFormat) throws IdDoesNotExistException
   {
	   OAIRecord oairecord = loadRecordFromIdentifier(identifier);
       
       return oairecord.getPublishedXML().getBytes() ;
	   
   }
   */
   /* Local Methods
   ***************************************************************************/

   private String buildIdentifier(OAIRecord record) {  
	   return "lr::" + record.getSnapshot().getId() + "::" + record.getId() + "::" + record.getIdentifier();
   }
   
   private OAIRecord loadRecordFromIdentifier(String identifier) throws IdDoesNotExistException {
	   
	   String[] parsedIdentifier = identifier.split("::");
	   if ( parsedIdentifier.length < 4) 
		      throw new IdDoesNotExistException();
	   
	   OAIRecord record = oaiRecordRepository.findOne( Long.parseLong( parsedIdentifier[2] ) );
	   
	   if ( record == null ) {
		      throw new IdDoesNotExistException();
	   }
	   
	   return record;
	   
   }


   
}
