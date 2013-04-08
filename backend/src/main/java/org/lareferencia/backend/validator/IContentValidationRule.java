package org.lareferencia.backend.validator;


public interface IContentValidationRule {
	
	public boolean validate(String content);
	
	public boolean isMandatory();
    public void setMandatory(boolean isMandatory);
}
