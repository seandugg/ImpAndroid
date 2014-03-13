package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.RedEyesDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

/**
 * Test Case ID: ccm_rule_7_3
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Red Eye (less than 4 days)' 
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Red Eye: YES
 * 		-> Red Eye Duration: 2 Days
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Red Eye (less than 4 days)'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> TREAT at home and ADVISE on home care
 * 		-> Apply antibiotic eye ointment.
 *		   Squeeze the size of a grain of rice on each of the inner lower eyelids, three times a day for 3 days.
 * 		-> Advise on when to return. Go to nearest health facility or, if not possible, return immediately if child:
 * 			1. Cannot drink
 * 			2. Becomes sicker
 * 			3. Has blood in stool
 * 		-> Advise caregiver to give more fluids and continue feeding
 * 		-> Follow up child in 3 days
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmRedEyeSickSignDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String RED_EYE_DURATION_IN_DAYS = "2";
	
    public CcmRedEyeSickSignDiagnosticTest() {
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

    	// 2. Red Eye Duration: 2 days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_secondary_assessment_review_red_eyes_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_red_eyes_duration_four_days_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_secondary_assessment_red_eye_duration_id);
    	getReviewItems().add(new RedEyesDurationCcmReviewItem(reviewItemLabel, RED_EYE_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));	
    }

    /**
     * Test case to check the classification and treatment of 
     * the sick sign: 'Red Eye (less than 4 days)'
     * 
     */
    public void testRedEyeSickSign() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   1, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Red Eye (less than 4 days)"));
        
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   5, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));  
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "TREAT at home and ADVISE on home care"));
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), 
        		"Apply antibiotic eye ointment. Squeeze the size of a grain of rice on each of the inner lower eyelids, three times a day for 3 days."));
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), 
        		"Advise on when to return. Go to nearest health facility or, if not possible, return immediately if child: 1) Cannot drink 2) Becomes sicker 3) Has blood in stool"));																														 
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise caregiver to give more fluids and continue feeding"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Follow up child in 3 days"));        
    }

} // end of class