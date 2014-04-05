package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmAssessmentResultsReviewFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Review Results
 * 
 * Review Tab in CCM Assessment Results
 * 
 * Responsible for capturing data analytics in relation to
 * CCM Review tab in the CCM Assessment Results
 * 
 * @author timothyosullivan
 */

public class CcmReviewResultsPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_CCM_REVIEW_RESULTS_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_CCM_REVIEW_RESULTS_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_CCM_REVIEW_RESULTS_PAGE_DURATION";
	
    private CcmAssessmentResultsReviewFragment ccmAssessmentResultsReviewFragment;
	
    @Override
    public Fragment createFragment() {
    	setCcmAssessmentResultsReviewFragment(CcmAssessmentResultsReviewFragment.create(getKey()));
        return getCcmAssessmentResultsReviewFragment();
    }

	public CcmReviewResultsPage(String title) {
		super(title);
	}

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the 'CCM review assessment results' page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }

	public CcmAssessmentResultsReviewFragment getCcmAssessmentResultsReviewFragment() {
		return ccmAssessmentResultsReviewFragment;
	}

	public void setCcmAssessmentResultsReviewFragment(CcmAssessmentResultsReviewFragment ccmAssessmentResultsReviewFragment) {
		this.ccmAssessmentResultsReviewFragment = ccmAssessmentResultsReviewFragment;
	}
}
