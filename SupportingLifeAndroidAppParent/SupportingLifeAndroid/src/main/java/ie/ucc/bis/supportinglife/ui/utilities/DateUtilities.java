package ie.ucc.bis.supportinglife.ui.utilities;

import ie.ucc.bis.supportinglife.assessment.model.listener.DateDialogSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * Class providing Date/Time utility methods 
 * 
 * @author timothyosullivan
 */
public class DateUtilities {

	public static final String DATE_TIME_CUSTOM_FORMAT = "dd MMMM yyyy HH:mm";
	public static final String DATE_CUSTOM_FORMAT = "dd MMMM yyyy";
	public static final Locale LOCALE = Locale.UK;
	
	private static final int NUMBER_OF_MONTHS_IN_YEAR = 12;
	private static final int MILLISECONDS_IN_A_SECOND = 1000;
	private static final int SECONDS_IN_A_MINUTE = 60;
	private static final int MINUTES_IN_A_HOUR = 60;
	private static final int HOURS_IN_A_DAY = 24;

	/**
	 * Utility method to retrieve the number of months difference between
	 * two dates
	 * 
	 * @param earliestDate
	 * @param latestDate
	 * @return
	 */
	public static int getDiffMonths(Date earliestDate, Date latestDate) {
	    Calendar earliestCalendar = getCalendar(earliestDate);
	    Calendar latestCalendar = getCalendar(latestDate);
	    
	    int monthsDifference = (latestCalendar.get(Calendar.YEAR) - earliestCalendar.get(Calendar.YEAR)) * NUMBER_OF_MONTHS_IN_YEAR;
	    
	    if (latestCalendar.get(Calendar.MONTH) > earliestCalendar.get(Calendar.MONTH)) {
	    	monthsDifference += latestCalendar.get(Calendar.MONTH) - earliestCalendar.get(Calendar.MONTH);
	    }
	    else if (earliestCalendar.get(Calendar.MONTH) > latestCalendar.get(Calendar.MONTH)) {
	    	monthsDifference -= earliestCalendar.get(Calendar.MONTH) - latestCalendar.get(Calendar.MONTH);
	    }
	    return monthsDifference;
	}
	
	/**
	 * Utility method to retrieve the number of days difference between
	 * two dates
	 * 
	 * @param earliestDate
	 * @param latestDate
	 * @return
	 */
	public static int getDiffDays(Date earliestDate, Date latestDate) {
		return Long.valueOf((latestDate.getTime() - earliestDate.getTime())
				/ (MILLISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE * MINUTES_IN_A_HOUR * HOURS_IN_A_DAY)).intValue();
	}	
	
	/**
	 * Utility method to obtain a TimeStamp of a number of years ago from today
	 * 
	 * @param years
	 * @return long - TimeStamp in milliseconds
	 */
	public static long retrieveTimeStampWithYearsSubtracted(int years) {
	    Calendar cal = Calendar.getInstance(Locale.UK);
	    // convert years to negative value
	    cal.add(Calendar.YEAR, (years * -1));
	    return cal.getTimeInMillis();
	}
	
	public static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance(Locale.UK);
	    cal.setTime(date);
	    return cal;
	}
	
	/**
	 * Utility method to return today's date timestamp
	 * 
	 * @return String - Date TimeStamp
	 */
	public static Date getTodaysDateTimestamp() {
		Calendar cal = Calendar.getInstance(Locale.UK);
		return cal.getTime();
	}
	
	/**
	 * Utility method to return today's date
	 * 
	 * @return String - Today's Date in String Representation
	 */
	public static String getTodaysDate() {
		Calendar cal = Calendar.getInstance(Locale.UK);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtilities.DATE_CUSTOM_FORMAT, DateDialogSetListener.LOCALE);
				
		return dateFormat.format(cal.getTime());
	}
	
	/**
	 * Parse and format string value representing date
	 * 
	 * @param string
	 * 
	 * @return Date
	 * @throws ParseException 
	 */
	public static Date parseDate(String dateValue, String slDateFormat) throws ParseException {
		if (dateValue != null) {
			Date dateInstance = new SimpleDateFormat(slDateFormat, LOCALE)
										.parse(dateValue);
			return dateInstance;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Convert a Date instance value to it's  
	 * string value equivalent
	 * 
	 * @param Date
	 * 
	 * @return Date
	 * @throws ParseException 
	 */
	public static String formatDate(Date date, String slDateFormat) throws ParseException {
		if (date != null) {
			String dateString = new SimpleDateFormat(slDateFormat, DateDialogSetListener.LOCALE)
										.format(date);
			return dateString;
		}
		else {
			return null;
		}
	}	
}
