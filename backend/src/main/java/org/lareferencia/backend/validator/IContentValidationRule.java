package org.lareferencia.backend.validator;


public interface IContentValidationRule {
	
	public static final String QUANTIFIER_ZERO_OR_MORE = "0..*";
	public static final String QUANTIFIER_ONE_OR_MORE = "1..*";
	public static final String QUANTIFIER_ONE_ONLY = "1..1";

	
	public ContentValidationResult validate(String content);
	
	public void setQuantifier(String quantifier);
	public String getQuantifier();
	

}
