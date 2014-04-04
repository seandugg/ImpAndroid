package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AnalyticsPageList;
import android.content.Context;

/**
 * 
 * @author timothyosullivan
 */

public class CcmAssessmentResultsModel extends AbstractModel {

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
}
