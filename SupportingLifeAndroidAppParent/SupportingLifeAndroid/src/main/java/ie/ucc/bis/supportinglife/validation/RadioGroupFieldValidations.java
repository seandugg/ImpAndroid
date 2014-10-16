package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

import android.widget.RadioGroup;
import android.widget.TextView;

public class RadioGroupFieldValidations {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private RadioGroup radioGroup;
    private TextView label;
    
    private RadioGroupFieldValidations(RadioGroup radioGroup, TextView label) {
    	setRadioGroup(radioGroup);
    	setLabel(label);
    }
      
    public static RadioGroupFieldValidations using(RadioGroup radioGroup, TextView label) {
        return new RadioGroupFieldValidations(radioGroup, label);
    }
    
    public RadioGroupFieldValidations validate(Validation what) {
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


    public void setRadioGroup(RadioGroup radioGroup) {
    	this.radioGroup = radioGroup;
    }
    
    public RadioGroup getRadioGroup() {
    	return this.radioGroup;
    }
    
	public List<Validation> getValidations() {
		return validations;
	}

	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}

	public TextView getLabel() {
		return label;
	}

	public void setLabel(TextView label) {
		this.label = label;
	}
    
}