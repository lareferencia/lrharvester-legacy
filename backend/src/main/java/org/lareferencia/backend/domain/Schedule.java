
package org.lareferencia.backend.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Schedule extends AbstractEntity{

	private String cronExpression;	
}
