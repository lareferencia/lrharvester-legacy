/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;

import org.lareferencia.xoai.services.api.config.ConfigurationService;
import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;
import org.lareferencia.xoai.services.api.xoai.IdentifyResolver;
import org.springframework.beans.factory.annotation.Autowired;

public class LRIdentifyResolver implements IdentifyResolver {
	
  
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ContextService contextService;

    @Override
    public RepositoryConfiguration getIdentify() throws ContextServiceException {
        return new LRRepositoryConfiguration(configurationService, contextService.getContext());
    }
}
