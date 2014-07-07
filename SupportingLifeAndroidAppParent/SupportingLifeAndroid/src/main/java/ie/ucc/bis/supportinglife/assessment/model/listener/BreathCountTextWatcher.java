package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Customised Text Watch Listener for the Breath Count EditText
 * in CCM.
 * 
 * Responsible for recording:
 * 
 * - Breath Count Number
 * - Whether breath counter feature was used
 * - Whether a full sixty seconds assessment was done 
 *   if breath counter was used
 * 
 * @author timothyosullivan
 */

public class BreathCountTextWatcher implements TextWatcher {

	private AbstractAssessmentPage page;
	private String breathCountNumberDataKey;
	private String breathCounterUsedDataKey;
	private String fullBreathCountTimeAssessmentDataKey;
	private boolean breathCounterUsed;
	private boolean fullBreathCountTimeAssessment;
	
	public BreathCountTextWatcher(AbstractAssessmentPage page, String breathCountNumberDataKey,
			String breathCounterUsedDataKey, String fullBreathCountTimeAssessmentDataKey) {
		setPage(page); 
		setBreathCountNumberDataKey(breathCountNumberDataKey);
		setBreathCounterUsedDataKey(breathCounterUsedDataKey);
		setFullBreathCountTimeAssessmentDataKey(fullBreathCountTimeAssessmentDataKey);
		setBreathCounterUsed(false);
		setFullBreathCountTimeAssessment(false);
	}
	
	
	public void afterTextChanged(Editable editable) {
		if (editable != null) {
			getPage().getPageData().putString(getBreathCountNumberDataKey(), editable.toString());
			getPage().getPageData().putString(getBreathCounterUsedDataKey(), String.valueOf(isBreathCounterUsed()));
			getPage().getPageData().putString(getFullBreathCountTimeAssessmentDataKey(), String.valueOf(isFullBreathCountTimeAssessment()));
		}
		else {
			getPage().getPageData().remove(getBreathCountNumberDataKey());
			getPage().getPageData().remove(getBreathCounterUsedDataKey());
			getPage().getPageData().remove(getFullBreathCountTimeAssessmentDataKey());
		}
    	getPage().notifyDataChanged();
    	
    	// now reset breath counter usage monitors
		setBreathCounterUsed(false);
		setFullBreathCountTimeAssessment(false);
	}

	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
	private AbstractAssessmentPage getPage() {
		return page;
	}

	private void setPage(AbstractAssessmentPage page) {
		this.page = page;
	}

	private String getBreathCountNumberDataKey() {
		return breathCountNumberDataKey;
	}

	private void setBreathCountNumberDataKey(String breathCountNumberDataKey) {
		this.breathCountNumberDataKey = breathCountNumberDataKey;
	}

	private String getBreathCounterUsedDataKey() {
		return breathCounterUsedDataKey;
	}

	private void setBreathCounterUsedDataKey(String breathCounterUsedDataKey) {
		this.breathCounterUsedDataKey = breathCounterUsedDataKey;
	}

	private String getFullBreathCountTimeAssessmentDataKey() {
		return fullBreathCountTimeAssessmentDataKey;
	}

	private void setFullBreathCountTimeAssessmentDataKey(
			String fullBreathCountTimeAssessmentDataKey) {
		this.fullBreathCountTimeAssessmentDataKey = fullBreathCountTimeAssessmentDataKey;
	}

	private boolean isBreathCounterUsed() {
		return breathCounterUsed;
	}

	public void setBreathCounterUsed(boolean breathCounterUsed) {
		this.breathCounterUsed = breathCounterUsed;
	}

	private boolean isFullBreathCountTimeAssessment() {
		return fullBreathCountTimeAssessment;
	}

	public void setFullBreathCountTimeAssessment(
			boolean fullBreathCountTimeAssessment) {
		this.fullBreathCountTimeAssessment = fullBreathCountTimeAssessment;
	}
}
