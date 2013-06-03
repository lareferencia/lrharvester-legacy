/*
* Copyright 2008 the original author or authors.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/ 

package org.lareferencia.provider.providers;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.repositories.NationalNetworkRepository;
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


private Log log = LogFactory.getLog(getClass());	

  
  
   /****************/
   @Autowired 
   NationalNetworkRepository nationalNetworkRepository;
   
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
      
      
      List<NationalNetwork> networks = nationalNetworkRepository.findByPublishedOrderByNameAsc(true);
          
      for (NationalNetwork network:networks) {
    	  
    	  final SetMembership setMembership = new SetMembership();
          setMembership.setSetSpec(network.getCountry().getIso());
          setMembership.setSetName(network.getName());
          // TODO: setMembership.setSetDescription();
          setMemberships.add(setMembership);
    	  
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
       record.setDate( oairecord.getDatestamp().toString() );
       record.setDeleted(false);
       record.addSet( oairecord.getSnapshot().getNetwork().getCountry().getIso() );
       record.setMetadata( oairecord.getPublishedXML() );
    
       return record;
   }
   
   public List<Record> listRecords(String set, StateHolder state) throws CannotDisseminateFormatException, NoRecordsMatchException
   {
      if(oaiRecordRepository == null)
         throw new IllegalStateException("listRecords() expects a non-null oairecord repository");
         
      final List<Record> records = new ArrayList<Record>();
      
      if ( state.isFirstCall() ) {
    	  
    	  List<Long>    snapshotIdList = new ArrayList<Long>();
    	  List<Integer> totalPageList = new ArrayList<Integer>();
    	  
    	  // Se recorren todas las redes publicadas
    	  for (NationalNetwork network:nationalNetworkRepository.findByPublishedOrderByNameAsc(true)) {
    		  
    		  NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
    		  
    		  if ( snapshot != null ) {
    			  
    			  // obtiene la primera página de cada snapshot 
    			  Page<OAIRecord> page = oaiRecordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
    			  
    			  // agrega los datos del snapshot a la lista
    			  snapshotIdList.add( snapshot.getId() );
    			  totalPageList.add( page.getTotalPages() );
    		  }
    	  }
    	  
    	  // se inicializa el estado
    	  state.initialize(snapshotIdList, totalPageList);
      } 
    
      // obtiene la página actual
      NetworkSnapshot snapshot = networkSnapshotRepository.findOne( state.obtainActualSnapshotID() );
	  Page<OAIRecord> page = oaiRecordRepository.findBySnapshotAndStatus(snapshot, RecordStatus.VALID, new PageRequest(state.obtainActualPage(), PAGE_SIZE));

	  // actualiza el estado
	  state.next();
   
      /**   
      // Dates.
      final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      if(session.getFrom() != null && session.getUntil() != null)
      {
         final String formattedFrom = dateFormat.format(session.getFrom());
         final String formattedUntil = dateFormat.format(session.getUntil());
         query.setQuery(query.getQuery() + " AND lastModifiedDateFacet:[" +  formattedFrom + " TO " + formattedUntil + "]");
      }
      else if(session.getFrom() != null)
      {
         final String formattedFrom = dateFormat.format(session.getFrom());
         query.setQuery(query.getQuery() + " AND lastModifiedDateFacet:[" +  formattedFrom + " TO 99999999999999999]");
      }
      else if(session.getUntil() != null)
      {
         final String formattedUntil = dateFormat.format(session.getUntil());
         query.setQuery(query.getQuery() + " AND lastModifiedDateFacet:[00000000000000000 TO " + formattedUntil + "]");
      }
      */
      
  
      try
      {
         

         if(page.getContent().size() == 0)
            throw new NoRecordsMatchException();

  
         for(OAIRecord oairecord: page.getContent())
         {
            final Record record = new Record();
            
            // Identifier.
            record.setIdentifier( buildIdentifier(oairecord) );
            record.setDate( oairecord.getDatestamp().toString() );
            record.setDeleted(false);
            record.addSet( oairecord.getSnapshot().getNetwork().getCountry().getIso() );
            record.setMetadata( oairecord.getPublishedXML() );
            
            records.add(record);
         }
      }
      finally
      {
         //session.setMemento(memento);
      }
       
      return records;
   }
   
   public byte[] getRecordMetadata(final String identifier, final MetadataFormat metadataFormat) throws IdDoesNotExistException
   {
	   OAIRecord oairecord = loadRecordFromIdentifier(identifier);
       
       return oairecord.getPublishedXML().getBytes() ;
	   
   }
   
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
