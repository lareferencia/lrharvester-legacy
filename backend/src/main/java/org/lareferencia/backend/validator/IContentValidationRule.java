package org.lareferencia.backend.validator;


public interface IContentValidationRule {
	
	public boolean validate(String content);
	
	public boolean isMandatory();
    public void setMandatory(boolean isMandatory);
    
    public Integer getMinValidOccurrences();
    public void setMinValidOccurrences(Integer value);
    
    public Integer getMaxValidOccurrences();
    public void setMaxValidOccurrences(Integer value);
}
