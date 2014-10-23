package ie.ucc.bis.supportinglife.validation;

public interface Validation {
	
	public static final int ONE_DAY = 1;
	public static final int ONE_HUNDRED_DAYS = 100;
	public static final int ONE_YEAR_IN_DAYS = 365;

	public ValidationResult validate(TextFieldValidations fieldValidation);
	public ValidationResult validate(RadioGroupFieldValidations radioGroupField);
}