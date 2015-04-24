/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.lareferencia.xoai.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.lareferencia.xoai.solr.exceptions.LRSolrException;
import org.lareferencia.xoai.solr.exceptions.SolrSearchEmptyException;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRSolrSearch
{
    public static SolrDocumentList query(SolrServer server, SolrQuery solrParams)
            throws LRSolrException
    {
        try
        {
            solrParams.addSortField("item.id", ORDER.asc);
            QueryResponse response = server.query(solrParams);
            return response.getResults();
        }
        catch (SolrServerException ex)
        {
            throw new LRSolrException(ex.getMessage(), ex);
        }
    }

    public static SolrDocument querySingle(SolrServer server, SolrQuery solrParams)
            throws SolrSearchEmptyException
    {
        try
        {
            solrParams.addSortField("item.id", ORDER.asc);
            QueryResponse response = server.query(solrParams);
            if (response.getResults().getNumFound() > 0)
                return response.getResults().get(0);
            else
                throw new SolrSearchEmptyException();
        }
        catch (SolrServerException ex)
        {
            throw new SolrSearchEmptyException(ex.getMessage(), ex);
        }
    }
}
