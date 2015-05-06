/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import com.lyncode.xoai.dataprovider.core.ReferenceSet;

/**
 *
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRSetSpecFilter extends LRFilter
{
    private static Logger log = LogManager.getLogger(LRSetSpecFilter.class);

    private String setSpec;
//    private HandleResolver handleResolver;
//    private CollectionsService collectionsService;

    public LRSetSpecFilter(/*CollectionsService collectionsService, HandleResolver handleResolver, */String spec)
    {
//        this.collectionsService = collectionsService;
//        this.handleResolver = handleResolver;
        this.setSpec = spec;
    }

//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context)
//    {
//        if (setSpec.startsWith("col_"))
//        {
//            try
//            {
//                DSpaceObject dso = handleResolver.resolve(setSpec.replace("col_", ""));
//                return new DatabaseFilterResult(
//                        "EXISTS (SELECT tmp.* FROM collection2item tmp WHERE tmp.resource_id=i.item_id AND collection_id = ?)",
//                        dso.getID());
//            }
//            catch (Exception ex)
//            {
//                log.error(ex.getMessage(), ex);
//            }
//        }
//        else if (setSpec.startsWith("com_"))
//        {
//            try
//            {
//                DSpaceObject dso = handleResolver.resolve(setSpec.replace("com_", ""));
//                List<Integer> list = collectionsService.getAllSubCollections(dso.getID());
//                String subCollections = StringUtils.join(list.iterator(), ",");
//                return new DatabaseFilterResult(
//                        "EXISTS (SELECT tmp.* FROM collection2item tmp WHERE tmp.resource_id=i.item_id AND collection_id IN ("
//                                + subCollections + "))");
//            }
//            catch (Exception e)
//            {
//                log.error(e.getMessage(), e);
//            }
//        }
//        return new DatabaseFilterResult();
//    }

    @Override
    public boolean isShown(RepostioryItem item)
    {
        for (ReferenceSet s : item.getSets())
            if (s.getSetSpec().equals(setSpec))
                return true;
        return false;
    }

    @Override
    public SolrFilterResult buildSolrQuery()
    {
        
        //FIXME: Aqui hay que decidir como tratar los sets  
    	if (setSpec.startsWith("col_"))
        {
            try
            {
                return new SolrFilterResult("item.collections:"
                        + ClientUtils.escapeQueryChars(setSpec));
            }
            catch (Exception ex)
            {
                log.error(ex.getMessage(), ex);
            }
        }
        else if (setSpec.startsWith("com_"))
        {
            try
            {
                return new SolrFilterResult("item.communities:"
                        + ClientUtils.escapeQueryChars(setSpec));
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        else { // sin prefijo se mapea contra item.collections
        	return new SolrFilterResult("item.collections:"
                    + ClientUtils.escapeQueryChars(setSpec));
        }
        
        return new SolrFilterResult();
    }

}
