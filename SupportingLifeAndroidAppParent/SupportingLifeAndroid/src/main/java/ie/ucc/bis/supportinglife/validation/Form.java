package ie.ucc.bis.supportinglife.validation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.EditText;

public class Form {

    private List<Field> fields = new ArrayList<Field>();
    private ValidationFailedRenderer validationFailedRenderer;
    private Context context;

    public Form(Context context) {
        setContext(context);
        setValidationFailedRenderer(new TextViewValidationFailedRenderer(getContext()));
    }

    public void addField(Field field) {
        getFields().add(field);
    }

    public boolean isValid() {
        boolean isValid = true;
        getValidationFailedRenderer().clear();

        for (Field field : getFields()) {
            FieldValidationResult result = field.validate();

            if (!result.isValid()) {
                ValidationResult firstFailedValidation = result.getFailedValidationResults().get(0);
                EditText textView = firstFailedValidation.getTextView();
                textView.requestFocus();
                textView.selectAll();

//                FormUtils.showKeyboard(mContext, textView);

                getValidationFailedRenderer().showErrorMessage(firstFailedValidation);

                isValid = false;
                break;
            }
        }

        return isValid;
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
}
