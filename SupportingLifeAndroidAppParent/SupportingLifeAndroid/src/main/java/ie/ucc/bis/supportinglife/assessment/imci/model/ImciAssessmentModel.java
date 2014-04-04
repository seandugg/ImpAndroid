package ie.ucc.bis.supportinglife.assessment.imci.model;

import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AnalyticsPageList;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentPageList;
import ie.ucc.bis.supportinglife.assessment.model.ReviewPage;
import android.content.Context;

/**
 * 
 * @author timothyosullivan
 */

public class ImciAssessmentModel extends AbstractModel {

	public static final String GENERAL_PATIENT_DETAILS_PAGE_TITLE = "Patient Details";
	public static final String DANGER_SIGNS_PAGE_TITLE = "Danger Signs";
	public static final String BREATHING_ASSESSMENT_PAGE_TITLE = "Breathing Assessment";
	public static final String DIARRHOEA_ASSESSMENT_PAGE_TITLE = "Diarrhoea Assessment";
	public static final String FEVER_ASSESSMENT_PAGE_TITLE = "Fever Assessment";
	public static final String EAR_ASSESSMENT_PAGE_TITLE = "Ear Assessment";
	public static final String MALNUTRITION_ASSESSMENT_PAGE_TITLE = "Malnutrition Assessment";
	public static final String IMMUNIZATION_ASSESSMENT_PAGE_TITLE = "Immunization Status";
	public static final String IMCI_REVIEW_PAGE_TITLE = "IMCI REVIEW PAGE";
	
	
	/**
	 * Constructor
	 * 
	 * @param context Context
	 */
	public ImciAssessmentModel(Context context) {
		super(context);
	}

	@Override
	protected AssessmentPageList configureAssessmentPageList() {
		/*
		 * Assessment Wizard Pages are as follows:
		 * 
		 * 1. General Patient Details AnalyticsPage
		 * 2. General Danger Signs AnalyticsPage
		 * 3. Cough / Breathing Assessment AnalyticsPage
		 * 4. Diarrhoea Assessment AnalyticsPage
		 * 5. Fever Assessment AnalyticsPage
		 * 6. Ear Assessment AnalyticsPage
		 * 7. Malnutrition and Anaemia AnalyticsPage
		 * 8. Immunization Status AnalyticsPage
		 * 
		 */	
		return new AssessmentPageList(new GeneralPatientDetailsPage(this, 
				GENERAL_PATIENT_DETAILS_PAGE_TITLE).setRequired(true),
		new GeneralDangerSignsPage(this, 
				DANGER_SIGNS_PAGE_TITLE).setRequired(true),
		new BreathingAssessmentPage(this, 
				BREATHING_ASSESSMENT_PAGE_TITLE).setRequired(true),
		new DiarrhoeaAssessmentPage(this,
				DIARRHOEA_ASSESSMENT_PAGE_TITLE).setRequired(true),
		new FeverAssessmentPage(this, 
				FEVER_ASSESSMENT_PAGE_TITLE).setRequired(true),
		new EarAssessmentPage(this,
				EAR_ASSESSMENT_PAGE_TITLE).setRequired(true),
		new MalnutritionAssessmentPage(this,
				MALNUTRITION_ASSESSMENT_PAGE_TITLE).setRequired(true),
		new ImmunizationAssessmentPage(this,
				IMMUNIZATION_ASSESSMENT_PAGE_TITLE).setRequired(true));		
	}
	
	@Override
	protected AnalyticsPageList configureAnalyticsPageList() {
		/*
		 * CCM Data Analytic Pages (beside Assessment Pages whose
		 * data analytics are captured implicitly) are as follows:
		 * 
		 * 1. Review Page
		 */
		
		return new AnalyticsPageList(new ReviewPage(IMCI_REVIEW_PAGE_TITLE));
	}
	
}
