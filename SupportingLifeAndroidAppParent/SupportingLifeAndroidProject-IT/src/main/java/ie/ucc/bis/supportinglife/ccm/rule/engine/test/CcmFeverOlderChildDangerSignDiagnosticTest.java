package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.FeverDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.FeverLaDosageCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.listener.DateDialogSetListener;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Test Case ID: ccm_rule_3_1
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Fever for last 7 Days'
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Fever (Reported or now?): YES
 * 		-> Fever Duration: 7 Days
 * 		-> Patient Age is older than 3 years and younger than 5 years
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Fever for last 7 Days'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> REFER URGENTLY to health facility
 * 		-> Explain why child needs to go to health facility
 * 		-> Advise to give fluids and continue feeding
 * 		-> Give first dose of LA: 2 tablets
 * 		-> Advise to keep child warm, if 'child is NOT hot with fever'
 * 		-> Write a referral note
 * 		-> Arrange transportation and help solve other difficulties in referral
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmFeverOlderChildDangerSignDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String FEVER_DURATION_IN_DAYS = "7";
	private static final int PATIENT_AGE_IN_YEARS = 4;
	
    public CcmFeverOlderChildDangerSignDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	
    	// 1. Fever (Reported or now?): YES
    	String reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_fever);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_fever_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_fever_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));

    	// 2. Fever Duration: 7 days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_fever_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_fever_duration_seven_days_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_fever_duration_id);
    	getReviewItems().add(new FeverDurationCcmReviewItem(reviewItemLabel, FEVER_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));	
    	
    	// 3. Patient Age is older than 3 years and younger than 5 years
    	Calendar cal = Calendar.getInstance(Locale.UK);
	    cal.add(Calendar.YEAR, (PATIENT_AGE_IN_YEARS * -1));
    	String birthDate = new SimpleDateFormat(DateDialogSetListener.DATE_TIME_CUSTOM_FORMAT, DateDialogSetListener.LOCALE).format(cal.getTime());
    	
    	reviewItemLabel = getResources().getString(R.string.ccm_general_patient_details_review_date_of_birth);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_id);
    	ReviewItem birthDateReviewItem = new ReviewItem(reviewItemLabel, birthDate, reviewItemSymptomId, null, -1, reviewItemIdentifier);
    	getReviewItems().add(birthDateReviewItem);
    	
    	// 4. add the required fever-related LA dosage review item
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_fever_la_dosage_age_symptom_id);
    	// note: In assessing the LA dosage for fever assessment,
    	//       the date of birth child needs to be captured to facilitate the decision logic.
    	getReviewItems().add(new FeverLaDosageCcmReviewItem(null, null, reviewItemSymptomId, null, -1, Arrays.asList(birthDateReviewItem)));
    	
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger sign: 'Fever for last 7 Days'
     * 
     */
    public void testFeverDangerSign() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   1, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Fever for last 7 Days"));
        
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   7, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "REFER URGENTLY to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Explain why child needs to go to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to give fluids and continue feeding"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Give first dose of LA: 2 tablets"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to keep child warm, if 'child is NOT hot with fever'"));																														 
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Write a referral note"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Arrange transportation and help solve other difficulties in referral"));
    }
} // end of class