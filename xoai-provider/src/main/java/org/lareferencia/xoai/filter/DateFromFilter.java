/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.filter;

import com.lyncode.builder.DateBuilder;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import java.util.Date;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DateFromFilter extends LRFilter {
    private static DateProvider dateProvider = new BaseDateProvider();
    private Date date;

    public DateFromFilter(Date date)
    {
        this.date = new DateBuilder(date).setMinMilliseconds().build();
    }

//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context)
//    {
//        return new DatabaseFilterResult("i.last_modified >= ?",
//                new java.sql.Date(date.getTime()));
//    }

    @Override
    public boolean isShown(RepostioryItem item)
    {
        if (item.getDatestamp().compareTo(date) >= 0)
            return true;
        return false;
    }

    @Override
    public SolrFilterResult buildSolrQuery()
    {
        String format = dateProvider.format(date).replace("Z", ".000Z"); // Tweak to set the milliseconds
        return new SolrFilterResult("item.lastmodified:["
                + ClientUtils.escapeQueryChars(format)
                + " TO *]");
    }

}
