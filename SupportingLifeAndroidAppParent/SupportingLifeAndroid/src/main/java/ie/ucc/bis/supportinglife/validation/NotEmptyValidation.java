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
    public ValidationResult validate(Field fieldValidation) {
        boolean isValid = !TextUtils.isEmpty(fieldValidation.getTextView().getText());
        return isValid ?
            ValidationResult.buildSuccess(fieldValidation.getTextView())
            : ValidationResult.buildFailed(fieldValidation.getTextView(), fieldValidation.getLabel() + EMPTY_ALERT);
    }
	
}
