package ie.ucc.bis.supportinglife.assessment.imci.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.imci.ui.ImciAssessmentTreatmentsFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: IMCI Treatments
 * 
 * Treatments Tab in IMCI Assessment Results
 * 
 * Responsible for capturing data analytics in relation to
 * IMCI Treatments tab
 * 
 * @author timothyosullivan
 */

public class ImciTreatmentsPage extends AbstractAnalyticsPage {
	
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_IMCI_TREATMENTS_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_IMCI_TREATMENTS_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_IMCI_TREATMENTS_PAGE_DURATION";
	
    private ImciAssessmentTreatmentsFragment imciAssessmentTreatmentsFragment;
	
    @Override
    public Fragment createFragment() {
    	setImciAssessmentTreatmentsFragment(ImciAssessmentTreatmentsFragment.create(getKey()));
        return getImciAssessmentTreatmentsFragment();
    }

	public ImciTreatmentsPage(String title) {
		super(title);
	}

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the IMCI Treatments page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }

	public ImciAssessmentTreatmentsFragment getImciAssessmentTreatmentsFragment() {
		return imciAssessmentTreatmentsFragment;
	}

	public void setImciAssessmentTreatmentsFragment(ImciAssessmentTreatmentsFragment imciAssessmentTreatmentsFragment) {
		this.imciAssessmentTreatmentsFragment = imciAssessmentTreatmentsFragment;
	}
}
