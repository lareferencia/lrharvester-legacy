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

import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.lareferencia.backend.domain.Network;
import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.domain.RecordStatus;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.repositories.NetworkSnapshotRepository;
import org.lareferencia.backend.repositories.OAIRecordRepository;
import org.lareferencia.backend.util.OAIMetadataXSLTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class Intelligo4Indexer implements IIndexer {

	private static final int PAGE_SIZE = 1000;

	@Autowired
	private OAIRecordRepository recordRepository;

	@Autowired
	private NetworkSnapshotRepository networkSnapshotRepository;

	private String solrURL;
	private HttpSolrServer solrServer;

	private OAIMetadataXSLTransformer metadataTransformer;

	private String xslFileName;
	private String solrNetworkIDField;

	public Intelligo4Indexer(String xslFileName, String solrURL,
			String solrNetworkIDField, String langProfileDirectory)
			throws IndexerException {
		this.solrURL = solrURL;
		this.solrServer = new HttpSolrServer(solrURL);
		this.xslFileName = xslFileName;
		this.solrNetworkIDField = solrNetworkIDField;

		try {
			DetectorFactory.loadProfile(langProfileDirectory);
		} catch (LangDetectException e) {
			System.err
					.println("!!!! Error al cargar los perfiles del detector de idiomas en: "
							+ langProfileDirectory);
		}
	}

	/*
	 * Este método es syncronized para asegurar que no se superpongan dos
	 * indexaciones y los commits solr (not isolated) se produzan
	 */
	public synchronized boolean index(Network network,
			NetworkSnapshot snapshot, boolean deleteOnly) {

		boolean valid = false;

		// Primero borra la red del índice
		valid |= delete(network);

		// Una vez borrada y si no es una acción de borrado únicamente, indexa
		// el LGK Snapshot
		if (!deleteOnly)
			valid |= index(snapshot);

		return valid;
	}

	private boolean index(NetworkSnapshot snapshot) {

		try {

			// Acrónimo
			String networkAcronym = snapshot.getNetwork().getAcronym();

			// Update de los registros de a PAGE_SIZE
			Page<OAIRecord> page = recordRepository.findBySnapshotIdAndStatus(
					snapshot.getId(), RecordStatus.VALID, new PageRequest(0,
							PAGE_SIZE));
			int totalPages = page.getTotalPages();

			metadataTransformer = new OAIMetadataXSLTransformer(xslFileName);
			metadataTransformer.setParameter("networkAcronym", networkAcronym);
			metadataTransformer.setParameter("networkName", snapshot
					.getNetwork().getName());
			metadataTransformer.setParameter("institutionName", snapshot
					.getNetwork().getInstitutionName());

			Long lastRecordID = -1L;

			for (int i = 0; i < totalPages; i++) {

				System.out.println("Indexando Snapshot: " + snapshot.getId()
						+ " de: " + snapshot.getNetwork().getName()
						+ " página: " + (i + 1) + " de: " + totalPages);

				/**
				 * Este pedido paginado pide siempre la primera página
				 * restringida a que el id sea mayor al ultimo anterior
				 **/
				page = recordRepository.findBySnapshotIdAndStatusOptimized(
						snapshot.getId(), RecordStatus.VALID, lastRecordID,
						new PageRequest(0, PAGE_SIZE));
				List<OAIRecord> records = page.getContent();

				StringBuffer stringBuffer = new StringBuffer();

				for (OAIRecord record : records) {

					OAIRecordMetadata metadata = new OAIRecordMetadata(
							record.getIdentifier(), record.getPublishedXML());

					// ///// DC:DESCRIPTION - Detección y división de idiomas
					String ab_es = "";
					String ab_en = "";
					String ab_pt = "";
					for (String ab : metadata
							.getFieldOcurrences("dc:description")) {
						String lang = detectLang(ab);
						switch (lang) {
						case "es":
							ab_es += ab;
							break;
						case "en":
							ab_en += ab;
							break;
						case "pt":
							ab_pt += ab;
							break;
						}
					}

					metadataTransformer.setParameter("ab_es", ab_es);
					metadataTransformer.setParameter("ab_en", ab_en);
					metadataTransformer.setParameter("ab_pt", ab_pt);
					// /////////////////////////////////////////////////////////

					// ///// DC:title - Detección y división de idiomas
					String ti_es = "";
					String ti_en = "";
					String ti_pt = "";
					for (String ti : metadata.getFieldOcurrences("dc:title")) {
						String lang = detectLang(ti);
						switch (lang) {
						case "es":
							ti_es += ti;
							break;
						case "en":
							ti_en += ti;
							break;
						case "pt":
							ti_pt += ti;
							break;
						}
					}
					metadataTransformer.setParameter("ti_es", ti_es);
					metadataTransformer.setParameter("ti_en", ti_en);
					metadataTransformer.setParameter("ti_pt", ti_pt);
					// ///////////////////////////////////////////////////////////

					// fingerprint del registro
					metadataTransformer.setParameter("vufind_id",
							record.getFingerprint());
					// TODO: Creo que sería bueno cambiar el nombre de vufind_id
					// por fingerprint y modificarlo en el xsl

					// identifier del record
					metadataTransformer.setParameter("header_id",
							record.getIdentifier());

					// metadata como string
					metadataTransformer.setParameter("record_id", record
							.getId().toString());

					// Se transforma y genera el string del registro
					stringBuffer
							.append(metadataTransformer.transform(metadata));

					lastRecordID = record.getId();
				}

				try {
					this.sendUpdateToSolr("<add>" + stringBuffer.toString()
							+ "</add>");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				page = null;

			}

			// commit de los cambios
			this.sendUpdateToSolr("<commit/>");

		} catch (TransformerConfigurationException e) {
			System.err.println("Problemas en la carga del transformador xsl:"
					+ xslFileName);
			solrRollback();
			return false;
		} catch (TransformerException e) {
			System.err.println("Problemas en el proceso de transformación xsl:"
					+ xslFileName);
			solrRollback();
			return false;
		} catch (SolrServerException e) {
			System.err.println("Problemas en el proceso de envío a Intelligo:"
					+ solrURL);
			solrRollback();
			return false;
		} catch (IOException e) {
			System.err
					.println("Problemas en el proceso de envío a Intelligo - E/S:"
							+ solrURL);
			solrRollback();
			return false;
		} catch (Exception e) {
			System.err.println("Problemas en indexación - Indeterminado");
			solrRollback();
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean delete(Network network) {

		try {

			// Borrado de la red
			String networkAcronym = network.getAcronym();
			this.sendUpdateToSolr("<delete><query>" + this.solrNetworkIDField
					+ ":" + networkAcronym + "</query></delete>");

			// commit de los cambios
			this.sendUpdateToSolr("<commit/>");

		} catch (SolrServerException e) {
			System.err
					.println("Problemas en el proceso de borrado de red en Intelligo:"
							+ solrURL);
			solrRollback();
			return false;
		} catch (IOException e) {
			System.err
					.println("Problemas en el proceso de envío a Intelligo - E/S:"
							+ solrURL);
			solrRollback();
			return false;
		} catch (Exception e) {
			System.err
					.println("Problemas en borrado durante indexación - Indeterminado");
			solrRollback();
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void solrRollback() {
		try {
			this.sendUpdateToSolr("<rollback/>");
		} catch (SolrServerException e) {
			System.err
					.println("Problemas en rollback Intelligo - Indexer - SolrServer");
		} catch (IOException e) {
			System.err
					.println("Problemas en rollback Intelligo - Indexer - Error E/S");
		}
	}

	private void sendUpdateToSolr(String data) throws SolrServerException,
			IOException {
		DirectXmlRequest request = new DirectXmlRequest("/update", data);
		solrServer.request(request);
	}

	private String detectLang(String text) {

		Detector detector;
		try {
			detector = DetectorFactory.create();
			detector.append(text);
			return detector.detect();

		} catch (LangDetectException e) {
			if (text != null)
				System.err.println("Error creando detector de idioma: " + text);
			return "00";
		}

	}

}
