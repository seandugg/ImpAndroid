package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.rule.engine.TreatmentRecommendation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * @author timothyosullivan
 */

public class TreatmentCheckBoxListener implements OnClickListener {
		
	private AbstractAssessmentPage page;
	private String dataKey;
	private TreatmentRecommendation treatmentRecommendation;
	
	public TreatmentCheckBoxListener(AbstractAssessmentPage page, String dataKey, TreatmentRecommendation treatmentRecommendation) {
		setPage(page);
		setDataKey(dataKey);
		setTreatmentRecommendation(treatmentRecommendation);
	}
	
	@Override
	public void onClick(View v) {
		if (((CheckBox) v).isChecked()) {
			getPage().getPageData().putBoolean(dataKey, true);
			getTreatmentRecommendation().setTreatmentAdministered(true);
		}
		else {
			getPage().getPageData().putBoolean(dataKey, false);
			getTreatmentRecommendation().setTreatmentAdministered(false);
		}
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

	public TreatmentRecommendation getTreatmentRecommendation() {
		return treatmentRecommendation;
	}

	public void setTreatmentRecommendation(TreatmentRecommendation treatmentRecommendation) {
		this.treatmentRecommendation = treatmentRecommendation;
	}
}
