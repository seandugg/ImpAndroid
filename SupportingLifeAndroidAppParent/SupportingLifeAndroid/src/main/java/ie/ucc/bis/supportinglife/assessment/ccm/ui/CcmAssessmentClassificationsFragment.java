package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentResultsActivity;
import ie.ucc.bis.supportinglife.activity.CcmAssessmentResultsActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.CcmClassificationAdapter;
import ie.ucc.bis.supportinglife.assessment.ccm.model.CcmClassificationsPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.rule.engine.Diagnostic;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Class: CcmAssessmentClassificationsFragment
 * 
 * Responsible for displaying the CCM Assessment 
 * Classifications of a patient
 * 
 * @author TOSullivan
 *
 */
public class CcmAssessmentClassificationsFragment extends ListFragment implements FragmentLifecycle {
    
	private static final String NO_CLASSIFICATIONS = "No classifications apply based on patient assessment";
	
	private String pageKey;
    private CcmClassificationAdapter ccmClassificationAdapter;
    private PatientAssessment patient;
	
	public static CcmAssessmentClassificationsFragment create(String pageKey) {
		Bundle args = new Bundle();
		args.putString(ARG_PAGE_KEY, pageKey);

		CcmAssessmentClassificationsFragment fragment = new CcmAssessmentClassificationsFragment();
		fragment.setArguments(args);
		return fragment;
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Bundle args = getArguments();
		setPageKey(args.getString(ARG_PAGE_KEY));
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      
        // obtain a reference to the patient being dealt with...
        setPatient(((AssessmentResultsActivity) getActivity()).getPatientAssessment());          
        setCcmClassificationAdapter(new CcmClassificationAdapter(this, new ArrayList<Diagnostic>(getPatient().getDiagnostics())));
        setListAdapter(getCcmClassificationAdapter());
        
        // check if we are dealing with a situation where no classifications apply - if so, inform user
        performClassificationCheck();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	  View myFragmentView = inflater.inflate(R.layout.fragment_assessment_results_classification_tab, container, false);
          
          ListView listView = (ListView) myFragmentView.findViewById(android.R.id.list);         
          listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    	  return myFragmentView;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Activity activity = getActivity();
        
        if (activity != null) {   
            // open the treatments tab and scroll to the relevant treatment
        	String classificationTitle = ((TextView) v.findViewById(R.id.classification_list_item_label)).getText().toString();
			((CcmAssessmentResultsActivity) getActivity()).displayTreatmentTab(classificationTitle);
        }
    }
    
    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAnalyticsPage analyticsPage = (AbstractAnalyticsPage) assessmentModel.findAnalyticsPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (analyticsPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(analyticsPage, CcmClassificationsPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(analyticsPage,
													CcmClassificationsPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) analyticsPage.getPageData().getSerializable(CcmClassificationsPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) analyticsPage.getPageData().getSerializable(CcmClassificationsPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAnalyticsPage analyticsPage = (AbstractAnalyticsPage) assessmentModel.findAnalyticsPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (analyticsPage != null) {
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(analyticsPage, CcmClassificationsPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { 
            performClassificationCheck();
        }
    }

	/**
	 * Check if any classifications apply in this case and inform
	 * user if the case applies where there are no classifications 
	 * applicable to the assessment performed.
	 */
	private void performClassificationCheck() {
		// check if we are dealing with a situation where no classifications apply - if so, inform user
		if (getPatient() != null && getPatient().getDiagnostics().size() == 0) {
			Crouton.clearCroutonsForActivity(this.getActivity());
			Crouton.makeText(this.getActivity(), NO_CLASSIFICATIONS, Style.INFO).show();  
		}
	}
    
	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	/**
	 * Getter Method: getCcmClassificationAdapter()
	 */	
	private CcmClassificationAdapter getCcmClassificationAdapter() {
		return ccmClassificationAdapter;
	}

	/**
	 * Setter Method: setCcmClassificationAdapter()
	 */
	private void setCcmClassificationAdapter(CcmClassificationAdapter ccmClassificationAdapter) {
		this.ccmClassificationAdapter = ccmClassificationAdapter;
	}

	/**
	 * Getter Method: getPatient()
	 */
	public PatientAssessment getPatient() {
		return patient;
	}

	/**
	 * Setter Method: setPatient()
	 */
	private void setPatient(PatientAssessment patient) {
		this.patient = patient;
	}
}
