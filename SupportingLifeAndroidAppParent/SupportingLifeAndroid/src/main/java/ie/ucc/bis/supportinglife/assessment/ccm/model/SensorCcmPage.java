package ie.ucc.bis.supportinglife.assessment.ccm.model;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.ui.SensorCcmFragment;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.ModelCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;

import java.util.ArrayList;

import android.content.res.Resources;
import android.support.v4.app.Fragment;

/**
 * AnalyticsPage Title: CCM Sensor Assessment
 * 
 * Stage in CCM bread-crumb UI Wizard: 5
 * 
 * Responsible for displaying form for CCM 'Zephyr Sensor' assessment
 * 
 * @author timothyosullivan
 */
public class SensorCcmPage extends AbstractAssessmentPage {
	public static final String HEART_RATE_DATA_KEY = "HEART_RATE";
    public static final String RESPIRATION_RATE_DATA_KEY = "RESPIRATION_RATE";
    public static final String SKIN_TEMPERATURE_DATA_KEY = "SKIN_TEMPERATURE";
    public static final String VITAL_SIGN_READINGS_ACCEPTED_DATA_KEY = "VITAL_SIGN_READINGS_ACCEPTED";
    
    // ANALYTICS DATA KEYS
    public static final String ANALTYICS_START_PAGE_TIMER_DATA_KEY = "ANALYTICS_ZEPHYR_SENSOR_CCM_PAGE_START_PAGE_TIMER";
    public static final String ANALTYICS_STOP_PAGE_TIMER_DATA_KEY = "ANALYTICS_ZEPHYR_SENSOR_CCM_PAGE_STOP_PAGE_TIMER";
    public static final String ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY = "ANALTYICS_ZEPHYR_SENSOR_CCM_PAGE_DURATION";
    
    private SensorCcmFragment sensorCcmFragment;

    public SensorCcmPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
    	setSensorCcmFragment(SensorCcmFragment.create(getKey()));
        return getSensorCcmFragment();
    }

	/**
	 * Method: getReviewItems
	 * 
	 * Define the review items associated with the 'zephyr sensor assessment' page.
	 * 
	 * @param reviewItems : ArrayList<ReviewItem>
	 */      
    @Override
    public void getReviewItems(ArrayList<ReviewItem> reviewItems) {
    	Resources resources = ((AbstractAssessmentModel) getModelCallbacks()).getApplicationContext().getResources();
    	String reviewItemIdentifier = null;
    	String reviewItemLabel = null;
    	String reviewItemValue = null;
    	String reviewItemSymptomId = null;

    	boolean sensorReadingsAccepted = getPageData().getBoolean(SensorCcmPage.VITAL_SIGN_READINGS_ACCEPTED_DATA_KEY);
    	    	
    	// review header
    	reviewItemLabel = resources.getString(R.string.ccm_sensor_assessment_title);
    	reviewItems.add(new ReviewItem(reviewItemLabel, getKey()));	
      	    	    	    	    	
    	// heart rate
    	reviewItemIdentifier = resources.getString(R.string.ccm_sensor_assessment_heart_rate_id);
    	reviewItemLabel = resources.getString(R.string.ccm_sensor_assessment_review_heart_rate);
    	reviewItemValue = getSensorReading(sensorReadingsAccepted, HEART_RATE_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_sensor_assessment_heart_rate_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
    	
    	// respiration rate
    	reviewItemIdentifier = resources.getString(R.string.ccm_sensor_assessment_respiration_rate_id);
    	reviewItemLabel = resources.getString(R.string.ccm_sensor_assessment_review_respiration_rate);
    	reviewItemValue = getSensorReading(sensorReadingsAccepted, RESPIRATION_RATE_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_sensor_assessment_respiration_rate_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));
    	
    	// skin temperature
    	reviewItemIdentifier = resources.getString(R.string.ccm_sensor_assessment_skin_temperature_id);
    	reviewItemLabel = resources.getString(R.string.ccm_sensor_assessment_review_skin_temperature);
    	reviewItemValue = getSensorReading(sensorReadingsAccepted, SKIN_TEMPERATURE_DATA_KEY);
    	reviewItemSymptomId = resources.getString(R.string.ccm_sensor_assessment_skin_temperature_symptom_id);
    	reviewItems.add(new ReviewItem(reviewItemLabel, reviewItemValue, reviewItemSymptomId, getKey(), -1, reviewItemIdentifier));    	
    }

	/**
	 * Responsible for retrieving the sensor reading value requested. Will return
	 * a null value if a full reading of the sensors (i.e. 15/30/45/60 seconds) was
	 * not completed.
	 * 
	 * @param sensorReadingsAccepted
	 * @param dataKey
	 * 
	 * @return sensor reading
	 */
	private String getSensorReading(boolean sensorReadingsAccepted, String dataKey) {
		String reviewItemValue;
		if (sensorReadingsAccepted) {
    		reviewItemValue = getPageData().getString(dataKey);
    	}
    	else {
    		reviewItemValue = null;
    	}
		return reviewItemValue;
	}

    /**
     * Method: getDataAnalytics
     * 
     * Define the data analytics associated with the 'zephyr sensor assessment' page.
     * 
     * @param dataAnalytics : ArrayList<DataAnalytic>
     */      
    @Override
    public void getDataAnalytics(ArrayList<DataAnalytic> dataAnalytics) {
    	// add the duration page timer
    	dataAnalytics.add((DataAnalytic) getPageData().getSerializable(ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY));
    }
    
    @Override
    public boolean isCompleted() {
    	return true;
    }

	/**
	 * Getter Method: getSensorCcmFragment()
	 */    
	public SensorCcmFragment getSensorCcmFragment() {
		return sensorCcmFragment;
	}

	/**
	 * Setter Method: setSensorCcmFragment()
	 */		
	public void setSensorCcmFragment(SensorCcmFragment sensorCcmFragment) {
		this.sensorCcmFragment = sensorCcmFragment;
	}
}
