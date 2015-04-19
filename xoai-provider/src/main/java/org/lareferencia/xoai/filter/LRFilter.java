/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.filter;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.data.DSpaceItem;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public abstract class LRFilter implements Filter
{
    //public abstract DatabaseFilterResult buildDatabaseQuery(Context context);
    public abstract SolrFilterResult buildSolrQuery();
    public abstract boolean isShown(DSpaceItem item);

    @Override
    public boolean isItemShown(ItemIdentifier item)
    {
        if (item instanceof DSpaceItem)
        {
            return isShown((DSpaceItem) item);
        }
        return false;
    }
}
