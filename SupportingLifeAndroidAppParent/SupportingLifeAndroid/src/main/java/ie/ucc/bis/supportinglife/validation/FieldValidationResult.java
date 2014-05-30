package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

public class FieldValidationResult {

	private List<ValidationResult> validationResults = new LinkedList<ValidationResult>();

	public FieldValidationResult() {}

	public void addValidationResult(ValidationResult validationResult) {
		getValidationResults().add(validationResult);
	}

	public boolean isValid() {
		for (ValidationResult validationResult : getValidationResults()) {
			if (!validationResult.isValid()) {
				return false;
			}
		}
		return true;
	}

	public List<ValidationResult> getFailedValidationResults() {
		List<ValidationResult> failedValidations = new LinkedList<ValidationResult>();
		for (ValidationResult validationResult : getValidationResults()) {
			if(!validationResult.isValid()) {
				failedValidations.add(validationResult);
			}
		}
		return failedValidations;
	}

	public List<ValidationResult> getValidationResults() {
		return validationResults;
	}

	public void setValidationResults(List<ValidationResult> validationResults) {
		this.validationResults = validationResults;
	}
}
