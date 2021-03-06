package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.assessment.imci.ui.DatePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;

/**
 * 
 * @author timothyosullivan
 */

public class DateDialogSetListener implements OnDateSetListener {
	
	public static final Locale LOCALE = Locale.UK;
	
	private DatePickerDialogFragment datePickerDialogFragment;
	private String slDateFormat;
	
	/**
	 * Constructor
	 * 
	 * @param dateEditText
	 */
	public DateDialogSetListener(DatePickerDialogFragment datePickerDialogFragment, String slDateFormat) {
		setDatePickerDialogFragment(datePickerDialogFragment);
		setSlDateFormat(slDateFormat);
	}


	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getSlDateFormat(), LOCALE);
				
		String dateString = simpleDateFormat.format(calendar.getTime());
		
		// update data record associated with this page
		getDatePickerDialogFragment().getPage().getPageData().putString(getDatePickerDialogFragment().getDataKey(),
                (dateString != null) ? dateString : null);
		getDatePickerDialogFragment().getPage().notifyDataChanged();
		
		// update view with date selected and toggle focus off
		getDatePickerDialogFragment().getDateEditText().setText(dateString);
		getDatePickerDialogFragment().getDateEditText().clearFocus();
	}

	/**
	 * Getter Method: getDatePickerDialogFragment()
	 */
	public DatePickerDialogFragment getDatePickerDialogFragment() {
		return datePickerDialogFragment;
	}

	/**
	 * Setter Method: setDatePickerDialogFragment()
	 */
	public void setDatePickerDialogFragment(DatePickerDialogFragment datePickerDialogFragment) {
		this.datePickerDialogFragment = datePickerDialogFragment;
	}


	public String getSlDateFormat() {
		return slDateFormat;
	}


	public void setSlDateFormat(String slDateFormat) {
		this.slDateFormat = slDateFormat;
	}

}
