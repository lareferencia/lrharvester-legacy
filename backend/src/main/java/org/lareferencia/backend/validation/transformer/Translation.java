package org.lareferencia.backend.validation.transformer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@ToString
class Translation {

	@JsonProperty("search")
	String search;

	@JsonProperty("replace")
	String replace;

	public Translation(@JsonProperty("search") String search, @JsonProperty("replace") String replace) {
		super();
		this.search = search;
		this.replace = replace;
	}

	public Translation() {
		super();
	}

}