/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.lareferencia.xoai.services.impl.config;

import org.lareferencia.xoai.ConfigurationManager;
import org.lareferencia.xoai.services.api.config.ConfigurationService;

public class LRConfigurationService implements ConfigurationService {
    @Override
    public String getProperty(String key) {
        return ConfigurationManager.getProperty(key);
    }

    
    // se retira la implementacion de configuracion de modulos para simplificar los archivos de config 
   /* @Override
    public String getProperty(String module, String key)  {
        return ConfigurationManager.getProperty(module, key);
    }*/

    @Override
    public boolean getBooleanProperty(String module, String key, boolean defaultValue) {
        return ConfigurationManager.getBooleanProperty(module, key, defaultValue);
    }
}
