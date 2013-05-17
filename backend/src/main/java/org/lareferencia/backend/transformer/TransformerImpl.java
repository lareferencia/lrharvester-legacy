package org.lareferencia.backend.transformer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.IContentValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
public class TransformerImpl implements ITransformer {
	
	@Autowired
	@Qualifier("driverTypeRule")
	private IContentValidationRule driverRule;
	
	@Autowired
	@Qualifier("statusTypeRule")
	private IContentValidationRule statusRule;
	
	@Autowired
	@Qualifier("langRule")
	private IContentValidationRule langRule;

	private static Map<String, String> statusTypeMapper;

	
	static {
		
		// TODO: CaseInsensitive
		statusTypeMapper = 	new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
		statusTypeMapper.put("draft","info:eu-repo/semantics/draft");
		statusTypeMapper.put("info:eu-repo/semantics/draftVersion","info:eu-repo/semantics/draft");  
		statusTypeMapper.put("accepted","info:eu-repo/semantics/acceptedVersion");
		statusTypeMapper.put("info:eu-repo/semantics/accepted","info:eu-repo/semantics/acceptedVersion");
		statusTypeMapper.put("submitted","info:eu-repo/semantics/submittedVersion");
		statusTypeMapper.put("info:eu-repo/semantics/submitted","info:eu-repo/semantics/submittedVersion");
		statusTypeMapper.put("published","info:eu-repo/semantics/publishedVersion");
		statusTypeMapper.put("info:eu-repo/semantics/published","info:eu-repo/semantics/publishedVersion");
		statusTypeMapper.put("updated","info:eu-repo/semantics/updatedVersion");
		statusTypeMapper.put("info:eu-repo/semantics/updated", "info:eu-repo/semantics/updatedVersion");
	}
	
	
	private static Map<String,String> driverTypeMapper;
	static {
		//TODO CaseInsensitive
		driverTypeMapper = new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
		driverTypeMapper.put("article","info:eu-repo/semantics/article");
		driverTypeMapper.put("artículo de revista","info:eu-repo/semantics/article");
		driverTypeMapper.put("Artículo Internacional","info:eu-repo/semantics/article");
		driverTypeMapper.put("Artículo revisado por pares","info:eu-repo/semantics/article");
		driverTypeMapper.put("artículo","info:eu-repo/semantics/article");
		driverTypeMapper.put("articulo","info:eu-repo/semantics/article");
		driverTypeMapper.put("artigo","info:eu-repo/semantics/article");
		driverTypeMapper.put("dissertação","info:eu-repo/semantics/masterThesis");
		driverTypeMapper.put("doctoralThesis","info:eu-repo/semantics/doctoralThesis");
		driverTypeMapper.put("informe técnico","info:eu-repo/semantics/report");
		driverTypeMapper.put("journal article","info:eu-repo/semantics/article");
		driverTypeMapper.put("masterThesis","info:eu-repo/semantics/masterThesis");
		driverTypeMapper.put("relátorio técnico","info:eu-repo/semantics/report");
		driverTypeMapper.put("tese","info:eu-repo/semantics/doctoralThesis");
		driverTypeMapper.put("tesis de doctorado","info:eu-repo/semantics/doctoralThesis");
		driverTypeMapper.put("tesis de maestría","info:eu-repo/semantics/masterThesis");
		driverTypeMapper.put("tesis de maestria","info:eu-repo/semantics/masterThesis");
		driverTypeMapper.put("tesis doctoral","info:eu-repo/semantics/doctoralThesis");
		driverTypeMapper.put("trabajo de grado","info:eu-repo/semantics/masterThesis");
	}
	
	private static Map<String,String> langMapper;
	static {
		
		langMapper = new HashMap<String, String>();
		langMapper.put("de","deu");
		langMapper.put("en","eng");
		langMapper.put("en_us","eng");
		langMapper.put("es","spa");
		langMapper.put("esp","spa");
		langMapper.put("fr","fra");
		langMapper.put("fre","fra");
		langMapper.put("it","ita");
		langMapper.put("pt","por");
		langMapper.put("Spanish","spa");
	}
	
	
	
	/***
	 * Este transformador harcoded es un preliminar que será mejorado
	 * en futuras iteraciones.
	 */
	@Override
	public void transform(OAIRecordMetadata metadata) {
			
		boolean driverFound = false;
		boolean statusFound = false;
		
		
		ContentValidationResult result;
		
		// Ciclo de búsqueda
		for (Node node: metadata.getFieldNodes("dc:type") ) {
			
			String occr = node.getFirstChild().getNodeValue();
			
			if (!driverFound) {
				result = driverRule.validate( occr );
				driverFound |= result.isValid();
			}
			
			if (!statusFound) {
				result = statusRule.validate( occr );
				statusFound |= result.isValid();		
			}
		}
		
		// ciclo de reemplazo
		if ( !driverFound || !statusFound )
			for (Node node: metadata.getFieldNodes("dc:type") ) {
				
				String occr = node.getFirstChild().getNodeValue();
				
				if (!driverFound && driverTypeMapper.containsKey(occr) ) {
					node.getFirstChild().setNodeValue( driverTypeMapper.get(occr) );
					driverFound = true;
				}
			
				if (!statusFound && statusTypeMapper.containsKey(occr) ) {
						node.getFirstChild().setNodeValue( statusTypeMapper.get(occr) );
						statusFound = true;
				}
			}
		
		// creacion del status en caso de no haber sido encontrado
		if ( !statusFound ) 
			metadata.addFieldOcurrence("dc:type", "info:eu-repo/semantics/publishedVersion");
		
		
		// test del idioma
		boolean langFound = false;
		// Ciclo de búsqueda
		for (Node node: metadata.getFieldNodes("dc:language") ) {
			
			String occr = node.getFirstChild().getNodeValue();
			
			if (!langFound) 
				langFound |= langRule.validate(occr).isValid();
			
		}
		
		// ciclo de reemplazo
		if ( !langFound )
			for (Node node: metadata.getFieldNodes("dc:language") ) {
				
				String occr = node.getFirstChild().getNodeValue();
				
				if ( langMapper.containsKey(occr) ) {
					node.getFirstChild().setNodeValue( langMapper.get(occr) );
					langFound = true;
				}
			}
				

		// creacion del status en caso de no haber sido encontrado
		if ( !langFound ) 
			metadata.addFieldOcurrence("dc:language", "spa");
		
	}
	
	
		static class CaseInsensitiveComparator implements Comparator<String> {
		    public static final CaseInsensitiveComparator INSTANCE = 
		           new CaseInsensitiveComparator();

		    public int compare(String first, String second) {
		         // some null checks
		         return first.compareToIgnoreCase(second);
		    }
		}
		
		
		
}	


