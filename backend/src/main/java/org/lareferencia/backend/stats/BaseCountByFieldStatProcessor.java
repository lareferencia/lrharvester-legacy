package org.lareferencia.backend.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;


public abstract class BaseCountByFieldStatProcessor extends BaseStatProcessor {
	
	@Getter
	protected List<String> fieldNames;

	protected Map<String, Integer[]> countMap;
	
	protected enum DataIndex { ALL, NONEMPTY, VALID, RESERVED }
	
	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
		
		countMap = new HashMap<String, Integer[]>();	
		for (String fieldName:fieldNames) {
			countMap.put(fieldName,  new Integer[]{0,0,0,0} );	
		}
	}
	
	protected void incrementFieldCounter(String fieldName, DataIndex index) {
		countMap.get(fieldName)[index.ordinal()] = countMap.get(fieldName)[index.ordinal()] + 1;
	}
}
