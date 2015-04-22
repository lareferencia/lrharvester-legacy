/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.database;


import org.dspace.content.DSpaceObject;
import org.dspace.handle.HandleManager;
import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;
import org.lareferencia.xoai.services.api.database.HandleResolver;
import org.lareferencia.xoai.services.api.database.HandleResolverException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

public class DSpaceHandlerResolver implements HandleResolver {
    @Autowired
    private ContextService contextService;

    @Override
    public DSpaceObject resolve(String handle) throws HandleResolverException {
       // try {
            //return HandleManager.resolveToObject(contextService.getContext(), handle);
        	throw new HandleResolverException("Llamada a resolusión de objeto dspace, codigo obsoleto");
        	// TODO: Este método ya no debiera ser llamado, HandleResolver.resolve
        
        	
//        } catch (ContextServiceException e) {
//            throw new HandleResolverException(e);
//        } catch (SQLException e) {
//            throw new HandleResolverException(e);
//        }
    }

    @Override
    public String getHandle(DSpaceObject object) throws HandleResolverException {
//        try {
//            return HandleManager.findHandle(contextService.getContext(), object);
//        } catch (SQLException e) {
//            throw new HandleResolverException(e);
//        } catch (ContextServiceException e) {
//            throw new HandleResolverException(e);
//        }
    	throw new HandleResolverException("Llamada a resolusión de handle dspace, codigo obsoleto");

    }
}
