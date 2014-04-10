package ie.ucc.bis.supportinglife.helper;

import ie.ucc.bis.supportinglife.assessment.model.listener.DateDialogSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHandlerUtils {

	public static final String DATE_TIME_CUSTOM_FORMAT = "dd MMMM yyyy HH:mm";
	public static final String BIRTH_DATE_CUSTOM_FORMAT = "dd MMMM yyyy";
	public static final Locale LOCALE = Locale.UK;
	
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
