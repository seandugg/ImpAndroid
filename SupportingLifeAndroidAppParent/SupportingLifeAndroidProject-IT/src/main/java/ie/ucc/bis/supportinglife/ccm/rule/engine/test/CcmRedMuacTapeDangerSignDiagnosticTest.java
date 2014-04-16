package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.RedMuacTapeCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.model.listener.DateDialogSetListener;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.ccm.rule.engine.utilities.CcmRuleEngineUtilities;
import ie.ucc.bis.supportinglife.ui.utilities.DateUtilities;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Test Case ID: ccm_rule_12_1
 * 
 * This test case evaluates the correctness of the CCM Classification and 
 * Treatment rule engines in assessing the condition: 
 * 
 * 		-> 'Red on MUAC Tape' 
 * 
 * The test cases establishes the following patient criteria to fulfil 
 * this condition:
 * 
 * 		-> MUAC Tape Colour: Red
 *		-> Patient Age is older than 6 months and younger than 5 years
 * 
 * The classification returned by the CCM Classification rule engine should
 * be:
 * 
 * 		-> 'Red on MUAC Tape'
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
public class CcmRedMuacTapeDangerSignDiagnosticTest extends CcmDiagnosticRuleEngineTest {
	
	private static final int PATIENT_AGE_IN_MONTHS = 8;
	private static final String RED_MUAC_TAPE_COLOUR = "Red";
	
    public CcmRedMuacTapeDangerSignDiagnosticTest() {
        super(); 
    }
    
    @Override
    public void setUp() {
    	super.setUp();

    	// CONFIGURE THE PATIENT SYMPTOMS
    	// 1. Patient Age older than 6 months and younger than 5 years
    	Calendar cal = Calendar.getInstance(Locale.UK);
	    cal.add(Calendar.MONTH, (PATIENT_AGE_IN_MONTHS * -1));
    	String birthDate = new SimpleDateFormat(DateUtilities.DATE_CUSTOM_FORMAT, DateDialogSetListener.LOCALE).format(cal.getTime());
    	
    	String reviewItemLabel = getResources().getString(R.string.ccm_general_patient_details_review_date_of_birth);
    	String reviewItemSymptomId = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_symptom_id);
    	String reviewItemIdentifier = getResources().getString(R.string.ccm_general_patient_details_date_of_birth_id);
    	ReviewItem birthDateReviewItem = new ReviewItem(reviewItemLabel, birthDate, reviewItemSymptomId, null, -1, reviewItemIdentifier);
    	getReviewItems().add(birthDateReviewItem);    	
    	
    	// 2. MUAC Tape Colour: Red
    	reviewItemLabel = getResources().getString(R.string.ccm_look_assessment_review_muac_tape_colour);	
    	reviewItemSymptomId = getResources().getString(R.string.ccm_look_assessment_red_muac_tape_colour_symptom_id);
    	reviewItemIdentifier = getResources().getString(R.string.ccm_look_assessment_muac_tape_colour_id);
    	// note: In assessing whether the 'Red on MUAC Tape' symptom applies when interpreting the 'MUAC Tape Colour',
    	//       the age of the child is a determining factor. Therefore the date of birth child needs to capture to
    	//       facilitate the decision logic.    	
    	getReviewItems().add(new RedMuacTapeCcmReviewItem(reviewItemLabel, RED_MUAC_TAPE_COLOUR, 
    			reviewItemSymptomId, null, -1, Arrays.asList(birthDateReviewItem), reviewItemIdentifier));
    }

    /**
     * Test case to check the classification and treatment of 
     * the danger sign: 'Red on MUAC Tape'
     * 
     */
    public void testRedMuacTapeDangerSign() {
    	// 1. Execute the Classification rule engine to determine patient classifications
    	// 2. Execute the Treatment rule engine to determine patient treatments
    	executeRuleEngines();
        
        // 3. Has the correct number of classifications been determined?
       assertEquals("the actual number of patient classifications does not match the expected number",
    		   1, CcmRuleEngineUtilities.calculateStandardClassificationNumber(getPatientAssessment().getDiagnostics()));
        
        // 4. Has the correct classification been determined?
        assertEquals("incorrect classification assessed", true, CcmRuleEngineUtilities.classificationPresent(getPatientAssessment().getDiagnostics(), "Red on MUAC Tape"));
        
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