package ie.ucc.bis.supportinglife.analytics;

import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;
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
}
