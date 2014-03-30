package ie.ucc.bis.supportinglife.analytics;

import ie.ucc.bis.supportinglife.assessment.model.AbstractPage;
/**
 * 
 * @author timothyosullivan
 */

public class AnalyticUtilities {

	public static final String START_PAGE_TIMER_ACTION = "START_PAGE_TIMER";
	public static final String STOP_PAGE_TIMER_ACTION = "START_PAGE_TIMER";
	public static final String DURATION_PAGE_TIMER_ACTION = "DURATION_PAGE_TIMER";	

	private static final String TIMER_CATEGORY = "TIMER";	
	private static final int MILLISECONDS_IN_A_SECOND = 1000;
	
	/**
	 * Utility method to configure/initialise a timer associated with an 
	 * assessment page
	 * 
	 * @param page
	 * @param analtyicsDataKey
	 */
	public static void configurePageTimer(AbstractPage page, String analtyicsDataKey, String timerAction) {
		
		// firstly check that the timestamp for this timer has not been already set for this assessment
		if (page.getPageData().get(analtyicsDataKey) == null) {	
			// get the current timestamp
			int timeStamp = Long.valueOf(System.currentTimeMillis() / MILLISECONDS_IN_A_SECOND).intValue();	
			DataAnalytic dataAnalytic = new DataAnalytic(TIMER_CATEGORY, timerAction, analtyicsDataKey, timeStamp);
			page.getPageData().putSerializable(analtyicsDataKey, dataAnalytic);
		}
	}

	/**
	 * Utility method to determine the duration between two analytic timers and to store the result
	 * in a separate data analytic
	 * 
	 * @param page
	 * @param analtyicsDataKey
	 */
	public static void determineTimerDuration(AbstractPage page, String analtyicsDataKey, String timerAction,
											DataAnalytic startTimerAnalytic, DataAnalytic stopTimerAnalytic) {
		int duration = stopTimerAnalytic.getValue() - startTimerAnalytic.getValue();
		
		DataAnalytic dataAnalytic = new DataAnalytic(TIMER_CATEGORY, timerAction, analtyicsDataKey, duration);
		page.getPageData().putSerializable(analtyicsDataKey, dataAnalytic);
	}
}
