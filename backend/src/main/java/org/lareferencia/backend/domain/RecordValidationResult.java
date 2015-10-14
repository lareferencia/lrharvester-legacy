package org.lareferencia.backend.domain;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Dynamic;

@Getter
@Setter
public class RecordValidationResult {
 
    @Id
    @Field
    private String id;
    
    @Field("oai_identifier")
    private String identifier;
    
    @Field("snapshot_id")
    private Long snapshotID;
    
    @Field("network_acronym") 
    private String networkAcronym;
    
    @Field("repository_name") 
    private String repositoryName;
    
    @Field("institution_name") 
    private String institutionName;
    
    @Field("record_is_valid") 
    private Boolean isValid;
    
    @Dynamic
    @Field("*_rule_valid_occrs")
    private Map<String, List<String>> validOccurrencesByRuleID;
    
    @Dynamic
    @Field("*_rule_invalid_occrs")
    private Map<String, List<String>> invalidOccurrencesByRuleID;
    
    @Dynamic
    @Field("*_rule_is_valid")
    private Map<String, Boolean> isRuleValidByRuleID;
    
    public RecordValidationResult() {
    	validOccurrencesByRuleID = new HashMap<String, List<String>>();
    	invalidOccurrencesByRuleID = new HashMap<String, List<String>>();
    	isRuleValidByRuleID = new HashMap<String,Boolean>();
    }

}