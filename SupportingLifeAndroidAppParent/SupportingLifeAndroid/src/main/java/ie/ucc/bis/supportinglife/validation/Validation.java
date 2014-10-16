package ie.ucc.bis.supportinglife.validation;

public interface Validation {

	public ValidationResult validate(TextFieldValidations fieldValidation);
	public ValidationResult validate(RadioGroupFieldValidations radioGroupField);
}