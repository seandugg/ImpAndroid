package ie.ucc.bis.supportinglife.assessment.imci.model;

import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.imci.ui.ImciReviewFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;

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

public class ImciReviewPage extends AbstractAnalyticsPage {
		
    private ImciReviewFragment imciReviewFragment;
	
    @Override
    public Fragment createFragment() {
    	setImciReviewFragment(ImciReviewFragment.create(getKey()));
        return getImciReviewFragment();
    }

	public ImciReviewPage(String title) {
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
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {}


	public ImciReviewFragment getImciReviewFragment() {
		return imciReviewFragment;
	}

	public void setImciReviewFragment(ImciReviewFragment imciReviewFragment) {
		this.imciReviewFragment = imciReviewFragment;
	}
}
