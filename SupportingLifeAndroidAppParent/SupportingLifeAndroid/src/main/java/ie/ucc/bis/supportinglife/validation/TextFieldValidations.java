package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

import android.widget.TextView;

public class TextFieldValidations {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private TextView textValidatedView;
    private String label;

    private TextFieldValidations(TextView textValidatedView, String label) {
    	setTextValidatedView(textValidatedView);
    	setLabel(label);
    }
    
    public static TextFieldValidations using(TextView textValidatedView, String label) {
        return new TextFieldValidations(textValidatedView, label);
    }
    
    public TextFieldValidations validate(Validation what) {
        getValidations().add(what);
        return this;
    }
    
    public FieldValidationResult validate() {
        FieldValidationResult fieldValidationResult = new FieldValidationResult();
        for (Validation validation : getValidations()) {
            fieldValidationResult.addValidationResult(validation.validate(this));
        }
        return fieldValidationResult;
    }

	public List<Validation> getValidations() {
		return validations;
	}

	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}

	public String getLabel() {
		return label;
	}

	private void setLabel(String label) {
		this.label = label;
	}

	public TextView getTextValidatedView() {
		return textValidatedView;
	}

	private void setTextValidatedView(TextView textValidatedView) {
		this.textValidatedView = textValidatedView;
	}
}