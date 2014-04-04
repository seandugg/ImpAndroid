package ie.ucc.bis.supportinglife.assessment.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.imci.ui.ReviewFragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: IMCI & CCM Assessment
 * 
 * Final Stage in IMCI & CCM Breadcrumb UI
 * 
 * Responsible for displaying IMCI & CCM review items 
 * 
 * @author timothyosullivan
 */

public class ReviewPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_REVIEW_CCM_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_REVIEW_CCM_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_REVIEW_CCM_PAGE_DURATION";
	
    private ReviewFragment reviewFragment;
	
    @Override
    public Fragment createFragment() {
    	setReviewFragment(ReviewFragment.create(getKey()));
        return getReviewFragment();
    }

	public ReviewPage(String title) {
		super(title);
	}

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the 'review assessment' page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }

	public ReviewFragment getReviewFragment() {
		return reviewFragment;
	}

	public void setReviewFragment(ReviewFragment reviewFragment) {
		this.reviewFragment = reviewFragment;
	}
}
