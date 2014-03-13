package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

/**
 * Test Case ID: ccm_rule_8_1
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Other Problem'
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Any Other Problems I cannot Treat: YES
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Other Problem'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> Refer child to health facility
 * 		-> Write referral note
 * 		-> Do not give antibiotic or antimalarial
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmOtherProblemDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
    public CcmOtherProblemDiagnosticTest() {
        super();
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	   	  	
    	// 1. Any Other Problems I cannot Treat: YES
    	String reviewItemLabel = getResources().getString(R.string.ccm_ask_secondary_assessment_review_cannot_treat_problems);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    }

    /**
     * Test case to check the classification and treatment of 
     * the refer signs: 
     * 
     * 		-> 'Other Problem'
     * 
     */
    public void testDangerSigns() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   1, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Have the correct classifications been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Other Problem"));
         
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   3, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Refer child to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Write referral note"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Do not give antibiotic or antimalarial"));
    }
} // end of class