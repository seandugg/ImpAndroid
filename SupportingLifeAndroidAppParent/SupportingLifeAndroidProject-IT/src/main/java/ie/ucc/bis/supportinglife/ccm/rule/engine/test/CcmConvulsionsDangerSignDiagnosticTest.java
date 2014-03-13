package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

/**
 * Test Case ID: ccm_rule_4_1
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Convulsions' 
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Convulsions: YES
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Convulsions'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> REFER URGENTLY to health facility
 * 		-> Explain why child needs to go to health facility
 * 		-> Advise to give fluids and continue feeding
 * 		-> Advise to keep child warm, if 'child is NOT hot with fever'
 * 		-> Write a referral note
 * 		-> Arrange transportation and help solve other difficulties in referral
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmConvulsionsDangerSignDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
    public CcmConvulsionsDangerSignDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	
    	// 1. Convulsions: YES
    	String reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_convulsions);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_convulsions_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_convulsions_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger sign: 'Convulsions'
     * 
     */
    public void testDiarrhoeaDangerSign() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   1, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Convulsions"));
        
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   6, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "REFER URGENTLY to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Explain why child needs to go to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to give fluids and continue feeding"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to keep child warm, if 'child is NOT hot with fever'"));																														 
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Write a referral note"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Arrange transportation and help solve other difficulties in referral"));
    }
} // end of class