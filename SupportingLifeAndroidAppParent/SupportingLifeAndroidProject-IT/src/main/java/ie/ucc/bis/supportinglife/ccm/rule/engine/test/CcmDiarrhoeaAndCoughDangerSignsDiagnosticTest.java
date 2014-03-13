package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.CoughDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.DiarrhoeaDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

/**
 * Test Case ID: ccm_rule_2_2
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Cough for 21 Days or more'
 * 		-> 'Diarrhoea for 14 Days or more'
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Cough: YES
 * 		-> Cough Duration: 21 Days
 * 		-> Diarrhoea (loose stools): YES
 * 		-> Diarrhoea Duration: 16 Days
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Cough for 21 days or more'
 * 		-> 'Diarrhoea for 14 Days or more'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> REFER URGENTLY to health facility
 * 		-> Explain why child needs to go to health facility
 * 		-> Advise to give fluids and continue feeding
 * 		-> Begin giving ORS solution immediately
 * 		-> Advise to keep child warm, if 'child is NOT hot with fever'
 * 		-> Write a referral note
 * 		-> Arrange transportation and help solve other difficulties in referral
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmDiarrhoeaAndCoughDangerSignsDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String COUGH_DURATION_IN_DAYS = "21";
	private static final String DIARRHOEA_DURATION_IN_DAYS = "16";
	
    public CcmDiarrhoeaAndCoughDangerSignsDiagnosticTest() {
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

    	// 2. Cough Duration: 21 Days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_cough_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_cough_duration_twenty_one_days_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_cough_duration_id);
    	getReviewItems().add(new CoughDurationCcmReviewItem(reviewItemLabel, COUGH_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    	
    	// 3. Diarrhoea: YES
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_diarrhoea);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_diarrhoea_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_diarrhoea_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));

    	// 4. Diarrhoea Duration: 16 days
    	reviewItemLabel = getResources().getString(R.string.ccm_ask_initial_assessment_review_diarrhoea_duration);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_ask_initial_assessment_diarrhoea_duration_fourteen_days_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_ask_initial_assessment_diarrhoea_duration_id);
    	getReviewItems().add(new DiarrhoeaDurationCcmReviewItem(reviewItemLabel, DIARRHOEA_DURATION_IN_DAYS, reviewItemSymptomId, null, -1, reviewItemIdentifier));	
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger signs: 
     * 
     * 		-> 'Cough for 21 Days or more'
     * 		-> 'Diarrhoea for 14 Days or more'
     * 
     */
    public void testDangerSigns() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   2, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Have the correct classifications been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Cough for 21 Days or more"));
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Diarrhoea for 14 Days or more"));
         
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   7, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "REFER URGENTLY to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Explain why child needs to go to health facility"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to give fluids and continue feeding"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Begin giving ORS solution immediately"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to keep child warm, if 'child is NOT hot with fever'"));																														 
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Write a referral note"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Arrange transportation and help solve other difficulties in referral"));
    }
} // end of class