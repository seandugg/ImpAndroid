package ie.ucc.bis.supportinglife.validation;

import android.widget.EditText;

public class ValidationResult {

    private final boolean isValid;
    private final String message;
    private final EditText textView;

    public static ValidationResult buildSuccess(EditText textView) {
        return new ValidationResult(true, "", textView);
    }

    public static ValidationResult buildFailed(EditText textView, String message) {
        return new ValidationResult(false, message, textView);
    }

    private ValidationResult(boolean isValid, String message, EditText textView) {
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

	public EditText getTextView() {
		return textView;
	}
}
