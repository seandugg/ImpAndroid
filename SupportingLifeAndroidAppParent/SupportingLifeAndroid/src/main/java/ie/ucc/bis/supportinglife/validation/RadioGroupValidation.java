package ie.ucc.bis.supportinglife.validation;

import android.content.Context;
import android.text.TextUtils;

public class RadioGroupValidation extends BaseValidation {
	
	private static final String SELECTION_ALERT = " option must be selected";

    public static Validation build(Context context) {
        return new RadioGroupValidation(context);
    }

    private RadioGroupValidation(Context context) {
        super(context);
    }

    @Override
    public ValidationResult validate(Field field) {
		boolean isValid = true;
		
//		if (field.get)
//		
//   		isValid = !TextUtils.isEmpty(field.getTextView().getText());
//	    return isValid ?
//	            ValidationResult.buildSuccess(field.getTextView())
//	            : ValidationResult.buildFailed(field.getTextView(), field.getLabel() + SELECTION_ALERT);
		return null;
    }
	
}
