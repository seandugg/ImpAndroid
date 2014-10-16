package ie.ucc.bis.supportinglife.validation;

import android.widget.TextView;

public interface ValidationFailedRenderer {

    void showErrorMessage(ValidationResult validationResult);
    void clear(TextView textView);	
    void clearAll();	
}
