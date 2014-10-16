package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RadioGroupFieldValidations {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private View view;
    private String label;
    
    private RadioGroupFieldValidations(View view, String label) {
    	setView(view);
    	setLabel(label);
    }
      
    public static RadioGroupFieldValidations using(View view, String label) {
        return new RadioGroupFieldValidations(view, label);
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

    public TextView getRadioGroupLabel() {
    	return (TextView) ((LinearLayout) this.getView()).getChildAt(0);
    }
    
    public RadioGroup getRadioGroup() {
    	return (RadioGroup) ((LinearLayout) this.getView()).getChildAt(1);
    }
    
	public List<Validation> getValidations() {
		return validations;
	}

	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
    
}