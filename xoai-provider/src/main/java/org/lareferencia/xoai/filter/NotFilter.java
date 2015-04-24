/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.filter;

import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

public class NotFilter extends LRFilter {
    private LRFilter inFilter;

    public NotFilter(LRFilter inFilter) {
        this.inFilter = inFilter;
    }


//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context) {
//        DatabaseFilterResult result = inFilter.buildDatabaseQuery(context);
//        return new DatabaseFilterResult("NOT ("+result.getQuery()+")", result.getParameters());
//    }

    @Override
    public SolrFilterResult buildSolrQuery() {
        return new SolrFilterResult("NOT("+inFilter.buildSolrQuery()+")");
    }

    @Override
    public boolean isShown(RepostioryItem item) {
        return !inFilter.isShown(item);
    }
}
