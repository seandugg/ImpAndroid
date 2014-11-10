package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentViewPager;
import ie.ucc.bis.supportinglife.validation.Form;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author timothyosullivan
 */

public class RadioGroupListener implements OnCheckedChangeListener {
	
	public static final String RADIO_BUTTON_TEXT_DATA_KEY = "RadioButtonText";
	
	private AbstractAssessmentPage page;
	private String dataKey;
	private Fragment fragment;
	private Form form;
	
	public RadioGroupListener(AbstractAssessmentPage page, String dataKey) {
		setPage(page);
		setDataKey(dataKey);
	}
	
	public RadioGroupListener(AbstractAssessmentPage page, String dataKey, Form form, Fragment fragment) {
		setPage(page); 
		setDataKey(dataKey);
		setForm(form);
		setFragment(fragment);
	}
	
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
		
		if (radioButton != null) {
			// firstly add text label of radio button
			// needed for displaying review UI screen
	    	getPage().getPageData().putString(dataKey + RADIO_BUTTON_TEXT_DATA_KEY, radioButton.getText().toString());
	    	
	    	// secondly add the id of the radio button
	    	// needed for onCreateView() in relevant Fragment to re-display
	    	// a view from page data
			getPage().getPageData().putInt(dataKey, checkedId);
		}
		else {
			// indicates that the radio group has been cleared, i.e.
			// no radio button selected, programaticaly. Need to remove
			// key entry from page data
			getPage().getPageData().remove(dataKey + RADIO_BUTTON_TEXT_DATA_KEY);
			getPage().getPageData().remove(dataKey);
		}
		
		// validation check
		if (getForm() != null) {
			boolean valid = getForm().performValidation();
			if (getFragment() != null) {
				AssessmentViewPager pager = ((AssessmentActivity) getFragment().getActivity()).getAssessmentViewPager();
				int pagePosition = ((AssessmentActivity) getFragment().getActivity()).getAssessmentModel().getAssessmentPages().indexOf(getPage());
				pager.configurePagingEnabledElement(pagePosition, valid);
			}
		}
		
    	getPage().notifyDataChanged();
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
