package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.validation.Form;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * 
 * @author timothyosullivan
 */

public class AssessmentWizardTextWatcher implements TextWatcher {

	private AbstractAssessmentPage page;
	private String dataKey;
	private Fragment fragment;
	private Form form;
	
	public AssessmentWizardTextWatcher(AbstractAssessmentPage page, String dataKey) {
		setPage(page); 
		setDataKey(dataKey);
	}
	
	public AssessmentWizardTextWatcher(AbstractAssessmentPage page, String dataKey, Form form, Fragment fragment) {
		setPage(page); 
		setDataKey(dataKey);
		setForm(form);
		setFragment(fragment);
	}
		
	public void afterTextChanged(Editable editable) {
		if (editable != null) {
			getPage().getPageData().putString(dataKey, editable.toString());
			
			// valiation check
			if (getForm() != null) {
				boolean valid = getForm().performValidation();
				if (getFragment() != null) {
					((AssessmentActivity) fragment.getActivity()).getAssessmentViewPager().setPagingEnabled(valid);
				}
			}
		}
		else {
			getPage().getPageData().remove(dataKey);
		}
    	getPage().notifyDataChanged();
	}

	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
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
