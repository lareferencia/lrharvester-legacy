package org.lareferencia.backend.harvester;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ORG.oclc.oai.harvester2.verb.ListRecords;

@Component
@Scope(value = "prototype")
public class OCLCBasedHarvesterImpl extends BaseHarvestingEventSource implements
		IHarvester {

	private static final int STANDARD_RECORD_SIZE = 100;
	private static final String METADATA_NODE_NAME = "metadata";
	int MAX_RETRIES = 3;
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	public OCLCBasedHarvesterImpl() {
		super();
		System.out.println("Creando Harvester: " + this.toString());
	}

	public void harvest(String uri, String from, String until, String setname,
			String metadataPrefix) {

		ListRecords actualListRecords = null;
		String resumptionToken = null;

		int batchIndex = 0;
		int actualRetry = 0;
		int secondsToNextRetry = 5;

		// La condición es que sea la primera corrida o que no sea null el
		// resumption (caso de fin)
		// TODO: Hay casos donde dio null y no era el fin, estudiar alternativas
		while ( batchIndex == 0 || (resumptionToken.trim().length() != 0 ) ) {

			do {
				try {
					
					System.out.println( "Request:" + resumptionToken);
					actualListRecords = listRecords(uri, setname, metadataPrefix, batchIndex, resumptionToken);
					resumptionToken = actualListRecords.getResumptionToken();
					
					/**
					 * TODO: Baja prioridad. Dado que dos llamadas no se superponen puede reutilizarse el mismo evento dentro
					 * de esta función
					 */
					fireHarvestingEvent(new HarvestingEvent(
							parseRecords(actualListRecords),
							HarvestingEventStatus.OK));

					batchIndex++;
					actualRetry = 0;
					secondsToNextRetry = 5;
					break;

				} catch (HarvestingException | TransformerException | NoSuchFieldException e) {
					
					String message = buildErrorMessage(e, batchIndex, actualRetry);
					message += "Esperando " + secondsToNextRetry + " segundos para el próximo reintento ..";
					
					fireHarvestingEvent( new HarvestingEvent(message, HarvestingEventStatus.ERROR_RETRY) );
						
					// Una espera de secondsToNextRetry
					try { Thread.sleep(secondsToNextRetry * 1000); } catch (InterruptedException t) {}
						
					// Se incrementa el retry y se duplica el tiempo de espera
					actualRetry++;
					secondsToNextRetry = secondsToNextRetry * 2;
				}
				
			} while (actualRetry < MAX_RETRIES);
			
			if ( actualRetry == MAX_RETRIES ) {
				String message = "Número de reintentos máximos alcanzados.  Abortando proceso de cosecha.";
				fireHarvestingEvent( new HarvestingEvent(message, HarvestingEventStatus.ERROR_FATAL) );
				break;
			}

		}
	}
	
	private String buildErrorMessage(Exception e, int batchIndex, int actualRetry) {
		String message = "Error en lote: " + batchIndex + " reintento: " + actualRetry + "\n";
		message += "Detalles del error:\n";
		message += e.getMessage();
		message += "Fin detalles:\n";
		return message;
	}

	private ListRecords listRecords(String baseURL, String setSpec,
			String metadataPrefix, int batchIndex, String resumptionToken)
			throws HarvestingException {

		ListRecords listRecords = null;
		/*
		 * Se encapsulan las dos llamadas distintas en una sola, que depende de
		 * la existencia del RT
		 */
		try {

			if (batchIndex == 0)
				listRecords = new ListRecords(baseURL, null, null, setSpec,
						metadataPrefix);
			else
				listRecords = new ListRecords(baseURL, resumptionToken);

			NodeList errors = listRecords.getErrors();

			if (errors != null && errors.getLength() > 0) {
				throw new HarvestingException(listRecords.toString());
			} else {
				resumptionToken = listRecords.getResumptionToken();
				if (resumptionToken != null && resumptionToken.length() == 0)
					resumptionToken = null;
			}
		//TODO: Deben reordenarse el lanzamiento y conversión de exceptions
		} catch (IOException e) {
			throw new HarvestingException(e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new HarvestingException(e.getMessage());
		} catch (SAXException e) {
			throw new HarvestingException(e.getMessage());
		} catch (TransformerException e) {
			throw new HarvestingException(e.getMessage());
		} catch (NoSuchFieldException e) {
			throw new HarvestingException(e.getMessage());
		} catch (Exception e) {
			throw new HarvestingException(e.getMessage());
		}

		return listRecords;
	}

	private Map<String,Node> parseRecords(ListRecords listRecords) throws TransformerException, NoSuchFieldException {
		
		Map<String,Node> result = new HashMap<String,Node>(STANDARD_RECORD_SIZE);
		/**
		 * TODO: Podrían usarse una lista fija de registros, no persistentes para no crear siempre los
		 * objetos de registro, habría que evaluarlo cuidadosamente
		 */
		
		
		// La obtención de registros por xpath se realiza de acuerdo al schema correspondiente
		NodeList nodes = null;
		String namespace = null;
		
		if (listRecords.getSchemaLocation().indexOf(ListRecords.SCHEMA_LOCATION_V2_0) != -1) {
			nodes = listRecords.getNodeList("/oai20:OAI-PMH/oai20:ListRecords/oai20:record");
			namespace = "oai20";
		} else if (listRecords.getSchemaLocation().indexOf(ListRecords.SCHEMA_LOCATION_V1_1_LIST_RECORDS) != -1) {
			namespace = "oai11_ListRecords";
			nodes = listRecords.getNodeList("/oai11_ListRecords:ListRecords/oai11_ListRecords:record");
		} else {
			throw new NoSuchFieldException(listRecords.getSchemaLocation());
		}
		
		//System.out.println( listRecords.toString() );
				
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);			
			String identifier = listRecords.getSingleString(node, namespace + ":header/" + namespace + ":identifier");						
			result.put(identifier, getMetadataNode(node));				
		}		
		
		return result;
	}
	
	/**
	 * @param node
	 * @return 
	 * @throws TransformerException
	 * @throws NoSuchFieldException 
	 */
	private Node getMetadataNode(Node node) throws TransformerException, NoSuchFieldException {
		
		
		/**
		 *  TODO: búsqueda secuencial, puede ser ineficiente pero xpath no esta implementado sobre nodos individaules
		 *  en la interfaz listRecords, en necesario construir un DomHelper para Harvester, es sencillo dada la clase
		 *  base BaseMetadataDOMHelper
		 */ 
		
		NodeList childs = node.getChildNodes();
		Node metadataNode = null;
		for (int i=0; i < childs.getLength(); i++)
			if ( childs.item(i).getNodeName().equals(METADATA_NODE_NAME) )  
				metadataNode = childs.item(i);

		if (metadataNode == null) 
			throw new NoSuchFieldException(METADATA_NODE_NAME);
		
		
		return metadataNode;
	}
}
