package ie.ucc.bis.supportinglife.ccm.rule.engine.test;

import ie.ucc.bis.supportinglife.activity.CcmAssessmentActivity;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.rule.engine.ClassificationRuleEngine;
import ie.ucc.bis.supportinglife.rule.engine.TreatmentRuleEngine;

import java.util.ArrayList;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

/**
 * This abstract class provides base functionality for test cases related
 * to the CCM Diagnostic rule engine.
 * 
 * @author Tim O Sullivan
 *
 */
public abstract class CcmDiagnosticRuleEngineTest extends ActivityInstrumentationTestCase2<CcmAssessmentActivity> {

	protected static final String POSITIVE_SYMPTOM_RESPONSE = "YES";
	protected static final String NEGATIVE_SYMPTOM_RESPONSE = "NO"; 
	protected static final String LOG_TAG = "ie.ucc.bis.supportinglife.ccm.rule.engine.test";

	private SupportingLifeBaseActivity supportingLifeActivity;
	private PatientAssessment patientAssessment;
	private ArrayList<ReviewItem> reviewItems;
	private Resources resources;

	@Override
	public void setUp() {
		setPatientAssessment(new PatientAssessment());
		setSupportingLifeActivity(getActivity());
		setReviewItems(new ArrayList<ReviewItem>());
		setResources(getSupportingLifeActivity().getApplicationContext().getResources());
	}

	/**
	 * Constructor
	 */
	public CcmDiagnosticRuleEngineTest() {
		super(CcmAssessmentActivity.class); 
	}

	/**
	 * Responsible for executing the Classification and Treatment rule engines to determine
	 * patient classifications and treatments
	 * 
	 */
	protected void executeRuleEngines() {
		// 1. Execute the Classification rule engine to determine patient classifications
        ClassificationRuleEngine classificationRuleEngine = new ClassificationRuleEngine();
        classificationRuleEngine.readCcmClassificationRules(getSupportingLifeActivity());
        classificationRuleEngine.determinePatientClassifications(getSupportingLifeActivity(), getReviewItems(), getPatientAssessment(), classificationRuleEngine.getSystemCcmClassifications());
        
        // 2. Execute the Treatment rule engine to determine patient treatments
        TreatmentRuleEngine treatmentRuleEngine = new TreatmentRuleEngine();
        treatmentRuleEngine.readCcmTreatmentRules(getSupportingLifeActivity());
        treatmentRuleEngine.determineCcmTreatments(getSupportingLifeActivity(), getReviewItems(), getPatientAssessment());
	}
	
	/**
	 * Getter Method: getSupportingLifeActivity()
	 */
	public SupportingLifeBaseActivity getSupportingLifeActivity() {
		return supportingLifeActivity;
	}

	/**
	 * Setter Method: setSupportingLifeActivity()
	 */
	public void setSupportingLifeActivity(SupportingLifeBaseActivity supportingLifeActivity) {
		this.supportingLifeActivity = supportingLifeActivity;
	}

	/**
	 * Getter Method: getPatient()
	 */
	public PatientAssessment getPatientAssessment() {
		return patientAssessment;
	}

	/**
	 * Setter Method: setPatient()
	 */
	public void setPatientAssessment(PatientAssessment patientAssessment) {
		this.patientAssessment = patientAssessment;
	}

	/**
	 * Getter Method: getReviewItems()
	 */
	public ArrayList<ReviewItem> getReviewItems() {
		return reviewItems;
	}

	/**
	 * Setter Method: setReviewItems()
	 */
	public void setReviewItems(ArrayList<ReviewItem> reviewItems) {
		this.reviewItems = reviewItems;
	}

	/**
	 * Getter Method: getResources()
	 */
	public Resources getResources() {
		return resources;
	}

	/**
	 * Setter Method: setResources()
	 */
	public void setResources(Resources resources) {
		this.resources = resources;
	}
}
