/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.filter;

import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import java.util.ArrayList;
import java.util.List;

public class OrFilter extends LRFilter {
    private LRFilter left;
    private LRFilter right;

    public OrFilter(LRFilter left, LRFilter right) {
        this.left = left;
        this.right = right;
    }

//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context) {
//        DatabaseFilterResult leftResult = left.buildDatabaseQuery(context);
//        DatabaseFilterResult rightResult = right.buildDatabaseQuery(context);
//        List<Object> param = new ArrayList<Object>();
//        param.addAll(leftResult.getParameters());
//        param.addAll(rightResult.getParameters());
//        return new DatabaseFilterResult("("+leftResult.getQuery()+") OR ("+ rightResult.getQuery() +")", param);
//    }

    @Override
    public SolrFilterResult buildSolrQuery() {
        return new SolrFilterResult("("+left.buildSolrQuery()+") OR ("+right.buildSolrQuery()+")");
    }

    @Override
    public boolean isShown(RepostioryItem item) {
        return left.isShown(item) || right.isShown(item);
    }
}
