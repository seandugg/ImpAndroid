package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AnalyticsPageList;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentPageList;
import android.content.Context;

/**
 * 
 * @author timothyosullivan
 */

public class CcmAssessmentModel extends AbstractAssessmentModel {

	public static final String CCM_GENERAL_PATIENT_DETAILS_PAGE_TITLE = "Patient Details";
	public static final String CCM_INITIAL_ASK_ASSESSMENT_PAGE_TITLE = "Ask and Look Assessment (1)";
	public static final String CCM_SECONDARY_ASK_ASSESSMENT_PAGE_TITLE = "Ask and Look Assessment (2)";
	public static final String LOOK_ASSESSMENT_PAGE_TITLE = "Look Assessment";
	public static final String CCM_REVIEW_PAGE_TITLE = "CCM REVIEW PAGE";
	
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */
	public CcmAssessmentModel(Context context) {
		super(context);
	}

	@Override
	protected AssessmentPageList configureAssessmentPageList() {
		/*
		 * CCM Assessment Pages are as follows:
		 * 
		 * 1. General Patient Details CCM AnalyticsPage
		 * 2. Initial Ask Assessment CCM AnalyticsPage
		 * 3. Secondary Ask Assessment CCM AnalyticsPage
		 * 4. Look Assessment CCM AnalyticsPage
		 * 
		 */

		return new AssessmentPageList(new GeneralPatientDetailsCcmPage(this, 
				CCM_GENERAL_PATIENT_DETAILS_PAGE_TITLE).setRequired(true),
				new InitialAskCcmPage(this,
						CCM_INITIAL_ASK_ASSESSMENT_PAGE_TITLE).setRequired(true),
				new SecondaryAskCcmPage(this,
						CCM_SECONDARY_ASK_ASSESSMENT_PAGE_TITLE).setRequired(true),
				new LookCcmPage(this,
								LOOK_ASSESSMENT_PAGE_TITLE).setRequired(true));
		
	}

	@Override
	protected AnalyticsPageList configureAnalyticsPageList() {
		/*
		 * CCM Data Analytic Pages (beside Assessment Pages whose
		 * data analytics are captured implicitly) are as follows:
		 * 
		 * 1. Review Page
		 */
		
		return new AnalyticsPageList(new CcmReviewPage(CCM_REVIEW_PAGE_TITLE));
	}
}
