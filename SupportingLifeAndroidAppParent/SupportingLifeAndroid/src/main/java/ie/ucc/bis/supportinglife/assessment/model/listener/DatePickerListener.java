package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.assessment.imci.ui.DatePickerDialogFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentViewPager;
import ie.ucc.bis.supportinglife.validation.Form;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * Listener for DatePicker EditText UI Component. Upon receipt of a
 * click event, the listener will activate a date picker dialog.
 * 
 * @author TOSullivan
 *
 */
public class DatePickerListener implements OnFocusChangeListener {

	private Fragment invokingFragment;
	private AbstractAssessmentPage page;
	private String dataKey;
	private Form form;

	public DatePickerListener(Fragment fragment, AbstractAssessmentPage page, String dataKey) {
		setInvokingFragment(fragment);
		setPage(page);
		setDataKey(dataKey);
	}
	
	public DatePickerListener(Fragment fragment, AbstractAssessmentPage page, String dataKey, Form form) {
		this(fragment, page, dataKey);
		setForm(form);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			FragmentTransaction fragmentTransaction = getInvokingFragment().getFragmentManager().beginTransaction();
			DialogFragment dialogFragment = DatePickerDialogFragment.create((EditText) v, getPage(), getDataKey());		
			dialogFragment.show(fragmentTransaction, "date_dialog");
		}
		conductValidation();
	}

	/**
	 * Validate form data following event
	 */
	private void conductValidation() {
		// validation check
		if (getForm() != null) {
			boolean valid = getForm().performValidation();
			if (getInvokingFragment() != null) {
				AssessmentViewPager pager = ((AssessmentActivity) getInvokingFragment().getActivity()).getAssessmentViewPager();
				int pagePosition = ((AssessmentActivity) getInvokingFragment().getActivity()).getAssessmentModel().getAssessmentPages().indexOf(page);
				pager.configurePagingEnabledElement(pagePosition, valid);
			}
		}
	}
		
	public Fragment getInvokingFragment() {
		return invokingFragment;
	}

	public void setInvokingFragment(Fragment invokingFagment) {
		this.invokingFragment = invokingFagment;
	}

	public AbstractAssessmentPage getPage() {
		return page;
	}

	public void setPage(AbstractAssessmentPage page) {
		this.page = page;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
