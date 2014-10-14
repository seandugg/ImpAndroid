package ie.ucc.bis.supportinglife.validation;

import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

public class Field {
	
    private List<Validation> validations = new LinkedList<Validation>();
    private TextView textView;
    private View view;
    private String label;

    private Field(TextView textView, String label) {
    	setTextView(textView);
    	setLabel(label);
    }
    
    private Field(View view, String label) {
    	setView(view);
    	setLabel(label);
    }
      
    public static Field using(View view, String label) {
        return new Field(view, label);
    }
    
    public static Field using(TextView textView, String label) {
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

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
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