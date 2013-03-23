/**
 * @author Lautaro Matas (lmatas@gmail.com)
 */

package org.oclc.oai.harvester2.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.oclc.oai.harvester2.verb.ListRecords;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LaReferenciaProtoHarvester {
	
	String baseURL;
	String from = null;
	String until = null;
	String setSpec = null;
	String metadataPrefix = "oai_dc";
	String resumptionToken = null;
	
	int batchIndex = 0;	
	int MAX_RETRIES = 3;
	int secondsToNextRetry = 10;
	
	int actualRetry = 0;
	ListRecords actualListRecords = null;
	
	public LaReferenciaProtoHarvester(String baseURL) {
		this.baseURL = baseURL;
		this.resumptionToken = null;
	}
	
	public LaReferenciaProtoHarvester(String baseURL, String resumptionToken) {
		this.baseURL = baseURL;
		this.resumptionToken = resumptionToken;
	}

	public ListRecords listRecords() throws HarvestingException {
		/*Se encapsulan las dos llamadas distintas en una sola, que depende de la existencia del RT */
		try {
		
		    if (batchIndex == 0 && resumptionToken == null)
				actualListRecords = new ListRecords(baseURL, from, until, setSpec, metadataPrefix);
			else
		    	actualListRecords = new ListRecords(baseURL, resumptionToken);
		    
		    NodeList errors = actualListRecords.getErrors();
			
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

	private void writeRecordsToFile(String filename) {
		
		// escribe los registros a un archivo
		try {
			OutputStream out;
			out = new FileOutputStream(filename);
			out.write(actualListRecords.toString().getBytes("UTF-8"));
			out.write("\n".getBytes("UTF-8"));
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println( e.getMessage() );
		} catch (UnsupportedEncodingException e) {
			System.out.println( e.getMessage() );
		} catch (IOException e) {
			System.out.println( e.getMessage() );
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
	}
	
	
	private void reinitializeRetryState() {	
		actualRetry = 0;
		secondsToNextRetry = 5;
	}
	
	private void harvest() {	
				
		try {
			actualListRecords = listRecords();
			writeRecordsToFile(resumptionToken + ".xml");
			batchIndex++;
			
		} catch (HarvestingException e) {
			System.out.println("Falla en la primera llamada a ListRecords\n\n");
			System.out.println( e.getMessage() );
			System.exit(1);
		} 	
		
		while (resumptionToken != null) {
				
			do {
				try {
					actualListRecords = listRecords();
					writeRecordsToFile(resumptionToken + ".xml");
					batchIndex++;
					reinitializeRetryState();
					break;
				} catch (HarvestingException e) {
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
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length == 1)
			new LaReferenciaProtoHarvester(args[0]).harvest();
		else if (args.length == 2)
			new LaReferenciaProtoHarvester(args[0], args[1]).harvest();
		else
			System.out.println("Harverster uri [resumptionToken]");

	}
	
	
	public class HarvestingException extends Exception {
		 /**
		 *  Esta clase temporalmente unifica las excepciones generadas
		 */
		private static final long serialVersionUID = -5913401095836497654L;
		public HarvestingException() { super(); }
		  public HarvestingException(String message) { super(message); }
		  public HarvestingException(String message, Throwable cause) { super(message, cause); }
		  public HarvestingException(Throwable cause) { super(cause); }
		}

}
