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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class IntelligoIndexer implements IIndexer{

	
	
	private boolean delete(Network network) {
		// TODO Auto-generated method stub
		return false;
	}


	private File stylesheet;
	private String outputPath;
	
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	private static final int PAGE_SIZE = 500;
	
	@Autowired
	private OAIRecordRepository recordRepository;
	
	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;
	
	
	public IntelligoIndexer(String xslFileName, String langProfileDirectory, String outputPath) throws IndexerException {
		this.stylesheet = new File(xslFileName);
		this.outputPath = outputPath;
		
		/*
		try {
			DetectorFactory.loadProfile(langProfileDirectory);
		} catch (LangDetectException e) {
			System.err.println("!!!! Error al cargar los perfiles del detector de idiomas en: " + langProfileDirectory);
		}*/

	}
	
	
	
	
	/* Este método es syncronized para asegurar que no se superpongan dos indexaciones y los commits solr (not isolated) se produzan*/
	public synchronized boolean index(Network network, NetworkSnapshot snapshot_, boolean deleteOnly) {
		
		 
		 try {	
			// Borrado de los docs del país del snapshot
			 
				// Obtiene el snapshot LGK de la red parámetro
				NetworkSnapshot snapshot = networkSnapshotRepository.findLastGoodKnowByNetworkID(network.getId());
				
			 
				
			Page<OAIRecord> page = recordRepository.findBySnapshotIdAndStatus(snapshot.getId(), RecordStatus.VALID, new PageRequest(0, PAGE_SIZE));
			int totalPages = page.getTotalPages();
						

			for (int i = 0; i < totalPages; i++) {
							
				//Transformer trf = buildTransformer();
				
				//trf.setParameter("country", snapshot.getNetwork().getName() );
				
				page = recordRepository.findBySnapshotIdAndStatus(snapshot.getId(), RecordStatus.VALID, new PageRequest(i, PAGE_SIZE) );
				
				System.out.println( "Indexando Snapshot: " + snapshot.getId() + " de: " + snapshot.getNetwork().getName() + " página: " + i + " de: " + totalPages);
								
				StringBuffer strBuf = new StringBuffer();
				List<OAIRecord> records = page.getContent();

					strBuf.append("<add>");

					for (OAIRecord record : records) {
						
						OAIRecordMetadata metadata = new OAIRecordMetadata(record.getIdentifier(), record.getPublishedXML() );
						StringWriter stringWritter = new StringWriter();
						Result output = new StreamResult(stringWritter);
									
						// id unico pero mutable para solr
						//trf.setParameter("solr_id",  record.getId().toString()  );
						
						/////// DC:DESCRIPTION - Detección y división de idiomas 
						String ab_es = "";
						String ab_en = "";
						String ab_pt = "";
						for ( String ab : metadata.getFieldOcurrences("dc:description") ) {	
							String lang = detectLang(ab);				
							switch (lang) {
								case "es": ab_es += ab; break;
								case "en": ab_en += ab; break;
								case "pt": ab_pt += ab; break;		
							}
						}	
//						trf.setParameter("ab_es",  ab_es);
//						trf.setParameter("ab_en",  ab_en);
//						trf.setParameter("ab_pt",  ab_pt);
						/////////////////////////////////////////////////////////////
						
						/////// DC:title - Detección y división de idiomas 
						String ti_es = "";
						String ti_en = "";
						String ti_pt = "";
						for ( String ti : metadata.getFieldOcurrences("dc:title") ) {	
							String lang = detectLang(ti);				
							switch (lang) {
								case "es": ti_es += ti; break;
								case "en": ti_en += ti; break;
								case "pt": ti_pt += ti; break;		
							}
						}	
//						trf.setParameter("ti_es",  ti_es);
//						trf.setParameter("ti_en",  ti_en);
//						trf.setParameter("ti_pt",  ti_pt);
						/////////////////////////////////////////////////////////////
						
						
						// Se transforma y genera el string del registro
//						trf.transform( new DOMSource(metadata.getDOMDocument()), output);
						strBuf.append(stringWritter.toString());						
					}
				
					strBuf.append("</add>");
					
			        BufferedWriter out = new BufferedWriter(new FileWriter(outputPath + "/" + snapshot.getId() + "_" + i + ".solr.xml"));
			        out.write( strBuf.toString() );
			        out.close();
			}
				 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	private String detectLang(String text) {
		
		Detector detector;
		try {
			detector = DetectorFactory.create();
			detector.append(text);
			return detector.detect();
			
		} catch (LangDetectException e) {
			System.err.print("Error creando detector de idioma.");
			return "00";
		}
	
	}
	
}
