package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.FastBreathingDosageCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.listener.DateDialogSetListener;
import ie.ucc.bis.supportinglife.assessment.model.review.FastBreathingReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;
import ie.ucc.bis.supportinglife.helper.DateHandlerUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Test Case ID: ccm_rule_10_1
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Fast Breathing'
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> Breaths per Minute: 42
 * 		-> Patient Age is older than 1 year and younger than 5 years
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Fast Breathing'
 * 
 * The treatments returned by the CCM Treatment rule engine should be:
 * 
 * 		-> TREAT at home and ADVISE on home care
 * 		-> Give oral antibiotic (cotrimoxazole adult tablet - 80/400).
 * 		   Give twice daily for 5 days.
 * 		   Help caregiver to give first dose now.
 * 		   Age 12 months up to 5 years: 1 tablet (total 10 tabs)
 * 		-> Advise caregiver to give more fluids and continue feeding
 * 		-> Advise on when to return. Go to nearest health facility or, if not possible, return immediately if child:
 * 			1. Cannot drink
 * 			2. Becomes sicker
 * 			3. Has blood in stool
 * 		-> Follow up child in 3 days
 * 
 * @author Tim O Sullivan
 *
 */
public class CcmFastBreathingOlderChildSickSignDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final String BREATHS_PER_MINUTE = "42";
	private static final int PATIENT_AGE_IN_YEARS = 2;
	
    public CcmFastBreathingOlderChildSickSignDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS   	
    	// 1. Patient Age is older than 1 year and younger than 5 years
    	Calendar cal = Calendar.getInstance(Locale.UK);
	    cal.add(Calendar.YEAR, (PATIENT_AGE_IN_YEARS * -1));
    	String birthDate = new SimpleDateFormat(DateHandlerUtils.BIRTH_DATE_CUSTOM_FORMAT, DateDialogSetListener.LOCALE).format(cal.getTime());
    	
    	String reviewItemLabel = getResources().getString(R.string.ccm_general_patient_details_review_date_of_birth);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_id);
    	ReviewItem birthDateReviewItem = new ReviewItem(reviewItemLabel, birthDate, reviewItemSymptomId, null, -1, reviewItemIdentifier);
    	getReviewItems().add(birthDateReviewItem);    	
    	   	
    	// 2. Breaths per Minute: 42
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
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger sign: 'Fast Breathing'
     * 
     */
    public void testFastBreathingSickSign() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   1, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Fast Breathing"));
        
        // 5. Have the correct number of treatments been determined?
        assertEquals("the actual number of patient treatments does not match the expected number",
     		   5, CcmRuleEngineUtilities.calculateTotalTreatmentNumber(getPatientAssessment().getDiagnostics()));
        
        // 6. Have the correct treatments been determined?
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "TREAT at home and ADVISE on home care"));
                
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), 
        		"Give oral antibiotic (cotrimoxazole adult tablet - 80/400). Give twice daily for 5 days. Help caregiver to give first dose now. Age 12 months up to 5 years: 1 tablet (total 10 tabs)"));																														 
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Advise caregiver to give more fluids and continue feeding"));
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), 
        		"Advise on when to return. Go to nearest health facility or, if not possible, return immediately if child: 1) Cannot drink 2) Becomes sicker 3) Has blood in stool"));		
        
        assertEquals("incorrect treatment assessed", true, CcmRuleEngineUtilities.treatmentPresent(getPatientAssessment().getDiagnostics(), "Follow up child in 3 days"));
        
    }
} // end of class