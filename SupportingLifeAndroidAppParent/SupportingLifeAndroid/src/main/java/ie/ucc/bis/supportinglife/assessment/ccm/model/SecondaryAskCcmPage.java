package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.review.RedEyesDurationCcmReviewItem;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.SecondaryAskCcmFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.ModelCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupListener;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;

import java.util.ArrayList;

import android.content.res.Resources;
import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Ask Assessment
 * 
 * Stage in CCM bread-crumb UI Wizard: 3
 * 
 * Responsible for displaying form for CCM SECONDARY 'Ask' assessment patient details
 * 
 * @author timothyosullivan
 */
public class SecondaryAskCcmPage extends AbstractAssessmentPage {

    public static final String VOMITING_DATA_KEY = "VOMITING";
    public static final String VOMITS_EVERYTHING_DATA_KEY = "VOMITS_EVERYTHING";
    public static final String RED_EYES_DATA_KEY = "RED_EYES";
    public static final String RED_EYES_DURATION_DATA_KEY = "RED_EYES_DURATION";
    public static final String SEEING_DIFFICULTY_DATA_KEY = "SEEING_DIFFICULTY";
    public static final String SEEING_DIFFICULTY_DURATION_DATA_KEY = "SEEING_DIFFICULTY_DURATION";
    public static final String CANNOT_TREAT_PROBLEMS_DATA_KEY = "CANNOT_TREAT_PROBLEMS";
    public static final String CANNOT_TREAT_PROBLEMS_DETAILS_DATA_KEY = "CANNOT_TREAT_PROBLEMS_DETAILS";
    
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_SECOND_ASK_CCM_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_SECOND_ASK_CCM_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_SECOND_ASK_CCM_PAGE_DURATION";
    
    private SecondaryAskCcmFragment secondaryAskCcmFragment;

    public SecondaryAskCcmPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        setSecondaryAskCcmFragment(SecondaryAskCcmFragment.create(getKey()));
        return getSecondaryAskCcmFragment();
    }

	/**
	 * Method: getReviewItems
	 * 
	 * Define the review items associated with the 'ask assessment' page.
	 * 
	 * @param reviewItems : ArrayList<ReviewItem>
	 */      
    @Override
    public void getReviewItems(ArrayList<ReviewItem> reviewItems) {
    	Resources resources = ((AbstractAssessmentModel) getModelCallbacks()).getApplicationContext().getResources();
    	String reviewItemIdentifier = null;
    	String reviewItemLabel = null;
    	String reviewItemValue = null;
    	String reviewItemSymptomId = null;
    	
    	// review header
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_title);
    	reviewItems.add(new ReviewItem(reviewItemLabel, getKey()));	
    	
    	// vomiting
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_vomiting_id); 
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_vomiting);
    	reviewItemValue = getPageData().getString(VOMITING_DATA_KEY + RadioGroupListener.RADIO_BUTTON_TEXT_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_vomiting_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
    	
    	// vomits everything
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_vomits_everything_id); 
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_vomits_everything);
    	reviewItemValue = getPageData().getString(VOMITS_EVERYTHING_DATA_KEY + RadioGroupListener.RADIO_BUTTON_TEXT_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_vomits_everything_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
   
    	// red eyes
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_red_eye_id);
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_red_eyes);
    	reviewItemValue = getPageData().getString(RED_EYES_DATA_KEY + RadioGroupListener.RADIO_BUTTON_TEXT_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_red_eyes_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));

    	// red eyes duration
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_red_eye_duration_id);
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_red_eyes_duration);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_initial_assessment_red_eyes_duration_four_days_symptom_id);
    	reviewItems.add(new RedEyesDurationCcmReviewItem(reviewItemLabel, getPageData().getString(RED_EYES_DURATION_DATA_KEY), reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
    	
    	// seeing difficulty / 'difficulty in seeing'
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_id);
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_seeing_difficulty);
    	reviewItemValue = getPageData().getString(SEEING_DIFFICULTY_DATA_KEY + RadioGroupListener.RADIO_BUTTON_TEXT_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));

    	// seeing difficulty duration / / 'difficulty in seeing' duration
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_duration_id);
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_seeing_difficulty_duration);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_duration_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, getPageData().getString(SEEING_DIFFICULTY_DURATION_DATA_KEY), reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
    	
    	// 'any other problems I cannot treat'
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_id);
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_cannot_treat_problems);
    	reviewItemValue = getPageData().getString(CANNOT_TREAT_PROBLEMS_DATA_KEY + RadioGroupListener.RADIO_BUTTON_TEXT_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));

    	// 'any other problems I cannot treat' details
    	reviewItemIdentifier = resources.getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_details_id);
    	reviewItemLabel = resources.getString(R.string.ccm_ask_secondary_assessment_review_cannot_treat_problems_details);
    	reviewItemSymptomId = resources.getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_details_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, getPageData().getString(CANNOT_TREAT_PROBLEMS_DETAILS_DATA_KEY), reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
    }

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the 'Secondary Ask Assessment' page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }
    
    @Override
    public boolean isCompleted() {
     //   return !TextUtils.isEmpty(getPageData().getString(FIRST_NAME_DATA_KEY));
    	return true;
    }

	/**
	 * Getter Method: getSecondaryAskCcmFragment()
	 */    
	public SecondaryAskCcmFragment getSecondaryAskCcmFragment() {
		return secondaryAskCcmFragment;
	}

	/**
	 * Setter Method: setSecondaryAskCcmFragment()
	 */		
	public void setSecondaryAskCcmFragment(SecondaryAskCcmFragment secondaryAskCcmFragment) {
		this.secondaryAskCcmFragment = secondaryAskCcmFragment;
	}
}
