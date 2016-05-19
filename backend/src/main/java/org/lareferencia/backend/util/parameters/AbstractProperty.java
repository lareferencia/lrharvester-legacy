package org.lareferencia.backend.util.parameters;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
/* @JsonSubTypes({ @Type(value = PropertyA.class, name = "A") }) */
public abstract class AbstractProperty {

	@JsonProperty("name")
	String name;

	@Override
	public String toString() {
		return "Abstract Property [name=" + name + "]";
	}

}
