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
package org.lareferencia.backend.indexer;

import java.io.File;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.IOException;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class IndexerImpl implements IIndexer{

	private File stylesheet;
	
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	private static final int PAGE_SIZE = 1000;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	private String solrURL;
	
	public IndexerImpl(String xslFileName, String solrURL) throws IndexerException {
		this.stylesheet = new File(xslFileName);
		this.solrURL = solrURL;	
	}
	
	private Transformer buildTransformer() throws IndexerException {
		
		Transformer trf; 

		try {
		
			StreamSource stylesource = new StreamSource(stylesheet); 
	        trf = xformFactory.newTransformer(stylesource);
			
			trf = MedatadaDOMHelper.buildXSLTTransformer(stylesheet);
			trf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trf.setOutputProperty(OutputKeys.INDENT, "no");
			trf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
		} catch (TransformerConfigurationException e) {
			throw new IndexerException(e.getMessage(), e.getCause());
		} 
		
		return trf;
		
	}
	
	
	/* Este método es syncronized para asegurar que no se superpongan dos indexaciones y los commits solr (not isolated) se produzan*/
	public synchronized boolean index(NetworkSnapshot snapshot) {
		
		 
		 try {	
			// Borrado de los docs del país del snapshot
			
			 MessageDigest md = MessageDigest.getInstance("MD5");
			 String networkAcronym = snapshot.getNetwork().getAcronym();
	
			this.sendUpdateToSolr("<delete><query>network_acronym:" + snapshot.getNetwork().getAcronym() +"</query></delete>");
			
			// Update de los registros de a PAGE_SIZE
			Page<OAIRecord> page = recordRepository.findBySnapshotIdAndStatus(snapshot.getId(), RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();
			
			Long lastId = -1L; // Aquí se guarda el ultimo id de cada página para ser usado en el la query optimizada
			

			for (int i = 0; i < totalPages; i++) {
							
				Transformer trf = buildTransformer();
				trf.setParameter("networkAcronym", networkAcronym);
				trf.setParameter("networkName", snapshot.getNetwork().getName() );
				trf.setParameter("institutionName", snapshot.getNetwork().getInstitutionName() );
						
				//page = recordRepository.findBySnapshotIdAndStatusLimited(snapshot.getId(), RecordStatus.VALID, lastId, new PageRequest(0, PAGE_SIZE) );
				page = recordRepository.findBySnapshotIdAndStatus(snapshot.getId(), RecordStatus.VALID, new PageRequest(i, PAGE_SIZE) );
				
				System.out.println( "Indexando Snapshot: " + snapshot.getId() + " de: " + snapshot.getNetwork().getName() + " página: " + i + " de: " + totalPages);
								
				StringBuffer strBuf = new StringBuffer();
				
				List<OAIRecord> records = page.getContent();

				
				for (OAIRecord record : records) {
					
					System.out.println( record.getPublishedXML() );
					
					OAIRecordMetadata domRecord = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
					StringWriter stringWritter = new StringWriter();
					Result output = new StreamResult(stringWritter);
								
					// id permantente para vufind
					trf.setParameter("vufind_id", networkAcronym + "_" +  DigestUtils.md5Hex(record.getPublishedXML()) );
					// header id para staff
					trf.setParameter("header_id", record.getIdentifier() );
					
					// Se transforma y genera el string del registro
					trf.transform( new DOMSource(domRecord.getDOMDocument()), output);
					strBuf.append(stringWritter.toString());
				}
				
				this.sendUpdateToSolr("<add>" + strBuf.toString()  + "</add>");
				
				trf = null;
				page = null;
				strBuf = null;
				
			}
			
			// commit de los cambios
			this.sendUpdateToSolr("<commit/>");
				 
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.sendUpdateToSolr("<rollback/>");
			} catch (SolrServerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}

		return true;
	}
	
	
	private void sendUpdateToSolr(String data) throws SolrServerException, IOException {
		HttpSolrServer server = new HttpSolrServer(solrURL);
		DirectXmlRequest request = new DirectXmlRequest("/update", data);
		server.request(request);
		server = null;
		//System.out.println(data);
	}
	
	
}
