package ie.ucc.bis.supportinglife.analytics;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import android.content.Context;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
/**
 * 
 * @author timothyosullivan
 */

public class AnalyticUtilities {

	public static final String START_PAGE_TIMER_ACTION = "START_PAGE_TIMER";
	public static final String STOP_PAGE_TIMER_ACTION = "STOP_PAGE_TIMER";
	public static final String DURATION_PAGE_TIMER_ACTION = "DURATION_PAGE_TIMER";	

	private static final String TIMER_CATEGORY = "TIMER";	
	private static final int MILLISECONDS_IN_A_SECOND = 1000;
	
	/**
	 * Utility method to configure/initialise a timer associated with an 
	 * assessment page
	 * 
	 * @param AbstractAnalyticsPage
	 * @param analtyicsDataKey
	 * @param timerAction
	 */
	public static void configurePageTimer(AbstractAnalyticsPage page, String analtyicsDataKey, String timerAction) {
		
		boolean authoriseUpload = false;
		
		// get the current timestamp
		Long timeStamp = Long.valueOf(System.currentTimeMillis() / MILLISECONDS_IN_A_SECOND);	
		DataAnalytic dataAnalytic = new DataAnalytic(TIMER_CATEGORY, timerAction, analtyicsDataKey, timeStamp, authoriseUpload);
		page.getPageData().putSerializable(analtyicsDataKey, dataAnalytic);
	}

	/**
	 * Utility method to determine the duration between two analytic timers and to store the result
	 * in a separate data analytic
	 * 
	 * @param AbstractAnalyticsPage
	 * @param analtyicsDataKey
	 * @param timerAction
	 * @param startTimerAnalytic
	 * @param stopTimerAnalytic
	 * 
	 */
	public static void determineTimerDuration(AbstractAnalyticsPage page, String analtyicsDataKey, String timerAction,
											DataAnalytic startTimerAnalytic, DataAnalytic stopTimerAnalytic) {
		DataAnalytic dataAnalytic;
		boolean authoriseUpload = true;
		
		Long duration = stopTimerAnalytic.getValue() - startTimerAnalytic.getValue();
		
		if (page.getPageData().get(analtyicsDataKey) == null) {
			dataAnalytic = new DataAnalytic(TIMER_CATEGORY, timerAction, analtyicsDataKey, duration, authoriseUpload);
		}
		else {
			dataAnalytic = (DataAnalytic) page.getPageData().get(analtyicsDataKey);
			dataAnalytic.setValue(dataAnalytic.getValue() + duration);
		}
		
		page.getPageData().putSerializable(analtyicsDataKey, dataAnalytic);
	}
	
	/**
	 * Responsible for recording any data analytic events logged with any 
	 * individual page views
	 * 
	 * @param assessmentModel 
	 * @param context 
	 */		
	public static void recordDataAnalytics(Context context, AbstractModel model) {
		// need to take note of the data analytics associated with the patient assessment pages
		Tracker tracker = GoogleAnalytics.getInstance(context).getDefaultTracker();
		
		// configure demographic information of user
		String ageRange = "25-39";
		String gender = "male";
		
		tracker.setCustomDimension(1, ageRange);
		tracker.setCustomDimension(2, gender);
		// Dimension value is associated and sent with this hit.
		tracker.sendView();
		
		// check if any events registered with a question focused assessment
		if (model instanceof AbstractAssessmentModel) {
			for (DataAnalytic dataAnalyticItem : ((AbstractAssessmentModel) model).gatherPageDataAnalytics()) {
				if (dataAnalyticItem != null) {
					tracker.sendEvent(dataAnalyticItem.getCategory(), dataAnalyticItem.getAction(), 
							dataAnalyticItem.getLabel(), dataAnalyticItem.getValue());
				}
			}
		}

		// check if any events registered with a results focused assessment
		for (DataAnalytic dataAnalyticItem : model.gatherPageDataAnalytics()) {
			if (dataAnalyticItem != null) {
				tracker.sendEvent(dataAnalyticItem.getCategory(), dataAnalyticItem.getAction(), 
						dataAnalyticItem.getLabel(), dataAnalyticItem.getValue());
			}
		}
	}
}
