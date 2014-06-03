package ie.ucc.bis.supportinglife.validation;

import android.widget.EditText;

public interface ValidationFailedRenderer {

    void showErrorMessage(ValidationResult validationResult);
    void clear(EditText textView);	
    void clearAll();	
}
