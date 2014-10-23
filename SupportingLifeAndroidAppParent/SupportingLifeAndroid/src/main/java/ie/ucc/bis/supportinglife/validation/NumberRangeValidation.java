package ie.ucc.bis.supportinglife.validation;

import android.content.Context;

public class NumberRangeValidation extends BaseValidation {
	
	private static final String RANGE_VIOLATION_ALERT = " is outside range of %d to %d";
	private int lowerRange;
	private int upperRange;

    public static Validation build(Context context, int lowerRange, int upperRange) {
        return new NumberRangeValidation(context, lowerRange, upperRange);
    }

    private NumberRangeValidation(Context context, int lowerRange, int upperRange) {
        super(context);
        setLowerRange(lowerRange);
        setUpperRange(upperRange);
    }

    @Override
    public ValidationResult validate(TextFieldValidations field) {
		boolean isValid = false;
		
		if(field.getTextValidatedView().getText().length() > 0) {
			int num = Integer.valueOf(field.getTextValidatedView().getText().toString());
			
			if (num >= getLowerRange() && num <= getUpperRange()) {
				isValid = true;
			}
		}
		
	    return isValid ?
	            ValidationResult.buildSuccess(field.getTextValidatedView())
	            : ValidationResult.buildFailed(field.getTextValidatedView(), String.format(field.getLabel() + RANGE_VIOLATION_ALERT, getLowerRange(), getUpperRange()));
    }

	private int getLowerRange() {
		return lowerRange;
	}

	private void setLowerRange(int lowerRange) {
		this.lowerRange = lowerRange;
	}

	private int getUpperRange() {
		return upperRange;
	}

	private void setUpperRange(int upperRange) {
		this.upperRange = upperRange;
	}
}
