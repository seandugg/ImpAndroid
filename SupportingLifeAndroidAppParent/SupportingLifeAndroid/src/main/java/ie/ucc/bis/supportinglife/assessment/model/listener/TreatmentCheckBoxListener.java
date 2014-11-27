package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.CcmAssessmentResultsActivity;
import ie.ucc.bis.supportinglife.assessment.ccm.model.CcmTreatmentsPage;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmAssessmentTreatmentsFragment;
import ie.ucc.bis.supportinglife.rule.engine.TreatmentRecommendation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * @author timothyosullivan
 */

public class TreatmentCheckBoxListener implements OnClickListener {
		
	private static final String ALL_TREATMENTS_ADMINISTERED = "All Treatments Administered";
	
	private CcmTreatmentsPage page;
	private String dataKey;
	private TreatmentRecommendation treatmentRecommendation;
	private CcmAssessmentTreatmentsFragment ccmAssessmentTreatmentsFragment;
	
	public TreatmentCheckBoxListener(CcmTreatmentsPage page, String dataKey,
			TreatmentRecommendation treatmentRecommendation, CcmAssessmentTreatmentsFragment ccmAssessmentTreatmentsFragment) {
		setCcmTreatmentsPage(page);
		setDataKey(dataKey);
		setTreatmentRecommendation(treatmentRecommendation);
		setCcmAssessmentTreatmentsFragment(ccmAssessmentTreatmentsFragment);
	}
	
	@Override
	public void onClick(View v) {
		if (((CheckBox) v).isChecked()) {
			getCcmTreatmentsPage().getPageData().putBoolean(dataKey, true);
			getTreatmentRecommendation().setTreatmentAdministered(true);
			
			// check if all treatments have been administered and flag to
			// user if this is the case
			if (getCcmAssessmentTreatmentsFragment() != null) {
				CcmAssessmentResultsActivity parentActivity = (CcmAssessmentResultsActivity)getCcmAssessmentTreatmentsFragment().getActivity();
				if (parentActivity != null && parentActivity.checkAllTreatmentsAdministered()) {
					Crouton.clearCroutonsForActivity(parentActivity);
					Crouton.makeText(parentActivity, ALL_TREATMENTS_ADMINISTERED, Style.INFO).show();  				
				}
			}
		}
		else {
			getCcmTreatmentsPage().getPageData().putBoolean(dataKey, false);
			getTreatmentRecommendation().setTreatmentAdministered(false);
		}
	}
	
	public CcmTreatmentsPage getCcmTreatmentsPage() {
		return page;
	}

	public void setCcmTreatmentsPage(CcmTreatmentsPage page) {
		this.page = page;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public TreatmentRecommendation getTreatmentRecommendation() {
		return treatmentRecommendation;
	}

	public void setTreatmentRecommendation(TreatmentRecommendation treatmentRecommendation) {
		this.treatmentRecommendation = treatmentRecommendation;
	}

	public CcmAssessmentTreatmentsFragment getCcmAssessmentTreatmentsFragment() {
		return ccmAssessmentTreatmentsFragment;
	}

	public void setCcmAssessmentTreatmentsFragment(
			CcmAssessmentTreatmentsFragment ccmAssessmentTreatmentsFragment) {
		this.ccmAssessmentTreatmentsFragment = ccmAssessmentTreatmentsFragment;
	}
}
