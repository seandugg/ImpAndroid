package ie.ucc.bis.supportinglife.validation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

public class TextViewValidationFailedRenderer implements ValidationFailedRenderer {

	private Context context;
	private List<TextView> textViews = new ArrayList<TextView>();
	
	public TextViewValidationFailedRenderer(Context context) {
		setContext(context);
	}
	
	@Override
	public void showErrorMessage(ValidationResult validationResult) {
		EditText textView = validationResult.getTextView();
        getTextViews().add(textView);
        textView.setError(validationResult.getMessage());
	}

	@Override
	public void clear() {
		for (TextView textView : getTextViews()) {
            textView.setError(null);
        }
		getTextViews().clear();
    }

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<TextView> getTextViews() {
		return textViews;
	}

	public void setTextViews(List<TextView> textViews) {
		this.textViews = textViews;
	}

}
