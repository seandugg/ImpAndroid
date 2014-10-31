package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.CcmAssessmentTreatmentsFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Treatments
 * 
 * Treatments Tab in CCM Assessment Results
 * 
 * Responsible for capturing data analytics and treatment administration recording 
 * in relation to
 * CCM Treatments tab
 * 
 * @author timothyosullivan
 */

public class CcmTreatmentsPage extends AbstractAssessmentPage {
	
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
		// in this case the model is null as we are post assessment pages
		super(null, title);
	}
	
    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the CCM Treatments page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }

	@Override
	public void getReviewItems(ArrayList<ReviewItem> reviewItems) {
		// do nothing		
	}
    
	public CcmAssessmentTreatmentsFragment getCcmAssessmentTreatmentsFragment() {
		return ccmAssessmentTreatmentsFragment;
	}

	public void setCcmAssessmentTreatmentsFragment(CcmAssessmentTreatmentsFragment ccmAssessmentTreatmentsFragment) {
		this.ccmAssessmentTreatmentsFragment = ccmAssessmentTreatmentsFragment;
	}
}
