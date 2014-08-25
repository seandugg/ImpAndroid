package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AnalyticsPageList;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentPageList;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 
 * @author timothyosullivan
 */

public class CcmAssessmentModel extends AbstractAssessmentModel {

	public static final String CCM_GENERAL_PATIENT_DETAILS_PAGE_TITLE = "Patient Details";
	public static final String CCM_INITIAL_ASK_ASSESSMENT_PAGE_TITLE = "Ask and Look Assessment (1)";
	public static final String CCM_SECONDARY_ASK_ASSESSMENT_PAGE_TITLE = "Ask and Look Assessment (2)";
	public static final String LOOK_ASSESSMENT_PAGE_TITLE = "Look Assessment";
	public static final String SENSOR_ASSESSMENT_PAGE_TITLE = "Sensor Assessment";
	public static final String CCM_REVIEW_PAGE_TITLE = "CCM REVIEW PAGE";
	
	private static final String SENSOR_AVAILABLE = "Yes";
	private static final String SENSOR_NOT_AVAILABLE = "No";
	
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
		 * 1. General Patient Details CCM Analytics Page
		 * 2. Initial Ask Assessment CCM Analytics Page
		 * 3. Secondary Ask Assessment CCM Analytics Page
		 * 4. Look Assessment CCM Analytics Page
		 * 5. Zephyr Sensor Readings Page (optional)
		 */
		
		// need to determine if zephyr sensor interaction will be included in assessment
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String sensorAvailable = settings.getString(SupportingLifeBaseActivity.SENSOR_AVAILABILITY_SELECTION_KEY, SENSOR_NOT_AVAILABLE);

        if (sensorAvailable.equalsIgnoreCase(SENSOR_AVAILABLE)) {
			return new AssessmentPageList(new GeneralPatientDetailsCcmPage(this, 
					CCM_GENERAL_PATIENT_DETAILS_PAGE_TITLE).setRequired(true),
					new InitialAskCcmPage(this,	CCM_INITIAL_ASK_ASSESSMENT_PAGE_TITLE).setRequired(true),
					new SecondaryAskCcmPage(this, CCM_SECONDARY_ASK_ASSESSMENT_PAGE_TITLE).setRequired(true),
					new LookCcmPage(this, LOOK_ASSESSMENT_PAGE_TITLE).setRequired(true),
					new SensorCcmPage(this, SENSOR_ASSESSMENT_PAGE_TITLE).setRequired(true));
        }
        else {
			return new AssessmentPageList(new GeneralPatientDetailsCcmPage(this, 
					CCM_GENERAL_PATIENT_DETAILS_PAGE_TITLE).setRequired(true),
					new InitialAskCcmPage(this,	CCM_INITIAL_ASK_ASSESSMENT_PAGE_TITLE).setRequired(true),
					new SecondaryAskCcmPage(this, CCM_SECONDARY_ASK_ASSESSMENT_PAGE_TITLE).setRequired(true),
					new LookCcmPage(this, LOOK_ASSESSMENT_PAGE_TITLE).setRequired(true));        	
        }
		
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
