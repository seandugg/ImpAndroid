package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AnalyticsPageList;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentPageList;
import android.content.Context;

/**
 * 
 * @author timothyosullivan
 */

public class CcmAssessmentResultsModel extends AbstractAssessmentModel {

	public static final String CCM_REVIEW_RESULTS_TITLE = "CCM_REVIEW_RESULTS";
	public static final String CCM_CLASSIFICATIONS_TITLE = "CCM_CLASSIFICATIONS";
	public static final String CCM_TREATMENTS_TITLE = "CCM_TREATMENTS";
	
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */
	public CcmAssessmentResultsModel(Context context) {
		super(context);
	}

	@Override
	protected AnalyticsPageList configureAnalyticsPageList() {
		/*
		 * CCM Data Analytic Pages for CCM Assessment Results
		 * 
		 * 1. CCM Review Results Page
		 * 2. CCM Classifications Page
		 * 3. CCM Treatments Page
		 * 
		 */
		
		return new AnalyticsPageList(new CcmReviewResultsPage(CCM_REVIEW_RESULTS_TITLE),
									new CcmClassificationsPage(CCM_CLASSIFICATIONS_TITLE),
									new CcmTreatmentsPage(CCM_TREATMENTS_TITLE));
	}

	@Override
	protected AssessmentPageList configureAssessmentPageList() {
		/*
		 * CCM Data Pages for CCM Assessment Results
		 * 1. CCM Treatments Page
		 */
		
		return new AssessmentPageList(new CcmTreatmentsPage(CCM_TREATMENTS_TITLE));
	}
}
