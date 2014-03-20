package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.ChestIndrawingDosageCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.FastBreathingDosageCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.listener.DateDialogSetListener;
import ie.ucc.bis.supportinglife.assessment.model.review.FastBreathingReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Test Case ID: ccm_rule_10_4
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Fast Breathing and Danger Sign'
 * 		-> 'Chest Indrawing'
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Breaths per Minute: 58
 * 		-> Patient Age is older than 2 months and younger than 12 months
 * 		-> Chest Indrawing: YES
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Fast Breathing and Danger Sign'
 * 		-> 'ChestIndrawing'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> REFER URGENTLY to health facility
 * 		-> Explain why child needs to go to health facility
 * 		-> Advise to give fluids and continue feeding
 * 		-> Give first dose of oral antibiotic
 *		   (Cotrimoxazole adult tablet - 80/400)
 * 		   Age 2 months up to 12 months: 1/2 tablet
 * 		-> Advise to keep child warm, if 'child is NOT hot with fever'
 * 		-> Write a referral note
 * 		-> Arrange transportation and help solve other difficulties in referral
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmFastBreathingAndChestIndrawingYoungerChildDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String BREATHS_PER_MINUTE = "58";
	private static final int PATIENT_AGE_IN_MONTHS = 8;
	
    public CcmFastBreathingAndChestIndrawingYoungerChildDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	
    	// 1. Patient Age is older than 2 months and younger than 12 months
    	Calendar cal = Calendar.getInstance(Locale.UK);
	    cal.add(Calendar.MONTH, (PATIENT_AGE_IN_MONTHS * -1));
    	String birthDate = new SimpleDateFormat(DateDialogSetListener.DATE_TIME_CUSTOM_FORMAT, DateDialogSetListener.LOCALE).format(cal.getTime());
    	
    	String reviewItemLabel = getResources().getString(R.string.ccm_general_patient_details_review_date_of_birth);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_id);
    	ReviewItem birthDateReviewItem = new ReviewItem(reviewItemLabel, birthDate, reviewItemSymptomId, null, -1, reviewItemIdentifier);
    	getReviewItems().add(birthDateReviewItem);    	
    	   	
    	// 2. Breaths per Minute: 58
    	reviewItemLabel = getResources().getString(R.string.ccm_look_assessment_review_breaths_per_minute);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_look_assessment_fast_breathing_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_look_assessment_breaths_per_minute_id);
    	// note: In assessing whether the 'fast breathing' symptom applies when interpreting the 'breaths per minute',
    	//       the age of the child is a determining factor. Therefore the date of birth child needs to capture to
    	//       facilitate the decision logic.
    	getReviewItems().add(new FastBreathingReviewItem(reviewItemLabel, BREATHS_PER_MINUTE, 
    			reviewItemSymptomId, null, -1, Arrays.asList(birthDateReviewItem), reviewItemIdentifier));
    	   	
    	// 3. add the required fast breathing dosage review item
    	reviewItemSymptomId = getResources().getString(R.string.ccm_look_assessment_fast_breathing_dosage_symptom_id);
    	// note: In assessing the oral antibiotic dosage for fast breathing,
    	//       the date of birth child needs to be captured to facilitate the decision logic.
    	getReviewItems().add(new FastBreathingDosageCcmReviewItem(null, null, 
    			reviewItemSymptomId, null, -1, Arrays.asList(birthDateReviewItem)));
    	
    	// 4. Chest Indrawing: YES
    	reviewItemLabel = getResources().getString(R.string.ccm_look_assessment_review_chest_indrawing);
    	reviewItemSymptomId = getResources().getString(R.string.ccm_look_assessment_chest_indrawing_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_look_assessment_chest_indrawing_id);
    	getReviewItems().add(new ReviewItem(reviewItemLabel, POSITIVE_SYMPTOM_RESPONSE, reviewItemSymptomId, null, -1, reviewItemIdentifier));
    	
    	// 5. add the required Chest Indrawing dosage review item
    	reviewItemSymptomId = getResources().getString(R.string.ccm_look_assessment_chest_indrawing_dosage_age_symptom_id);
    	// note: In assessing the dosage for chest indrawing assessment,
    	//       the date of birth child needs to be captured to facilitate the decision logic.
    	getReviewItems().add(new ChestIndrawingDosageCcmReviewItem(null, null, reviewItemSymptomId, null, -1, Arrays.asList(birthDateReviewItem)));
    	
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger signs: 
     * 
     * 		-> 'Fast Breathing and Danger Sign'
     * 		-> 'Chest Indrawing'
     * 
     */
    public void testFastBreathingAndChestIndrawingDiagnostics() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   2, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Fast Breathing and Danger Sign"));
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Chest Indrawing"));
        
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   7, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "REFER URGENTLY to health facility"));             
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Explain why child needs to go to health facility"));																														 
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to give fluids and continue feeding"));
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), 
        		"Give first dose of oral antibiotic (Cotrimoxazole adult tablet - 80/400) Age 2 months up to 12 months: 1/2 tablet"));	
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise to keep child warm, if 'child is NOT hot with fever'"));		
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Write a referral note"));
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Arrange transportation and help solve other difficulties in referral"));
    }
} // end of class