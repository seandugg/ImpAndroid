package ie.ucc.bis.supportinglife.validation;

import android.content.Context;

public class ButtonGroupValidation extends BaseValidation {
	
	private static final String SELECTION_ALERT = " option must be selected";

    public static Validation build(Context context) {
        return new ButtonGroupValidation(context);
    }

    private ButtonGroupValidation(Context context) {
        super(context);
    }

    @Override
    public ValidationResult validate(ButtonGroupTableValidations field) {
    	
    	// determine if any of the radio buttons are checked	
		return field.getToggleButtonGroupTableLayout().isChecked() ?
				ValidationResult.buildSuccess(field.getLabel())
				: ValidationResult.buildFailed(field.getLabel(), field.getLabel() + SELECTION_ALERT);
		
    }
}
