package org.lareferencia.backend.harvester;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.OAIOrigin;
import org.lareferencia.backend.domain.OAISet;

import ORG.oclc.oai.harvester2.verb.ListRecords;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@Component
@Scope(value="prototype")
public class OCLCBasedHarvesterImpl extends BaseHarvestingEventSource implements IHarvester {
	
	int MAX_RETRIES = 3;
	
	public OCLCBasedHarvesterImpl() {
		super();
		System.out.println("Creando Harvester" + this.toString());	
	}
		
	@Override
	public void harvest(NationalNetwork network) {
		
		// Se recorren los orígenes 
		for ( OAIOrigin origin:network.getOrigins() ) {
			
			// si tiene sets declarados solo va a cosechar 
			if ( origin.getSets().size() > 0 ) {
			
				for ( OAISet set: origin.getSets() ) {
					harvest(origin.getUri(), set.getSpec(), origin.getMetadataPrefix());
				}
			}
			// si no hay set declarado cosecha todo
			else {
				harvest(origin.getUri(), null, origin.getMetadataPrefix());
			}
				
			
		}	
	}
	
	
	
	private void harvest(String uri, String setname, String metadataPrefix) {	
		
		ListRecords actualListRecords = null;
		String resumptionToken = null;
		
		int batchIndex = 0;
		int actualRetry = 0;
		int secondsToNextRetry = 5;
		
		
		// La condición es que sea la primera corrida o que no sea null el resumption (caso de fin)
		// TODO: Hay casos donde dio null y no era el fin, estudiar alternativas
		while (batchIndex == 0 || resumptionToken != null) {
				
			do {
				try {
					actualListRecords = listRecords(uri, setname, metadataPrefix, batchIndex, resumptionToken);
					resumptionToken = actualListRecords.getResumptionToken();
		
					fireHarvestingEvent( new HarvestingEvent(actualListRecords.toString(), HarvestingResult.OK) );
					
					batchIndex++;
					actualRetry = 0;
					secondsToNextRetry = 5;
					break;
				} catch (HarvestingException | NoSuchFieldException | TransformerException e) {
					System.out.println("Problemas en el lote: " + batchIndex + " reintento: " + actualRetry);
					System.out.println( e.getMessage() );
					
					System.out.print("Esperando " + secondsToNextRetry + " segundos para el proximo reintento ..");
					try {
						Thread.sleep(secondsToNextRetry*1000);
					} catch (InterruptedException t) {}
					System.out.println("OK");
					
					// Se incrementa el retry y se duplica el tiempo de espera
					actualRetry++;
					secondsToNextRetry = secondsToNextRetry*2;
				}
			} while ( actualRetry < MAX_RETRIES );
					
		}
	}
	
	
	private ListRecords listRecords(String baseURL, String setSpec, String metadataPrefix, int batchIndex, String resumptionToken ) 
			throws HarvestingException {
		
		ListRecords actualListRecords = null;
		/*Se encapsulan las dos llamadas distintas en una sola, que depende de la existencia del RT */
		try {
		
		    if (batchIndex == 0)
				actualListRecords = new  ListRecords(baseURL, null, null, setSpec, metadataPrefix);
			else
		    	actualListRecords = new ListRecords(baseURL, resumptionToken);
		    
		    NodeList errors = actualListRecords .getErrors();
			
			if (errors != null && errors.getLength() > 0) {
				throw new HarvestingException( actualListRecords.toString() );
			} else {
				resumptionToken = actualListRecords.getResumptionToken();
				if (resumptionToken != null && resumptionToken.length() == 0) 
					resumptionToken = null;
			}
		    
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
			throw new HarvestingException( e.getMessage() );
		}   
		
		return actualListRecords;
	}




	

}
