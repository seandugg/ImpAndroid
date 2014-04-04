package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmAssessmentClassificationsFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Classifications
 * 
 * Classifications Tab in CCM Assessment Results
 * 
 * Responsible for capturing data analytics in relation to
 * CCM Classifications tab
 * 
 * @author timothyosullivan
 */

public class CcmClassificationsPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_CCM_CLASSIFICATIONS_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_CCM_CLASSIFICATIONS_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_CCM_CLASSIFICATIONS_PAGE_DURATION";
	
    private CcmAssessmentClassificationsFragment ccmAssessmentClassificationsFragment;
	
    @Override
    public Fragment createFragment() {
    	setCcmAssessmentClassificationsFragment(CcmAssessmentClassificationsFragment.create(getKey()));
        return getCcmAssessmentClassificationsFragment();
    }

	public CcmClassificationsPage(String title) {
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

	public CcmAssessmentClassificationsFragment getCcmAssessmentClassificationsFragment() {
		return ccmAssessmentClassificationsFragment;
	}

	public void setCcmAssessmentClassificationsFragment(CcmAssessmentClassificationsFragment ccmAssessmentClassificationsFragment) {
		this.ccmAssessmentClassificationsFragment = ccmAssessmentClassificationsFragment;
	}
}
