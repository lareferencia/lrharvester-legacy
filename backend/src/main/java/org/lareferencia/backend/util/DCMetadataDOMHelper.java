package org.lareferencia.backend.util;

import org.apache.xml.utils.DefaultErrorHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DCMetadataDOMHelper extends BaseMedatadaDOMHelper {
	
	public DCMetadataDOMHelper() {
		
		super();
		
		
		DOMImplementation impl = builder.getDOMImplementation();
        Document namespaceHolder = impl.createDocument("http://www.openarchives.org/OAI/2.0/oai_dc","oaidc:namespaceHolder", null);
        
        Element namespaceElement = namespaceHolder.getDocumentElement();
        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:oai20", "http://www.openarchives.org/OAI/2.0/");
        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
        namespaceElement.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:dc", "http://purl.org/dc/elements/1.1/");

        
        
        super.setNamespaceElement(namespaceElement);
        	
	}

}
