package org.lareferencia.backend.util.parameters;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class PropertyA extends AbstractProperty {
	
	@JsonProperty("expresion")
	String expresion;
	
	@JsonProperty("valores")
	List<String> valores;
	
	@JsonProperty("diccionario")
	Map<String,String> diccionario;


	@Override
	public String toString() {
		return "PropertyA [expresion=" + expresion + ", valores=" + valores
				+ ", name=" + name + "]";
	}


	
	

}
