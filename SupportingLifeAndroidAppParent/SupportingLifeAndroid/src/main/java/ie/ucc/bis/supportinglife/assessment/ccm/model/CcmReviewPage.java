package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmReviewFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Assessment
 * 
 * Final Stage in CCM Breadcrumb UI
 * 
 * Responsible for displaying CCM review items 
 * 
 * @author timothyosullivan
 */

public class CcmReviewPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_REVIEW_CCM_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_REVIEW_CCM_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_REVIEW_CCM_PAGE_DURATION";
	
    private CcmReviewFragment ccmReviewFragment;
	
    @Override
    public Fragment createFragment() {
    	setCcmReviewFragment(CcmReviewFragment.create(getKey()));
        return getCcmReviewFragment();
    }

	public CcmReviewPage(String title) {
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

	public CcmReviewFragment getCcmReviewFragment() {
		return ccmReviewFragment;
	}

	public void setCcmReviewFragment(CcmReviewFragment ccmReviewFragment) {
		this.ccmReviewFragment = ccmReviewFragment;
	}
}
