/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.services.api.ItemRepository;

import org.apache.solr.client.solrj.SolrServerException;

import org.lareferencia.xoai.services.api.cache.XOAIItemCacheService;
import org.lareferencia.xoai.services.api.config.ConfigurationService;

import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;

//import org.lareferencia.xoai.services.api.database.CollectionsService;
//import org.lareferencia.xoai.services.api.database.DatabaseQueryResolver;
//import org.lareferencia.xoai.services.api.database.HandleResolver;

import org.lareferencia.xoai.services.api.solr.SolrQueryResolver;
import org.lareferencia.xoai.services.api.solr.SolrServerResolver;

import org.lareferencia.xoai.services.api.xoai.ItemRepositoryResolver;

import org.springframework.beans.factory.annotation.Autowired;

public class LRItemRepositoryResolver implements ItemRepositoryResolver {
	
    @Autowired
    ContextService contextService;
    @Autowired
    ConfigurationService configurationService;
    @Autowired
    SolrServerResolver solrServerResolver;
    @Autowired
    SolrQueryResolver solrQueryResolver;
    @Autowired
    
//    DatabaseQueryResolver databaseQueryResolver;
//    @Autowired
//   
//    CollectionsService collectionsService;
//    @Autowired
//    
//    private HandleResolver handleResolver;
//    @Autowired
    
    private XOAIItemCacheService cacheService;

    private ItemRepository itemRepository;


    @Override
    public ItemRepository getItemRepository() throws ContextServiceException {
        if (itemRepository == null) {
            //String storage = configurationService.getProperty( "storage");
            //if (storage == null || !storage.trim().toLowerCase().equals("database")) {
                try {
                    itemRepository = new LRItemSolrRepository(solrServerResolver.getServer(), /*, collectionsService, handleResolver, */ solrQueryResolver);
                } catch (SolrServerException e) {
                    throw new ContextServiceException(e.getMessage(), e);
                }
//            } else
//                itemRepository = new DSpaceItemDatabaseRepository(configurationService, collectionsService, handleResolver, cacheService, databaseQueryResolver, contextService);
        }

        return itemRepository;
    }
}
