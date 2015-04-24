/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.data;



import com.lyncode.xoai.dataprovider.core.Set;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class RepositorySet extends Set {
	private static final String DefaultName = "undefined";

	public static String checkName(String name) {
		if (name != null && !name.trim().equals(""))
			return name;
		return DefaultName;
	}

	
	public static Set newSet(String handle, String name) {

		return new Set(handle, checkName(name));
	}
	
	public RepositorySet() {
		
		super("DUMY_SETSPEC","DUMMY_SETNAME");
		// FIXME: ENTIENDO QUE ESTO NO ES USADO FUERA DEL CONTEXTO DSPACE
		
	}

	/*public RepositorySet(Community c) {
		super("com_" + c.getHandle().replace('/', '_'), checkName(c.getName()));
	}

	public RepositorySet(Collection c) {
		super("col_" + c.getHandle().replace('/', '_'), checkName(c.getName()));
	}*/
}
