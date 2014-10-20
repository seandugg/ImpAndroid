package ie.ucc.bis.supportinglife.validation;

import android.content.Context;
import android.text.TextUtils;

public class NotEmptyValidation extends BaseValidation {
	
	private static final String EMPTY_ALERT = " cannot be empty";

    public static Validation build(Context context) {
        return new NotEmptyValidation(context);
    }

    private NotEmptyValidation(Context context) {
        super(context);
    }

    @Override
    public ValidationResult validate(TextFieldValidations field) {
		boolean isValid = true;
		
		isValid = !TextUtils.isEmpty(field.getTextValidatedView().getText());
		
	    return isValid ?
	            ValidationResult.buildSuccess(field.getTextValidatedView())
	            : ValidationResult.buildFailed(field.getTextValidatedView(), field.getLabel() + EMPTY_ALERT);
    }
}
