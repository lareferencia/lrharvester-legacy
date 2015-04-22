/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.app;

import org.apache.log4j.Logger;
import org.lareferencia.xoai.services.api.cache.XOAICacheService;
//import org.lareferencia.xoai.services.api.cache.XOAIItemCacheService;
//import org.lareferencia.xoai.services.api.cache.XOAILastCompilationCacheService;
import org.lareferencia.xoai.services.api.cache.XOAIItemCacheService;
import org.lareferencia.xoai.services.api.cache.XOAILastCompilationCacheService;
import org.lareferencia.xoai.services.api.config.ConfigurationService;
import org.lareferencia.xoai.services.api.config.XOAIManagerResolver;
import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.database.EarliestDateResolver;
import org.lareferencia.xoai.services.api.database.FieldResolver;
import org.lareferencia.xoai.services.api.solr.SolrQueryResolver;
import org.lareferencia.xoai.services.api.solr.SolrServerResolver;
import org.lareferencia.xoai.services.api.xoai.IdentifyResolver;
import org.lareferencia.xoai.services.api.xoai.ItemRepositoryResolver;
import org.lareferencia.xoai.services.api.xoai.LRFilterResolver;
import org.lareferencia.xoai.services.api.xoai.SetRepositoryResolver;
import org.lareferencia.xoai.services.impl.cache.LREmptyCacheService;
import org.lareferencia.xoai.services.impl.cache.LRXOAIItemCacheService;
import org.lareferencia.xoai.services.impl.cache.LRXOAILastCompilationCacheService;
import org.lareferencia.xoai.services.impl.config.LRConfigurationService;
import org.lareferencia.xoai.services.impl.context.LRContextService;
import org.lareferencia.xoai.services.impl.context.LRXOAIManagerResolver;
import org.lareferencia.xoai.services.impl.database.DSpaceFieldResolver;
import org.lareferencia.xoai.services.impl.database.LRDummyEarliestDateResolver;
import org.lareferencia.xoai.services.impl.resources.DSpaceResourceResolver;
import org.lareferencia.xoai.services.impl.solr.LRSolrQueryResolver;
import org.lareferencia.xoai.services.impl.solr.LRSolrServerResolver;
import org.lareferencia.xoai.services.impl.xoai.BaseLRFilterResolver;
import org.lareferencia.xoai.services.impl.xoai.LRIdentifyResolver;
import org.lareferencia.xoai.services.impl.xoai.LRItemRepositoryResolver;
import org.lareferencia.xoai.services.impl.xoai.LRSetRepositoryResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

@Configuration
public class BasicConfiguration {
    private static final Logger log = Logger.getLogger(BasicConfiguration.class);

    @Bean
    public ConfigurationService configurationService() {
        return new LRConfigurationService();
    }

    @Bean
    public ContextService contextService() {
        return new LRContextService();
    }


    @Bean
    public SolrServerResolver solrServerResolver () {
        return new LRSolrServerResolver();
    }


    @Bean
    public XOAIManagerResolver xoaiManagerResolver() {
        return new LRXOAIManagerResolver();
    }

    @Bean
    public XOAICacheService xoaiCacheService() {
       /* if (configurationService().getBooleanProperty("oai", "cache.enabled", true)) {
            try {
                return new DSpaceXOAICacheService(xoaiManagerResolver().getManager());
            } catch (XOAIManagerResolverException e) {
                log.error("Not able to start XOAI normal cache service.", e);
                return new DSpaceEmptyCacheService();
            }
        } else*/
            return new LREmptyCacheService();
    }

    
    @Bean
    public XOAILastCompilationCacheService xoaiLastCompilationCacheService () {
        return new LRXOAILastCompilationCacheService();
    }

    @Bean
    public XOAIItemCacheService xoaiItemCacheService () {
        return new LRXOAIItemCacheService();
    }


    @Bean
    public ResourceResolver resourceResolver() {
        return new DSpaceResourceResolver();
    }

    @Bean
    public FieldResolver databaseService () {
        return new DSpaceFieldResolver();
    }

    @Bean
    public EarliestDateResolver earliestDateResolver () {
        return new LRDummyEarliestDateResolver();
    }

    @Bean
    public ItemRepositoryResolver itemRepositoryResolver () {
        return new LRItemRepositoryResolver();
    }
    @Bean
    public SetRepositoryResolver setRepositoryResolver () {
        return new LRSetRepositoryResolver();
    }
    @Bean
    public IdentifyResolver identifyResolver () {
        return new LRIdentifyResolver();
    }

    @Bean
    public LRFilterResolver dSpaceFilterResolver () {
        return new BaseLRFilterResolver();
    }
/*
    @Bean
    public HandleResolver handleResolver () {
        return new DSpaceHandlerResolver();
    }

    @Bean
    public CollectionsService collectionsService () {
        return new DSpaceCollectionsService();
    }*/

    @Bean
    public SolrQueryResolver solrQueryResolver () {
        return new LRSolrQueryResolver();
    }
    
    /*
    @Bean
    public DatabaseQueryResolver databaseQueryResolver () {
        return new DSpaceDatabaseQueryResolver();
    }*/
}
