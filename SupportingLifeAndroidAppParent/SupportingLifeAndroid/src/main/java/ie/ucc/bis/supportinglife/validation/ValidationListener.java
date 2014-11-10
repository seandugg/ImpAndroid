package ie.ucc.bis.supportinglife.validation;

import android.view.View;
import android.view.View.OnFocusChangeListener;


/**
 * Listener responsible for performing a validation check on a form when a 
 * focus change occurs on a UI component 
 * 
 * @author TOSullivan
 */
public class ValidationListener implements OnFocusChangeListener {

	private Form form;
		
	/**
	 * Constructor
	 */
	public ValidationListener(Form form) {
		setForm(form);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		getForm().performValidation();
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
