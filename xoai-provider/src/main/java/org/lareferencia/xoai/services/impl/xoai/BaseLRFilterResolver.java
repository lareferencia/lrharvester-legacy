/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.filter.Scope;
import com.lyncode.xoai.dataprovider.filter.conditions.*;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;
import org.apache.log4j.Logger;

import org.lareferencia.xoai.filter.*;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;

import org.lareferencia.xoai.services.api.database.FieldResolver;

import org.lareferencia.xoai.services.api.xoai.LRFilterResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.lyncode.xoai.dataprovider.filter.Scope.MetadataFormat;

public class BaseLRFilterResolver implements LRFilterResolver {
    private static final Logger LOGGER = Logger.getLogger(BaseLRFilterResolver.class);

    @Autowired
    FieldResolver fieldResolver;

    @Autowired
    ContextService contextService;

//    @Override
//    public String buildDatabaseQuery(Condition condition, List<Object> parameters, Scope scope) throws ContextServiceException {
//        DSpaceFilter filter = getFilter(condition);
//        DatabaseFilterResult result = filter.buildDatabaseQuery(contextService.getContext());
//        if (result.hasResult())
//        {
//            parameters.addAll(result.getParameters());
//            if (scope == MetadataFormat)
//                return "(item.deleted:true OR ("
//                        + result.getQuery() + "))";
//            else
//                return "(" + result.getQuery() + ")";
//        }
//        return "true";
//    }

    public LRFilter getFilter (Condition condition) {
        if (condition instanceof AndCondition) return (LRFilter) getFilter((AndCondition) condition);
        else if (condition instanceof OrCondition) return (LRFilter) getFilter((OrCondition) condition);
        else if (condition instanceof NotCondition) return (LRFilter) getFilter((NotCondition) condition);
        else if (condition instanceof CustomCondition) {
            CustomCondition customCondition = (CustomCondition) condition;
            return (LRFilter) customCondition.getFilter();
        } else {
            return (LRFilter) condition.getFilter();
        }
    }

    @Override
    public String buildSolrQuery(Scope scope, Condition condition) {
        LRFilter filter = getFilter(condition);
        SolrFilterResult result = filter.buildSolrQuery();
        if (result.hasResult())
        {
            if (scope == MetadataFormat)
                return "(item.deleted:true OR ("
                        + result.getQuery() + "))";
            else
                return "(" + result.getQuery() + ")";
        }
        return "true";
    }

    @Override
    public Filter getFilter(Class<? extends Filter> filterClass, ParameterMap configuration) {
        if (filterClass.isAssignableFrom(DSpaceAtLeastOneMetadataFilter.class)) {
            return new DSpaceAtLeastOneMetadataFilter(configuration);
        } else if (filterClass.isAssignableFrom(DSpaceAuthorizationFilter.class)) {
//            try {
////                return new DSpaceAuthorizationFilter(contextService.getContext());
//            } catch (ContextServiceException e) {
//                LOGGER.error(e.getMessage(), e);
//                return null;
//            }
        } else if (filterClass.isAssignableFrom(DSpaceMetadataExistsFilter.class)) {
            return new DSpaceMetadataExistsFilter(fieldResolver, configuration);
        }
        LOGGER.error("Filter "+filterClass.getName()+" unknown instantiation");
        return null;
    }

    @Override
    public Filter getFilter(AndCondition andCondition) {
        LRFilter leftFilter = this.getFilter(andCondition.getLeft());
        LRFilter rightFilter = this.getFilter(andCondition.getRight());
        return new AndFilter(leftFilter, rightFilter);
    }

    @Override
    public Filter getFilter(OrCondition orCondition) {
        LRFilter leftFilter = this.getFilter(orCondition.getLeft());
        LRFilter rightFilter = this.getFilter(orCondition.getRight());
        return new OrFilter(leftFilter, rightFilter);
    }

    @Override
    public Filter getFilter(NotCondition notCondition) {
        LRFilter filter = this.getFilter(notCondition.getCondition());
        return new NotFilter(filter);
    }
}
