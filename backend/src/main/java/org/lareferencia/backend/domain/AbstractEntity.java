package org.lareferencia.backend.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * Base class for entity implementations. Uses a {@link Long} id.
 * 
 * @author Oliver Gierke
 */
@MappedSuperclass
@Getter
@ToString
@EqualsAndHashCode
public class AbstractEntity  {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final Long id;

	protected AbstractEntity() {
		this.id = null;
	}
}
