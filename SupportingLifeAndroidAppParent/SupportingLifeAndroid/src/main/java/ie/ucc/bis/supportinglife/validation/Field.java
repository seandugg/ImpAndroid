package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

import android.widget.EditText;

public class Field {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private EditText textView;
    private String label;

    private Field(EditText textView, String label) {
    	setTextView(textView);
    	setLabel(label);
    }
    
    public static Field using(EditText textView, String label) {
        return new Field(textView, label);
    }
    
    public Field validate(Validation what) {
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

	public EditText getTextView() {
		return textView;
	}

	public void setTextView(EditText textView) {
		this.textView = textView;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
    
}