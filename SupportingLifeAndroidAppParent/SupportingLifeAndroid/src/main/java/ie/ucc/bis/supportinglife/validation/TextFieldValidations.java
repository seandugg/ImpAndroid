package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

import android.widget.TextView;

public class TextFieldValidations {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private TextView textView;
    private String label;

    private TextFieldValidations(TextView textView, String label) {
    	setTextView(textView);
    	setLabel(label);
    }
    
    public static TextFieldValidations using(TextView textView, String label) {
        return new TextFieldValidations(textView, label);
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

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
    
}