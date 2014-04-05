package ie.ucc.bis.supportinglife.assessment.imci.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.imci.ui.ImciAssessmentClassificationsFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: IMCI Classifications
 * 
 * Classifications Tab in IMCI Assessment Results
 * 
 * Responsible for capturing data analytics in relation to
 * IMCI Classifications tab
 * 
 * @author timothyosullivan
 */

public class ImciClassificationsPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_IMCI_CLASSIFICATIONS_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_IMCI_CLASSIFICATIONS_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_IMCI_CLASSIFICATIONS_PAGE_DURATION";
	
    private ImciAssessmentClassificationsFragment imciAssessmentClassificationsFragment;
	
    @Override
    public Fragment createFragment() {
    	setImciAssessmentClassificationsFragment(ImciAssessmentClassificationsFragment.create(getKey()));
        return getImciAssessmentClassificationsFragment();
    }

	public ImciClassificationsPage(String title) {
		super(title);
	}

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the IMCI Classifications page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }

	public ImciAssessmentClassificationsFragment getImciAssessmentClassificationsFragment() {
		return imciAssessmentClassificationsFragment;
	}

	public void setImciAssessmentClassificationsFragment(ImciAssessmentClassificationsFragment imciAssessmentClassificationsFragment) {
		this.imciAssessmentClassificationsFragment = imciAssessmentClassificationsFragment;
	}
}
