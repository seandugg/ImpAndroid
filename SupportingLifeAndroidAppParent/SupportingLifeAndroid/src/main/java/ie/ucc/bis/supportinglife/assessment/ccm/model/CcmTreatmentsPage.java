package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmAssessmentTreatmentsFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Treatments
 * 
 * Treatments Tab in CCM Assessment Results
 * 
 * Responsible for capturing data analytics in relation to
 * CCM Treatments tab
 * 
 * @author timothyosullivan
 */

public class CcmTreatmentsPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_CCM_TREATMENTS_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_CCM_TREATMENTS_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_CCM_TREATMENTS_PAGE_DURATION";
	
    private CcmAssessmentTreatmentsFragment ccmAssessmentTreatmentsFragment;
	
    @Override
    public Fragment createFragment() {
    	setCcmAssessmentTreatmentsFragment(CcmAssessmentTreatmentsFragment.create(getKey()));
        return getCcmAssessmentTreatmentsFragment();
    }

	public CcmTreatmentsPage(String title) {
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

	public CcmAssessmentTreatmentsFragment getCcmAssessmentTreatmentsFragment() {
		return ccmAssessmentTreatmentsFragment;
	}

	public void setCcmAssessmentTreatmentsFragment(CcmAssessmentTreatmentsFragment ccmAssessmentTreatmentsFragment) {
		this.ccmAssessmentTreatmentsFragment = ccmAssessmentTreatmentsFragment;
	}
}
