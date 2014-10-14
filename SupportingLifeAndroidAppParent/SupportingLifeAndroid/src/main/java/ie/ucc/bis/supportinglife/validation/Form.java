package ie.ucc.bis.supportinglife.validation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.EditText;

public class Form {

    private List<Field> fields = new ArrayList<Field>();
    private ValidationFailedRenderer validationFailedRenderer;
    private Context context;
    private boolean valid;

    public Form(Context context) {
        setContext(context);
        setValidationFailedRenderer(new ViewValidationFailedRenderer(getContext()));
        setValid(true);
    }

    public void addField(Field field) {
        getFields().add(field);
    }
    
    public boolean performValidation() {
        boolean allValid = true;
        getValidationFailedRenderer().clearAll();

        for (Field field : getFields()) {
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
            	getValidationFailedRenderer().clear((EditText) field.getTextView());
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
    
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
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
