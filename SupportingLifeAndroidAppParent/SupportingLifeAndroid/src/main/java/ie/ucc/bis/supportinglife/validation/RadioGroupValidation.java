package ie.ucc.bis.supportinglife.validation;

import android.content.Context;

public class RadioGroupValidation extends BaseValidation {
	
	private static final String SELECTION_ALERT = " option must be selected";

    public static Validation build(Context context) {
        return new RadioGroupValidation(context);
    }

    private RadioGroupValidation(Context context) {
        super(context);
    }

    @Override
    public ValidationResult validate(RadioGroupFieldValidations field) {
		boolean isValid = true;
			
		// check if no radio button in the group has been selected
		if (field.getRadioGroup().getCheckedRadioButtonId() == 0 || field.getRadioGroup().getCheckedRadioButtonId() == -1) {
			isValid = false;
		}
	
		return isValid ?
				ValidationResult.buildSuccess(field.getLabel())
				: ValidationResult.buildFailed(field.getLabel(), field.getLabel() + SELECTION_ALERT);
		
    }
}
