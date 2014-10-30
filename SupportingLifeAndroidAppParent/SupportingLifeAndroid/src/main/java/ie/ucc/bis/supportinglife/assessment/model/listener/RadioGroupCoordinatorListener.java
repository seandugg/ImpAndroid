package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.assessment.imci.model.DynamicView;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.validation.Form;
import ie.ucc.bis.supportinglife.validation.RadioGroupFieldValidations;
import ie.ucc.bis.supportinglife.validation.TextFieldValidations;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * This listener class is responsible for listening to a particular 
 * radio group, taking record of the option selected within the 
 * radio group, and also controlling the visibility of a 
 * dynamic view(s) on the UI depending on the user selection.
 * 
 * @author timothyosullivan
 *
 */
public class RadioGroupCoordinatorListener implements OnCheckedChangeListener {

	public static final String RADIO_BUTTON_TEXT_DATA_KEY = "RadioButtonText";
	public static final String DEFAULT_RADIO_BUTTON_ANIMATE_UP_TEXT = "No";

	private AbstractAssessmentPage page;
	private String dataKey;
	private List<String> radioButtonAnimateUpText;
	private List<DynamicView> dynamicViews;
	private ViewGroup parentView;
	private View animatedView;
	private Fragment fragment;
	private Form form;
	private TextFieldValidations textFieldValidations;
	private RadioGroupFieldValidations radioGroupFieldValidations;

	public RadioGroupCoordinatorListener(AbstractAssessmentPage page, String dataKey, List<DynamicView> dynamicViews, 
			ViewGroup parentView, View animatedView) {
		setPage(page);
		setDataKey(dataKey);
		setDynamicViews(dynamicViews);
		setParentView(parentView);

		List<String> animateUpRadioButtonTextTriggers = new ArrayList<String>();
		animateUpRadioButtonTextTriggers.add(DEFAULT_RADIO_BUTTON_ANIMATE_UP_TEXT);
		setRadioButtonAnimateUpText(animateUpRadioButtonTextTriggers);
		
		setAnimatedView(animatedView);
	}

	public RadioGroupCoordinatorListener(AbstractAssessmentPage page, String dataKey, List<DynamicView> dynamicViews, 
			ViewGroup parentView, View animatedView, List<String> animateUpTextTriggers) {
		setPage(page);
		setDataKey(dataKey);
		setDynamicViews(dynamicViews);
		setParentView(parentView);
		setAnimatedView(animatedView);
		setRadioButtonAnimateUpText(animateUpTextTriggers);
	}
	
	public RadioGroupCoordinatorListener(AbstractAssessmentPage page, String dataKey, List<DynamicView> dynamicViews, 
			ViewGroup parentView, View animatedView, Form form, Fragment fragment, RadioGroupFieldValidations fieldValidations) {
		
		this(page, dataKey, dynamicViews, parentView, animatedView);
		setForm(form);
		setFragment(fragment);
		setRadioGroupFieldValidations(fieldValidations);
	}


	public RadioGroupCoordinatorListener(AbstractAssessmentPage page, String dataKey, List<DynamicView> dynamicViews, 
			ViewGroup parentView, View animatedView, List<String> animateUpTextTriggers, 
			Form form, Fragment fragment, TextFieldValidations fieldValidations) {
		
		this(page, dataKey, dynamicViews, 
				parentView, animatedView, animateUpTextTriggers);
		setForm(form);
		setFragment(fragment);
		setTextFieldValidations(fieldValidations);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {

		int indexPosition = getParentView().indexOfChild(getAnimatedView()) + 1;

		RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
		// firstly add text label of radio button
		// needed for displaying review UI screen
		getPage().getPageData().putString(dataKey + RADIO_BUTTON_TEXT_DATA_KEY, radioButton.getText().toString());

		// secondly add the id of the radio button
		// needed for onCreateView() in relevant Fragment to re-display
		// a view from page data
		getPage().getPageData().putInt(dataKey, checkedId);

		// Thirdly, need to handle the View(s) that this RadioButton is controlling
		if (radioButtonAnimateUpEvent(radioButton.getText().toString())) {
			for (DynamicView dynamicView : getDynamicViews()) {
				if (dynamicView.getControlledElement() instanceof RadioGroup) {
					((RadioGroup) dynamicView.getControlledElement()).clearCheck();
					if (getForm() != null) {
						// remove validations from the view being removed
						getForm().removeRadioGroupFieldValidations(getRadioGroupFieldValidations());
					}
				}
				else if (dynamicView.getControlledElement() instanceof EditText) {
					((EditText) dynamicView.getControlledElement()).setText(null);
					if (getForm() != null) {
						// remove validations from the view being removed
						getForm().removeTextFieldValidations(getTextFieldValidations());
					}
				}

				int index = getParentView().indexOfChild(dynamicView.getWrappedView());
				if (index != -1) {				
					getParentView().removeViewAt(index);
				}
			}
		}	
		else { // e.g. user has selected 'YES'
			for (int counter = 0; counter < getDynamicViews().size(); counter++) {
				getParentView().addView(getDynamicViews().get(counter).getWrappedView(), indexPosition + counter);
				// add any associated validations to the view being added
				if (getDynamicViews().get(counter).getControlledElement() instanceof RadioGroup && getForm() != null) {
						getForm().addRadioGroupFieldValidations(getRadioGroupFieldValidations());
				}
				else if (getDynamicViews().get(counter).getControlledElement() instanceof EditText && getForm() != null) {
					
					getForm().addTextFieldValidations(getTextFieldValidations());
				}
			}
		}
		
		// validation check
		if (getForm() != null) {
			boolean valid = getForm().performValidation();
			if (getFragment() != null) {
				((AssessmentActivity) fragment.getActivity()).getAssessmentViewPager().setPagingEnabled(valid);
			}
		}
		
		getPage().notifyDataChanged();
	}

	/**
	 * Method responsible for determining whether an animate up event should occur 
	 * on the dynamic view given the radio button selected
	 * 
	 * 
	 * @param string
	 * @return - boolean (if this constitutes an animateUpEvent)
	 */
	private boolean radioButtonAnimateUpEvent(String radioButtonClickedText) {

		for (String animateUpText : getRadioButtonAnimateUpText()) {
			if (animateUpText.equalsIgnoreCase(radioButtonClickedText)) {
				return true;
			}
		}
		return false;
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

	public List<String> getRadioButtonAnimateUpText() {
		return radioButtonAnimateUpText;
	}

	public void setRadioButtonAnimateUpText(List<String> radioButtonAnimateUpText) {
		this.radioButtonAnimateUpText = radioButtonAnimateUpText;
	}

	private List<DynamicView> getDynamicViews() {
		return dynamicViews;
	}

	private void setDynamicViews(List<DynamicView> dynamicViews) {
		this.dynamicViews = dynamicViews;
	}

	private ViewGroup getParentView() {
		return parentView;
	}

	private void setParentView(ViewGroup parentView) {
		this.parentView = parentView;
	}

	public View getAnimatedView() {
		return animatedView;
	}

	public void setAnimatedView(View animatedView) {
		this.animatedView = animatedView;
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

	private TextFieldValidations getTextFieldValidations() {
		return textFieldValidations;
	}

	private void setTextFieldValidations(TextFieldValidations textFieldValidations) {
		this.textFieldValidations = textFieldValidations;
	}

	private RadioGroupFieldValidations getRadioGroupFieldValidations() {
		return radioGroupFieldValidations;
	}

	private void setRadioGroupFieldValidations(RadioGroupFieldValidations radioGroupFieldValidations) {
		this.radioGroupFieldValidations = radioGroupFieldValidations;
	}
	
}
