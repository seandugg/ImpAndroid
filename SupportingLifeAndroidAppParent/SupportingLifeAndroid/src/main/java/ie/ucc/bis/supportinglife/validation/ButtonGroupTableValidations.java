package ie.ucc.bis.supportinglife.validation;

import ie.ucc.bis.supportinglife.ui.custom.ToggleButtonGroupTableLayout;

import java.util.LinkedList;
import java.util.List;

import android.widget.TextView;

public class ButtonGroupTableValidations {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private ToggleButtonGroupTableLayout toggleButtonGroupTableLayout;
    private TextView label;
    
    private ButtonGroupTableValidations(ToggleButtonGroupTableLayout toggleButtonGroupTableLayout, TextView label) {
    	setToggleButtonGroupTableLayout(toggleButtonGroupTableLayout);
    	setLabel(label);
    }
      
    public static ButtonGroupTableValidations using(ToggleButtonGroupTableLayout toggleButtonGroupTableLayout, TextView label) {
        return new ButtonGroupTableValidations(toggleButtonGroupTableLayout, label);
    }
    
    public ButtonGroupTableValidations validate(Validation what) {
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

	public ToggleButtonGroupTableLayout getToggleButtonGroupTableLayout() {
		return toggleButtonGroupTableLayout;
	}

	public void setToggleButtonGroupTableLayout(ToggleButtonGroupTableLayout toggleButtonGroupTableLayout) {
		this.toggleButtonGroupTableLayout = toggleButtonGroupTableLayout;
	}

	public TextView getLabel() {
		return label;
	}

	public void setLabel(TextView label) {
		this.label = label;
	}
}