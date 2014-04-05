package ie.ucc.bis.supportinglife.assessment.imci.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.imci.ui.ImciAssessmentResultsReviewFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: IMCI Review Results
 * 
 * Review Tab in IMCI Assessment Results
 * 
 * Responsible for capturing data analytics in relation to
 * IMCI Review tab in the IMCI Assessment Results
 * 
 * @author timothyosullivan
 */

public class ImciReviewResultsPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_IMCI_REVIEW_RESULTS_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_IMCI_REVIEW_RESULTS_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_IMCI_REVIEW_RESULTS_PAGE_DURATION";
	
    private ImciAssessmentResultsReviewFragment imciAssessmentResultsReviewFragment;
	
    @Override
    public Fragment createFragment() {
    	setImciAssessmentResultsReviewFragment(ImciAssessmentResultsReviewFragment.create(getKey()));
        return getImciAssessmentResultsReviewFragment();
    }

	public ImciReviewResultsPage(String title) {
		super(title);
	}

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the IMCI 'review assessment results' page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }

	public ImciAssessmentResultsReviewFragment getImciAssessmentResultsReviewFragment() {
		return imciAssessmentResultsReviewFragment;
	}

	public void setImciAssessmentResultsReviewFragment(ImciAssessmentResultsReviewFragment imciAssessmentResultsReviewFragment) {
		this.imciAssessmentResultsReviewFragment = imciAssessmentResultsReviewFragment;
	}
}
