package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentResultsActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.CcmTreatmentAdapter;
import ie.ucc.bis.supportinglife.assessment.ccm.model.CcmTreatmentsPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAnalyticsPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.rule.engine.Diagnostic;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Class: CcmAssessmentTreatmentsFragment
 * 
 * Responsible for displaying the assessment 
 * treatments of a patient
 * 
 * @author TOSullivan
 *
 */
public class CcmAssessmentTreatmentsFragment extends ListFragment implements FragmentLifecycle {
    
	private static final String NO_TREATMENTS = "No treatments apply based on patient assessment";
	
	private String pageKey;
    private CcmTreatmentAdapter ccmTreatmentAdapter;
    private PatientAssessment patient;
	
	public static CcmAssessmentTreatmentsFragment create(String pageKey) {
		Bundle args = new Bundle();
		args.putString(ARG_PAGE_KEY, pageKey);

		CcmAssessmentTreatmentsFragment fragment = new CcmAssessmentTreatmentsFragment();
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
        setCcmTreatmentAdapter(new CcmTreatmentAdapter(this, new ArrayList<Diagnostic>(getPatient().getDiagnostics())));
        setListAdapter(getCcmTreatmentAdapter());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	  View myFragmentView = inflater.inflate(R.layout.fragment_assessment_results_treatment_tab, container, false);
          
          ListView listView = (ListView) myFragmentView.findViewById(android.R.id.list);         
          listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    
    	  return myFragmentView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {}
    
    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAnalyticsPage analyticsPage = (AbstractAnalyticsPage) assessmentModel.findAnalyticsPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (analyticsPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(analyticsPage, CcmTreatmentsPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(analyticsPage,
													CcmTreatmentsPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) analyticsPage.getPageData().getSerializable(CcmTreatmentsPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) analyticsPage.getPageData().getSerializable(CcmTreatmentsPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAnalyticsPage analyticsPage = (AbstractAnalyticsPage) assessmentModel.findAnalyticsPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (analyticsPage != null) {
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(analyticsPage, CcmTreatmentsPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}    	
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { 
            performTreatmentCheck();
        }
    }
    
	/**
	 * Check if any treatments apply in this case and inform
	 * user if the case applies where there are no treatments 
	 * applicable to the assessment performed.
	 */
	private void performTreatmentCheck() {
		// check if we are dealing with a situation where no classifications apply - if so, inform user
		if (getPatient() != null && getPatient().getDiagnostics().size() == 0) {
			Crouton.clearCroutonsForActivity(this.getActivity());
			Crouton.makeText(this.getActivity(), NO_TREATMENTS, Style.INFO).show();  
		}
	}
    
	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
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

	/**
	 * Getter Method: getCcmTreatmentAdapter()
	 */
	public CcmTreatmentAdapter getCcmTreatmentAdapter() {
		return ccmTreatmentAdapter;
	}
	
	/**
	 * Setter Method: setCcmTreatmentAdapter()
	 */
	public void setCcmTreatmentAdapter(CcmTreatmentAdapter ccmTreatmentAdapter) {
		this.ccmTreatmentAdapter = ccmTreatmentAdapter;
	}
}
