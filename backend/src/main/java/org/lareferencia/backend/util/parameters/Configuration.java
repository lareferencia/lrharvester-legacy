package org.lareferencia.backend.util.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

	HashMap<String, Map<String, AbstractProperty>> data;

	public Configuration() {
		data = new HashMap<String, Map<String, AbstractProperty>>();
	}

	public List<String> getSections() {
		return new ArrayList<String>(data.keySet());
	}

	public void addSection(String name) {
		data.put(name, new HashMap<String, AbstractProperty>());
	}

	public void addProperty(String section, String name,
			AbstractProperty property) {

		Map<String, AbstractProperty> properties = data.get(section);

		// si no existe lo agrega
		if (properties == null) {
			properties = new HashMap<String, AbstractProperty>();
			data.put(section, properties);
		}

		properties.put(name, property);
	}

}
