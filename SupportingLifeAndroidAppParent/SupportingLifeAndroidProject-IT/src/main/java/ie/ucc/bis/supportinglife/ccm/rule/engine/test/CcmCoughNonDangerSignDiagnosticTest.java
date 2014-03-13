package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.CoughDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

/**
 * Test Case ID: ccm_rule_1_2
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Cough for 21 Days or more' when Cough Duration < 21 Days
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Cough: YES
 * 		-> Cough Duration: 18 Days
 * 
 * No classifications should be returned by the CCM Classification rule engine.
 * 
 * No treatments should be returned by the CCM Treatment rule engine.
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmCoughNonDangerSignDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String COUGH_DURATION_IN_DAYS = "18";
	
    public CcmCoughNonDangerSignDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	
    	// 1. Cough: YES
    	String reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_cough);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_cough_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_cough_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));

    	// 2. Cough Duration: 18 Days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_cough_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_cough_duration_twenty_one_days_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_cough_duration_id);
    	getReviewItems().add(new CoughDurationCcmReviewItem(reviewItemLabel, COUGH_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    }

    /**
     * Test case to check:
     * 
     *  -> No classifications should be returned by the CCM Classification rule engine.
     *   
     *  -> No treatments should be returned by the CCM Treatment rule engine.
     *  
     */
    public void testDangerSigns() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   0, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
         
        // 4. Have the correct treatments been determined?
       assertEquals("the actual number of patient treatments does not match the expected number",
    		   0, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
    }
} // end of class