/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.solr;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import org.lareferencia.xoai.services.api.config.ConfigurationService;
import org.lareferencia.xoai.services.api.solr.SolrServerResolver;

import org.springframework.beans.factory.annotation.Autowired;

public class LRSolrServerResolver implements SolrServerResolver {
    private static Logger log = LogManager.getLogger(LRSolrServerResolver.class);
    private static SolrServer server = null;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public SolrServer getServer() throws SolrServerException
    {
        if (server == null)
        {
            try
            {
            	
                server = new HttpSolrServer(configurationService.getProperty("solr.url"));

                //server = new HttpSolrServer(configurationService.getProperty( "solr.url"));
                log.debug("Solr Server Initialized");
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return server;
    }
}
