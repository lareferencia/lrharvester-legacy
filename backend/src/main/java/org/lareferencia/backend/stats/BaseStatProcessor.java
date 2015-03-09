package org.lareferencia.backend.stats;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class BaseStatProcessor implements IMetadataStatProcessor{
	protected String id;	
}
