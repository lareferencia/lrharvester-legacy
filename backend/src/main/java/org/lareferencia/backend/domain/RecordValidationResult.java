package org.lareferencia.backend.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.mapping.Array;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.OAIMetadataXSLTransformer;
import org.lareferencia.backend.util.RepositoryNameHelper;
import org.lareferencia.backend.validation.validator.FieldContentValidatorResult;
import org.lareferencia.backend.validation.validator.ValidatorResult;
import org.lareferencia.backend.validation.validator.ValidatorRuleResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Dynamic;

@Getter
@Setter
public class RecordValidationResult {
	
	
	public static final String[] FACET_FIELDS = { "record_is_valid", "record_is_transformed", "valid_rules", "invalid_rules", "institution_name", "repository_name"};
	public static final String SNAPSHOT_ID_FIELD = "snapshot_id";
	public static final String INVALID_RULE_SUFFIX = "_rule_invalid_occrs";
	public static final String VALID_RULE_SUFFIX = "_rule_valid_occrs";


	@Id
	@Field
	private String id;

	@Field("oai_identifier")
	private String identifier;

	@Field( SNAPSHOT_ID_FIELD )
	private Long snapshotID;

	@Field("origin")
	private String origin;

	@Field("set_spec")
	private String setSpec;

	@Field("network_acronym")
	private String networkAcronym;

	@Field("repository_name")
	private String repositoryName;

	@Field("institution_name")
	private String institutionName;

	@Field("record_is_valid")
	private Boolean isValid;

	@Field("record_is_transformed")
	private Boolean isTransformed;

	@Dynamic
	@Field("*" + VALID_RULE_SUFFIX)
	private Map<String, List<String>> validOccurrencesByRuleID;

	@Dynamic
	@Field("*" + INVALID_RULE_SUFFIX)
	private Map<String, List<String>> invalidOccurrencesByRuleID;

	@Field("valid_rules")
	private List<String> validRulesID;

	@Field("invalid_rules")
	private List<String> invalidRulesID;

	
	
	
	/**
	 * Se construye un resultado de validación persistible en solr a partir del
	 * objeto resultado devuelto por el validador para un registro
	 * 
	 * @param result
	 */
	public RecordValidationResult(OAIRecord record,
			ValidatorResult validationResult) {

		validOccurrencesByRuleID = new HashMap<String, List<String>>();
		invalidOccurrencesByRuleID = new HashMap<String, List<String>>();
		validRulesID = new ArrayList<String>();
		invalidRulesID = new ArrayList<String>();

		id = record.getId().toString();
		identifier = record.getIdentifier();
		origin = record.getMetadata().getOrigin();
		setSpec = record.getMetadata().getSetSpec();
		isTransformed = record.isWasTransformed();

		repositoryName = RepositoryNameHelper.extractNameFromMetadata(
				record.getMetadata(), "dc:source", "reponame:");
		institutionName = RepositoryNameHelper.extractNameFromMetadata(
				record.getMetadata(), "dc:source", "instname:");

		snapshotID = record.getSnapshot().getId();
		networkAcronym = record.getSnapshot().getNetwork().getAcronym();

		isValid = validationResult.isValid();

		for (ValidatorRuleResult ruleResult : validationResult
				.getRulesResults()) {

			String ruleID = ruleResult.getRule().getRuleId().toString();

			List<String> invalidOccr = new ArrayList<String>();
			List<String> validOccr = new ArrayList<String>();

			for (FieldContentValidatorResult contentResult : ruleResult.getResults()) {

				if (contentResult.isValid())
					validOccr.add(contentResult.getReceivedValue());
				else
					invalidOccr.add(contentResult.getReceivedValue());

			}

			if (ruleResult.getValid())
				validRulesID.add(ruleID);
			else
				invalidRulesID.add(ruleID);

			validOccurrencesByRuleID.put(ruleID, validOccr);
			invalidOccurrencesByRuleID.put(ruleID, invalidOccr);
		}
	}




	public RecordValidationResult() {
		super();
		// TODO Auto-generated constructor stub
	}
}