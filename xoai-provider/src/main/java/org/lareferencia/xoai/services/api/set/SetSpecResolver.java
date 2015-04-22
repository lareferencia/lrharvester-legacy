/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.lareferencia.xoai.services.api.set;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.lareferencia.xoai.exceptions.InvalidSetSpecException;

public interface SetSpecResolver {
    String toSetSpec (Community community) throws InvalidSetSpecException;
    String toSetSpec (Collection collection) throws InvalidSetSpecException;
    DSpaceObject fromSetSpec (String setSpec) throws InvalidSetSpecException;
}
