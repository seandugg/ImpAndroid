package ie.ucc.bis.supportinglife.assessment.imci.model;

import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AnalyticsPageList;
import android.content.Context;

/**
 * 
 * @author timothyosullivan
 */

public class ImciAssessmentResultsModel extends AbstractModel {

	public static final String IMCI_REVIEW_RESULTS_TITLE = "IMCI_REVIEW_RESULTS";
	public static final String IMCI_CLASSIFICATIONS_TITLE = "IMCI_CLASSIFICATIONS";
	public static final String IMCI_TREATMENTS_TITLE = "IMCI_TREATMENTS";
	
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */
	public ImciAssessmentResultsModel(Context context) {
		super(context);
	}

	@Override
	protected AnalyticsPageList configureAnalyticsPageList() {
		/*
		 * IMCI Data Analytic Pages for IMCI Assessment Results
		 * 
		 * 1. IMCI Review Results Page
		 * 2. IMCI Classifications Page
		 * 3. IMCI Treatments Page
		 * 
		 */
		
		return new AnalyticsPageList(new ImciReviewResultsPage(IMCI_REVIEW_RESULTS_TITLE),
									new ImciClassificationsPage(IMCI_CLASSIFICATIONS_TITLE),
									new ImciTreatmentsPage(IMCI_TREATMENTS_TITLE));
	}
}
