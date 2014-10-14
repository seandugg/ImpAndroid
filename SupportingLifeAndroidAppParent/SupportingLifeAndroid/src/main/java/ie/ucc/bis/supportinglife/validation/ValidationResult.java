package ie.ucc.bis.supportinglife.validation;

import android.widget.TextView;

public class ValidationResult {

    private final boolean isValid;
    private final String message;
    private final TextView textView;

    public static ValidationResult buildSuccess(TextView textView) {
        return new ValidationResult(true, "", textView);
    }

    public static ValidationResult buildFailed(TextView textView, String message) {
        return new ValidationResult(false, message, textView);
    }

    private ValidationResult(boolean isValid, String message, TextView textView) {
        this.isValid = isValid;
        this.message = message;
        this.textView = textView;
    }

	public boolean isValid() {
		return isValid;
	}

	public String getMessage() {
		return message;
	}

	public TextView getTextView() {
		return textView;
	}
}
