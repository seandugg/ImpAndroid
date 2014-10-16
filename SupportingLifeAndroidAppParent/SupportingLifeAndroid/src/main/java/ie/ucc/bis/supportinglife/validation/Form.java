package ie.ucc.bis.supportinglife.validation;

import ie.ucc.bis.supportinglife.activity.AssessmentActivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Form {

    private List<TextFieldValidations> textFieldValidations = new ArrayList<TextFieldValidations>();
    private List<RadioGroupFieldValidations> radioGroupFieldValidations = new ArrayList<RadioGroupFieldValidations>();
    private ValidationFailedRenderer validationFailedRenderer;
    private Context context;
    private boolean valid;

    public Form(Context context) {
        setContext(context);
        setTextFieldValidations(new ArrayList<TextFieldValidations>());
        setRadioGroupFieldValidations(new ArrayList<RadioGroupFieldValidations>());
        setValidationFailedRenderer(new ViewValidationFailedRenderer(getContext()));
        setValid(true);
    }

    public void addTextFieldValidations(TextFieldValidations fieldValidations) {
    	getTextFieldValidations().add(fieldValidations);
    }
    
	public void removeTextFieldValidations(TextFieldValidations fieldValidations) {
		getTextFieldValidations().remove(fieldValidations);
	}
    
    public void addRadioGroupFieldValidations(RadioGroupFieldValidations fieldValidations) {
    	getRadioGroupFieldValidations().add(fieldValidations);
    }
    
    public boolean performValidation() {
    	
    	// if validation is off, then always return a positive result
    	if (((AssessmentActivity) getContext()).isValidationOn() == false) {
    		return true;
    	}
    	
        boolean allValid = true;
        getValidationFailedRenderer().clearAll();

        // text entry fields first
        for (TextFieldValidations field : getTextFieldValidations()) {
            FieldValidationResult result = field.validate();      
            
            if (!result.isValid()) {
                ValidationResult validatedResult = result.getFailedValidationResults().get(0);

                // target the keyboard on the first invalid textview
                if (allValid) {
                	FormUtils.showKeyboard(getContext(), validatedResult.getTextView());
                }

               	getValidationFailedRenderer().showErrorMessage(validatedResult);
                allValid = false;
            }
            else {
            	getValidationFailedRenderer().clear(field.getTextView());
            }
        }
        
        // radio group fields next
        for (RadioGroupFieldValidations field : getRadioGroupFieldValidations()) {
            FieldValidationResult result = field.validate();
                       
            if (!result.isValid()) {
                ValidationResult validatedResult = result.getFailedValidationResults().get(0);

               	getValidationFailedRenderer().showErrorMessage(validatedResult);
                allValid = false;
            }
            else {
            	getValidationFailedRenderer().clear(field.getLabel());
            }
        }
        
        // update valid flag
        setValid(allValid);
        
        return isValid();
    }

    public void setValidationFailedRenderer(ValidationFailedRenderer renderer) {
        this.validationFailedRenderer = renderer;
    }

    public ValidationFailedRenderer getValidationFailedRenderer() {
        return this.validationFailedRenderer;
    }
    
	private List<TextFieldValidations> getTextFieldValidations() {
		return textFieldValidations;
	}

	private void setTextFieldValidations(List<TextFieldValidations> textFieldValidations) {
		this.textFieldValidations = textFieldValidations;
	}

	private List<RadioGroupFieldValidations> getRadioGroupFieldValidations() {
		return radioGroupFieldValidations;
	}

	private void setRadioGroupFieldValidations(List<RadioGroupFieldValidations> radioGroupFieldValidations) {
		this.radioGroupFieldValidations = radioGroupFieldValidations;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
    public boolean isValid() {
    	return valid;
    }
}
