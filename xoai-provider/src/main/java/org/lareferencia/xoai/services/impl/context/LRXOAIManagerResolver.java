/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.context;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

import org.lareferencia.xoai.services.api.config.XOAIManagerResolver;
import org.lareferencia.xoai.services.api.config.XOAIManagerResolverException;

import org.lareferencia.xoai.services.api.xoai.LRFilterResolver;

import org.springframework.beans.factory.annotation.Autowired;

import static com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.readConfiguration;

public class LRXOAIManagerResolver implements XOAIManagerResolver {
    public static final String XOAI_CONFIGURATION_FILE = "xoai.xml";
    @Autowired ResourceResolver resourceResolver;
    @Autowired LRFilterResolver filterResolver;

    private XOAIManager manager;

    @Override
    public XOAIManager getManager() throws XOAIManagerResolverException {
        if (manager == null) {
            try {
                manager = new XOAIManager(filterResolver, resourceResolver, readConfiguration(resourceResolver.getResource(XOAI_CONFIGURATION_FILE)));
            } catch (Exception e) {
                throw new XOAIManagerResolverException(e);
            }
        }
        return manager;
    }
}
