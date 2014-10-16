package ie.ucc.bis.supportinglife.validation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.TextView;

public class ViewValidationFailedRenderer implements ValidationFailedRenderer {

	private Context context;
	private List<TextView> textViews = new ArrayList<TextView>();
	
	public ViewValidationFailedRenderer(Context context) {
		setContext(context);
	}
	
	@Override
	public void showErrorMessage(ValidationResult validationResult) {
		TextView textView = validationResult.getTextView();
		getTextViews().add(textView);
		textView.setError(validationResult.getMessage());
	}

	@Override
	public void clear(TextView textView) {
		textView.setError(null);
	}
	
	@Override
	public void clearAll() {
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
