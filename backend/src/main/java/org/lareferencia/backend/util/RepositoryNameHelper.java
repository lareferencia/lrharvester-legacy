package org.lareferencia.backend.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.w3c.dom.Node;

public class RepositoryNameHelper {
	
	private  Pattern pattern = null;
	
	public RepositoryNameHelper() {
		try {
			pattern = Pattern.compile(DOMAIN_NAME_PATTERN_STR);
		}
		catch (PatternSyntaxException e) {
			System.err.println("RepositoryNameHelper::Error en el patron: " + DOMAIN_NAME_PATTERN_STR);
			
		}
		
	}
	
	public void setDetectREPattern(String patternString) {
		
		try {
			pattern = Pattern.compile(patternString);
		} catch (PatternSyntaxException e) {
			System.err.println("RepositoryNameHelper::Error en el patron: " + patternString);
			
		}
	}

	public static String UNKNOWN = "No_Detectados"; 

	public static final String DOMAIN_NAME_PATTERN_STR = "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z-]{2,})";
	private static final String NAME_PATTERN_STR = "[A-Za-z0-9-]{4,}";

	public String detectRepositoryDomain(String identifier) {

		String result = UNKNOWN;

		Matcher matcher = pattern.matcher(identifier);

		if (matcher.find())
			result = matcher.group();
		
		return result;
	}
	
	static public String extractNameFromMetadata(OAIRecordMetadata metadata, String fieldname, String prefix) {
		
		String name = UNKNOWN;
		
		for ( Node node: metadata.getFieldNodes(fieldname) ) {
			
			String occr = node.getFirstChild().getNodeValue();

			if ( occr.startsWith(prefix) )
				name = occr.substring(prefix.length()).trim();	
		}
		
		return name;
	}
	
	public void appendNameToMetadata(OAIRecordMetadata metadata, String fieldname, String prefix, String value, Boolean replaceExisting) {
    	
		Node existingNode = null;
		
		for ( Node node: metadata.getFieldNodes(fieldname) ) {
			
			String occr = node.getFirstChild().getNodeValue();

			if ( occr.startsWith(prefix) )	
				existingNode = node;
		}
		
		if ( existingNode != null)
		{	
			if ( replaceExisting ) {
				Node fieldNode = existingNode.getParentNode();
				fieldNode.removeChild(existingNode);
				metadata.addFieldOcurrence(fieldname, prefix + value);
			}
			
		} else {
			metadata.addFieldOcurrence(fieldname, prefix + value);
		}
	}
	
}
