package ie.ucc.bis.supportinglife.validation;

import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnFocusChangeListener;


/**
 * Listener responsible for performing a validation check on a form when a 
 * focus change occurs on a UI component 
 * 
 * @author TOSullivan
 */
public class ValidationListener implements OnFocusChangeListener {

	private Fragment fragment;
	private Form form;
		
	/**
	 * Constructor
	 */
	public ValidationListener(Form form) {
		setForm(form);
	}

	/**
	 * Constructor
	 */
	public ValidationListener(Form form, Fragment fragment) {
		setForm(form);
		setFragment(fragment);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		boolean valid = getForm().performValidation();
		if (getFragment() != null) {
			((AssessmentActivity) fragment.getActivity()).getAssessmentViewPager().setPagingEnabled(valid);
		}
	}

	private Fragment getFragment() {
		return fragment;
	}

	private void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
