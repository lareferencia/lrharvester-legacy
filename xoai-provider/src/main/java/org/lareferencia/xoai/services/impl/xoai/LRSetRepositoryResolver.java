/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.services.api.SetRepository;

import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;
import org.lareferencia.xoai.services.api.solr.SolrServerResolver;
import org.lareferencia.xoai.services.api.xoai.SetRepositoryResolver;
import org.springframework.beans.factory.annotation.Autowired;

public class LRSetRepositoryResolver implements SetRepositoryResolver {
    /*@Autowired
    private ContextService contextService;*/
	
    @Autowired SolrServerResolver solrServerResolver;


    @Override
    public SetRepository getSetRepository()  {
        return new LRSetRepository(solrServerResolver);
    }
}
