package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.GeneralPatientDetailsCcmPage;
import ie.ucc.bis.supportinglife.assessment.ccm.model.SensorCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Responsible for UI fragment to display CCM 
 * 'zephyr' sensor assessment form
 * 
 * @author timothyosullivan
 * 
 */
public class SensorCcmFragment extends Fragment implements FragmentLifecycle {
	    
    private SensorCcmPage sensorCcmPage;    
    private PageFragmentCallbacks pageFragmentCallbacks;
    private String pageKey;
    
    private TextView heartRateTextView;
    private TextView respirationRateTextView;
    private TextView skinTemperatureTextView;

    public static SensorCcmFragment create(String pageKey) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_KEY, pageKey);

        SensorCcmFragment fragment = new SensorCcmFragment();
        fragment.setArguments(args);
        return fragment;
    }

	/**
	 * Constructor
	 *
	 */
    public SensorCcmFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        setPageKey(args.getString(ARG_PAGE_KEY));
    }
        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setSensorCcmPage((SensorCcmPage) getPageFragmentCallbacks().getPage(getPageKey()));
        
        View rootView = inflater.inflate(R.layout.fragment_ccm_page_sensor_assessment, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(getSensorCcmPage().getTitle());

        // heart rate
        setHeartRateTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_heart_rate)));
        getHeartRateTextView().setText(getSensorCcmPage().getPageData().getString(SensorCcmPage.HEART_RATE_DATA_KEY));
        
        // respiration rate
        setRespirationRateTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_respiration_rate)));
        getRespirationRateTextView().setText(getSensorCcmPage().getPageData().getString(SensorCcmPage.RESPIRATION_RATE_DATA_KEY));
        
        // skin temperature
        setSkinTemperatureTextView(((TextView) rootView.findViewById(R.id.ccm_sensor_assessment_skin_temperature)));
        getSkinTemperatureTextView().setText(getSensorCcmPage().getPageData().getString(SensorCcmPage.SKIN_TEMPERATURE_DATA_KEY));
        
		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		((SupportingLifeBaseActivity) getActivity()).addSoftKeyboardHandling(rootView);

        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        } 
        setPageFragmentCallbacks((PageFragmentCallbacks) activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setPageFragmentCallbacks(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
	
    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (assessmentPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(assessmentPage, GeneralPatientDetailsCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(assessmentPage,
					GeneralPatientDetailsCcmPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) assessmentPage.getPageData().getSerializable(GeneralPatientDetailsCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) assessmentPage.getPageData().getSerializable(GeneralPatientDetailsCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (assessmentPage != null) {			
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(assessmentPage, GeneralPatientDetailsCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}
    }
    
	/**
	 * Getter Method: getSensorCcmPage()
	 */
	public SensorCcmPage getSensorCcmPage() {
		return sensorCcmPage;
	}

	/**
	 * Setter Method: setSensorCcmPage()
	 */   	
	public void setSensorCcmPage(SensorCcmPage sensorCcmPage) {
		this.sensorCcmPage = sensorCcmPage;
	}

	/**
	 * Getter Method: getPageFragmentCallbacks()
	 */
	public PageFragmentCallbacks getPageFragmentCallbacks() {
		return pageFragmentCallbacks;
	}

	/**
	 * Setter Method: setPageFragmentCallbacks()
	 */
	public void setPageFragmentCallbacks(PageFragmentCallbacks pageFragmentCallbacks) {
		this.pageFragmentCallbacks = pageFragmentCallbacks;
	}
	
	/**
	 * Getter Method: getPageKey()
	 */	
	public String getPageKey() {
		return pageKey;
	}

	/**
	 * Setter Method: setPageKey()
	 */	
	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public TextView getHeartRateTextView() {
		return heartRateTextView;
	}

	public void setHeartRateTextView(TextView heartRateTextView) {
		this.heartRateTextView = heartRateTextView;
	}

	public TextView getRespirationRateTextView() {
		return respirationRateTextView;
	}

	public void setRespirationRateTextView(TextView respirationRateTextView) {
		this.respirationRateTextView = respirationRateTextView;
	}

	public TextView getSkinTemperatureTextView() {
		return skinTemperatureTextView;
	}

	public void setSkinTemperatureTextView(TextView skinTemperatureTextView) {
		this.skinTemperatureTextView = skinTemperatureTextView;
	}
}
