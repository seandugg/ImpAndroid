package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.RedEyesDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

/**
 * Test Case ID: ccm_rule_7_2
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Red Eye (less than 4 days)'
 * 		-> 'Red Eye with Visual Problems'
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Red Eye: YES
 * 		-> Red Eye Duration: 3 Days
 * 		-> Difficulty in Seeing: YES
 * 		-> Difficulty in Seeing Duration: 8 Days
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Red Eye (less than 4 days)'
 * 		-> 'Red Eye with Visual Problems'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> REFER URGENTLY to health facility
 * 		-> Explain why child needs to go to health facility
 * 		-> Advise to give fluids and continue feeding
 * 		-> Apply antibiotic eye ointment.
 *		   Squeeze the size of a grain of rice on each of the inner lower eyelids, three times a day for 3 days.
 * 		-> Advise to keep child warm, if 'child is NOT hot with fever'
 * 		-> Write a referral note
 * 		-> Arrange transportation and help solve other difficulties in referral
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmRedEyeAndVisualSignsDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String RED_EYE_DURATION_IN_DAYS = "3";
	private static final String DIFFICULTY_IN_SEEING_DURATION_IN_DAYS = "8";
	
    public CcmRedEyeAndVisualSignsDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	
    	// 1. Red Eye: YES
    	String reviewItemLabel = getResources().getString(R.string.ccm_ask_secondary_assessment_review_red_eyes);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_ask_secondary_assessment_red_eyes_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_ask_secondary_assessment_red_eye_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));

    	// 2. Red Eye Duration: 3 days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_secondary_assessment_review_red_eyes_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_red_eyes_duration_four_days_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_secondary_assessment_red_eye_duration_id);
    	getReviewItems().add(new RedEyesDurationCcmReviewItem(reviewItemLabel, RED_EYE_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    	
    	// 3. Difficulty in Seeing: YES
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_secondary_assessment_review_seeing_difficulty);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    	
    	// 4. Difficulty in Seeing Duration: 8 days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_secondary_assessment_review_seeing_difficulty_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_duration_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_duration_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, DIFFICULTY_IN_SEEING_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger and sick signs: 
     * 
     * 		-> 'Red Eye (less than 4 days)'
     * 		-> 'Red Eye with Visual Problems'
     * 
     */
    public void testRedEyeAndVisualSigns() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   2, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Red Eye (less than 4 days)"));
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Red Eye with Visual Problems")); 
        
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   7, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));  
      
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "REFER URGENTLY to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Explain why child needs to go to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to give fluids and continue feeding"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), 
        		"Apply antibiotic eye ointment. Squeeze the size of a grain of rice on each of the inner lower eyelids, three times a day for 3 days."));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to keep child warm, if 'child is NOT hot with fever'"));																														 
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Write a referral note"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Arrange transportation and help solve other difficulties in referral"));
    }
} // end of class