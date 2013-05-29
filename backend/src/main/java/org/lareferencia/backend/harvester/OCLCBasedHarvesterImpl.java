package org.lareferencia.backend.harvester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata.OAIRecordMetadataParseException;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
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
	private static int MAX_RETRIES = 15;
	private static int INITIAL_SECONDS_TO_RETRY = 3;
	private static int RETRY_FACTOR = 2;

	
	private static TransformerFactory xformFactory = TransformerFactory.newInstance();

	public OCLCBasedHarvesterImpl() {
		super();
		System.out.println("Creando Harvester: " + this.toString());
	}

	public void harvest(String uri, String from, String until, String setname,
			String metadataPrefix, String resumptionToken) {

		ListRecords actualListRecords = null;

		int batchIndex = 0;
		int actualRetry = 0;
		int secondsToNextRetry = INITIAL_SECONDS_TO_RETRY;

		// La condición es que sea la primera corrida o que no sea null el
		// resumption (caso de fin)
		// TODO: Hay casos donde dio null y no era el fin, estudiar alternativas
		while ( batchIndex == 0 || (resumptionToken.trim().length() != 0 ) ) {

			do {
				try {
					
					System.out.println( "Request:" + resumptionToken);
					actualListRecords = listRecords(uri, setname, metadataPrefix, batchIndex, resumptionToken);
					resumptionToken = actualListRecords.getResumptionToken();
					
					// se crea un evento a partir del resultado de listRecords
					HarvestingEvent event = createResultFromListRecords(actualListRecords);
					event.setStatus(HarvestingEventStatus.OK);
					event.setResumptionToken(resumptionToken);
					
					// se lanza el evento
					fireHarvestingEvent(event);

					batchIndex++;
					actualRetry = 0;
					secondsToNextRetry = INITIAL_SECONDS_TO_RETRY;
					break;

				}
				catch (Exception e) {
				//TODO: Esto es compatible solo con 1.7
				//} catch (HarvestingException | TransformerException | NoSuchFieldException e) {
					
					String message = buildErrorMessage(e, batchIndex, actualRetry);
					message += "Esperando " + secondsToNextRetry + " segundos para el próximo reintento ..";
					
					fireHarvestingEvent( new HarvestingEvent(message, HarvestingEventStatus.ERROR_RETRY) );
						
					// Una espera de secondsToNextRetry
					try { Thread.sleep(secondsToNextRetry * 1000); } catch (InterruptedException t) {}
						
					// Se incrementa el retry y se duplica el tiempo de espera
					actualRetry++;
					secondsToNextRetry = secondsToNextRetry * RETRY_FACTOR;
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

	private HarvestingEvent createResultFromListRecords(ListRecords listRecords) throws TransformerException, NoSuchFieldException {
		
		
		HarvestingEvent result = new HarvestingEvent();
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
			
			
			String identifier = "unknown";
			String metadataString = "unknown";
				
				
			try {
				identifier = listRecords.getSingleString(nodes.item(i), namespace + ":header/" + namespace + ":identifier");						
				metadataString = getMetadataString(nodes.item(i));
		
				//TODO: Hay que tratar aparte los casos deleted que pueden generar exceptions al no tener metadata
				result.getRecords().add( new OAIRecordMetadata(identifier,  metadataString ));	
				
				
			} catch (OAIRecordMetadataParseException e){
				//TODO: Hay que poder informar estas exceptions individuales para que quede registrada la pérdida del registro
				System.err.println("Error en el parseo de registro: " + identifier + '\n'+ metadataString );
				result.setRecordMissing(true);
			} catch (Exception e) {
				System.err.println("Error desconocido procesando el registro: " + identifier + '\n'+ metadataString );
				result.setRecordMissing(true);			
			}
		}		
		
		return result;
	}
	
	/**
	 * @param node
	 * @return 
	 * @throws TransformerException
	 * @throws NoSuchFieldException 
	 */
	private String getMetadataString(Node node) throws TransformerException, NoSuchFieldException {		
		
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
			throw new NoSuchFieldException( "No existe el nodo: " + METADATA_NODE_NAME + " en la respuesta.\n" +  MedatadaDOMHelper.Node2XMLString(node));
		
		// TODO: Ver el tema del char &#56256;
		return MedatadaDOMHelper.Node2XMLString( metadataNode );
	}
}
