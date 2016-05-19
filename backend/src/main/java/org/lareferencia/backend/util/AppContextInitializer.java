package org.lareferencia.backend.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class AppContextInitializer implements
		ApplicationContextInitializer<ConfigurableWebApplicationContext> {

	@Override
	public void initialize(ConfigurableWebApplicationContext applicationContext) {

		final String configFilePath = applicationContext.getServletContext()
				.getInitParameter("mainConfigFilePath");

		try {

			// Se agregan las propiedades del archivo de configuración
			ResourcePropertySource source = new ResourcePropertySource("file:"
					+ configFilePath);
			applicationContext.getEnvironment().getPropertySources()
					.addFirst(source);

			// Se agrega una propieedad con el paht del archivo de conf para ser
			// accedido desde el xml de contextos
			Properties lrConfigProperties = new Properties();
			lrConfigProperties.put("backend.properties.path", configFilePath);

			PropertiesPropertySource pathsPSource = new PropertiesPropertySource(
					"lrpaths", lrConfigProperties);
			applicationContext.getEnvironment().getPropertySources()
					.addFirst(pathsPSource);

			System.out
					.println("\n\n\n******************** Inicializando configuración desde  "
							+ configFilePath + "  !!! \n\n\n");

		} catch (IOException e) {

			System.out
					.println("\n\n\nNo se puede acceder al archivo de configuración principal:"
							+ configFilePath + "\n\n\n");

			// handle error
		}
	}
}