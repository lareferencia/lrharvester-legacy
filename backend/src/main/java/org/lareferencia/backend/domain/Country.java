
package org.lareferencia.backend.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Country extends AbstractEntity{

	private String iso;
	private String name;	
}
