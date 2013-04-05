package org.lareferencia.backend.validator;

import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class FieldOccurrenceRangeVRule extends BaseVRule {
	
	public FieldOccurrenceRangeVRule() {
		super();
	}

	private Integer minOccurences = 0;
	private Integer maxOccurences = Integer.MAX_VALUE;

	public FieldOccurrenceRangeVRule(String fieldName, Integer minOccurences,  Integer maxOccurences) {
		super(fieldName);
		this.maxOccurences = maxOccurences;
		this.minOccurences = minOccurences;
	}

	@Override
	public boolean validate(HarvesterRecord record) {	
		try {
			int count = MedatadaDOMHelper.getNodeList(record.getDomNode(), "//" + fieldName).getLength();
			System.out.println( fieldName + ": " + count);
			return count >= minOccurences && count <= maxOccurences;

		} catch (TransformerException e) {
			return false;
		}
	}
}
