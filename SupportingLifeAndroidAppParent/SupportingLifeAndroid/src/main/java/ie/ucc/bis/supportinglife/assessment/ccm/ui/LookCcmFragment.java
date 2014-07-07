package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.LookCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.listener.BreathCountTextWatcher;
import ie.ucc.bis.supportinglife.assessment.model.listener.BreathCounterDialogListener;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupListener;
import ie.ucc.bis.supportinglife.ui.custom.ToggleButtonGroupTableLayout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Responsible for UI fragment to display CCM 
 * 'Look' assessment form
 * 
 * @author timothyosullivan
 * 
 */
public class LookCcmFragment extends Fragment implements FragmentLifecycle {
    
	private static final String BREATH_COUNTER_NOT_APPLICABLE = "Not Applicable";
	static final String BREATHE_ICON_TYPEFACE_ASSET = "fonts/breathe-flaticon.ttf";
	
	private LookCcmPage lookCcmPage;    
	private PageFragmentCallbacks pageFragmentCallbacks;
	private String pageKey;
	private RadioGroup chestIndrawingRadioGroup;
	private RadioGroup verySleepyOrUnconsciousRadioGroup;
	private RadioGroup palmarPallorRadioGroup;
	private ToggleButtonGroupTableLayout muacTapeCustomRadioGroup;
	private RadioGroup swellingOfBothFeetRadioGroup;
	private EditText breathsPerMinuteEditText;
	
	// Breath Count Feature
	private Button breatheButton;
	private BreathCountTextWatcher breathCountTextWatcher;
	
	public static LookCcmFragment create(String pageKey) {
		Bundle args = new Bundle();
		args.putString(ARG_PAGE_KEY, pageKey);

		LookCcmFragment fragment = new LookCcmFragment();
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Constructor
	 *
	 */
	public LookCcmFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		setPageKey(args.getString(ARG_PAGE_KEY));
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setLookCcmPage((LookCcmPage) getPageFragmentCallbacks().getPage(getPageKey()));
		
		View rootView = inflater.inflate(R.layout.fragment_ccm_page_look_assessment, container, false);
		((TextView) rootView.findViewById(android.R.id.title)).setText(getLookCcmPage().getTitle());

		// chest indrawing
		setChestIndrawingRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_look_assessment_radio_chest_indrawing));
		getChestIndrawingRadioGroup().check(getLookCcmPage().getPageData().getInt(LookCcmPage.CHEST_INDRAWING_DATA_KEY));

		// breaths per minute
		setBreathsPerMinuteEditText(((EditText) rootView.findViewById(R.id.ccm_look_assessment_breaths_per_minute)));
		getBreathsPerMinuteEditText().setText(getLookCcmPage().getPageData().getString(LookCcmPage.BREATHS_PER_MINUTE_DATA_KEY));

		// very sleepy or unconscious
		setVerySleepyOrUnconsciousRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_look_assessment_radio_very_sleepy_or_unconscious));
		getVerySleepyOrUnconsciousRadioGroup().check(getLookCcmPage().getPageData().getInt(LookCcmPage.VERY_SLEEPY_OR_UNCONSCIOUS_DATA_KEY));

		// palmar pallor
		setPalmarPallorRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_look_assessment_radio_palmar_pallor));
		getPalmarPallorRadioGroup().check(getLookCcmPage().getPageData().getInt(LookCcmPage.PALMAR_PALLOR_DATA_KEY));
		
		// muac tape colour
        setMuacTapeCustomRadioGroup((ToggleButtonGroupTableLayout) rootView.findViewById(R.id.ccm_look_assessment_radio_muac_tape_colour));
        getMuacTapeCustomRadioGroup().check(getLookCcmPage().getPageData().getInt(LookCcmPage.MUAC_TAPE_COLOUR_DATA_KEY));
		
		// swelling of both feet
		setSwellingOfBothFeetRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_look_assessment_radio_swelling_of_both_feet));
		getSwellingOfBothFeetRadioGroup().check(getLookCcmPage().getPageData().getInt(LookCcmPage.SWELLING_OF_BOTH_FEET_DATA_KEY));
		
		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		((SupportingLifeBaseActivity) getActivity()).addSoftKeyboardHandling(rootView);
		
		// load icon fonts for breathe
		configureBreatheButtonFontIcon(rootView);

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

		// add listener to chest indrawing radio group
		getChestIndrawingRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(),
						LookCcmPage.CHEST_INDRAWING_DATA_KEY));

		// add listener to 'breaths per minute' textfield
		setBreathCountTextWatcher(new BreathCountTextWatcher(getLookCcmPage(), 
						LookCcmPage.BREATHS_PER_MINUTE_DATA_KEY, LookCcmPage.BREATHS_COUNTER_USED_DATA_KEY,
						LookCcmPage.FULL_BREATH_COUNT_TIME_ASSESSMENT_DATA_KEY));
		getBreathsPerMinuteEditText().addTextChangedListener(getBreathCountTextWatcher());	

		// add listener to 'very sleepy or unconscious' radio group
		getVerySleepyOrUnconsciousRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(),
						LookCcmPage.VERY_SLEEPY_OR_UNCONSCIOUS_DATA_KEY));
		
		// add listener to palmar pallor radio group
		getPalmarPallorRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(),
						LookCcmPage.PALMAR_PALLOR_DATA_KEY));
		
        // add listener to custom muac tape radio group
		getMuacTapeCustomRadioGroup().setPage(getLookCcmPage());
		getMuacTapeCustomRadioGroup().setDataKey(LookCcmPage.MUAC_TAPE_COLOUR_DATA_KEY);
				
		// add listener to 'swelling of both feet' radio group
		getSwellingOfBothFeetRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(),
						LookCcmPage.SWELLING_OF_BOTH_FEET_DATA_KEY));
		
		getBreatheButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        		// need to determine if breath counter has been turned on in user preferences
            	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        		String breathingDurationPreference = settings.getString(SupportingLifeBaseActivity.BREATHING_DURATION_SELECTION_KEY, 
        				String.valueOf(BreathCounterDialogFragment.ONE_MINUTE_IN_SECONDS));
        		if (breathingDurationPreference.equalsIgnoreCase(BREATH_COUNTER_NOT_APPLICABLE) == true) {
        			Crouton.makeText(getActivity(), "Breath Counter Feature Not Enabled", Style.INFO).show();   
        		}
        		else {
        			BreathCounterDialogFragment breathCounterFragment = BreathCounterDialogFragment.create(breathingDurationPreference);
                	
                	breathCounterFragment.setBreathCounterDialogListener(new BreathCounterDialogListener() {
						@Override
						public void dialogClosed(boolean timerComplete, boolean fullTimeAssessment, int breathCount) {
							getBreathCountTextWatcher().setBreathCounterUsed(true);
							getBreathCountTextWatcher().setFullBreathCountTimeAssessment(fullTimeAssessment);
							if (timerComplete && fullTimeAssessment) {
								getBreathsPerMinuteEditText().setText(String.valueOf(breathCount));
								Crouton.makeText(getActivity(), "Breath Count Assessment Complete", Style.INFO).show();
							}
							else if (timerComplete && !fullTimeAssessment) {
								getBreathsPerMinuteEditText().setText(String.valueOf(breathCount));
								Crouton.makeText(getActivity(), "Breath Count Assessment Complete (*Estimated Total)", Style.INFO).show();								
							}
							else {
								Crouton.makeText(getActivity(), "Breath Count Assessment Incomplete", Style.ALERT).show();
							}
						}
					});                	
                	breathCounterFragment.show(getFragmentManager(), "Breath Counter");	
        		}
            }
        });
	}

    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (assessmentPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(assessmentPage, LookCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(assessmentPage,
													LookCcmPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) assessmentPage.getPageData().getSerializable(LookCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) assessmentPage.getPageData().getSerializable(LookCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (assessmentPage != null) {
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(assessmentPage, LookCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}
    }
	
	/**
	 * Responsible for configuring the font icon (provided by FlatIcon)
	 * for the Breathe Icon
	 * 
	 */
	private void configureBreatheButtonFontIcon(View rootView) {
		// load the flaticon font for the buttons
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), BREATHE_ICON_TYPEFACE_ASSET);
		
		setBreatheButton((Button) rootView.findViewById(R.id.ccm_look_assessment_breath_counter_button));
		getBreatheButton().setTypeface(font);
	}
    
	/**
	 * Getter Method: getLookCcmPage()
	 */
	private LookCcmPage getLookCcmPage() {
		return lookCcmPage;
	}

	/**
	 * Setter Method: setLookCcmPage()
	 */   	
	private void setLookCcmPage(LookCcmPage lookCcmPage) {
		this.lookCcmPage = lookCcmPage;
	}

	/**
	 * Getter Method: getPageFragmentCallbacks()
	 */
	private PageFragmentCallbacks getPageFragmentCallbacks() {
		return pageFragmentCallbacks;
	}

	/**
	 * Setter Method: setPageFragmentCallbacks()
	 */
	private void setPageFragmentCallbacks(PageFragmentCallbacks pageFragmentCallbacks) {
		this.pageFragmentCallbacks = pageFragmentCallbacks;
	}

	/**
	 * Getter Method: getPageKey()
	 */	
	private String getPageKey() {
		return pageKey;
	}

	/**
	 * Setter Method: setPageKey()
	 */	
	private void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	/**
	 * Getter Method: getChestIndrawingRadioGroup()
	 */	
	private RadioGroup getChestIndrawingRadioGroup() {
		return chestIndrawingRadioGroup;
	}

	/**
	 * Setter Method: setChestIndrawingRadioGroup()
	 */	
	private void setChestIndrawingRadioGroup(RadioGroup chestIndrawingRadioGroup) {
		this.chestIndrawingRadioGroup = chestIndrawingRadioGroup;
	}

	/**
	 * Getter Method: getVerySleepyOrUnconsciousRadioGroup()
	 */	
	private RadioGroup getVerySleepyOrUnconsciousRadioGroup() {
		return verySleepyOrUnconsciousRadioGroup;
	}

	/**
	 * Setter Method: setVerySleepyOrUnconsciousRadioGroup()
	 */	
	private void setVerySleepyOrUnconsciousRadioGroup(RadioGroup verySleepyOrUnconsciousRadioGroup) {
		this.verySleepyOrUnconsciousRadioGroup = verySleepyOrUnconsciousRadioGroup;
	}

	/**
	 * Getter Method: getPalmarPallorRadioGroup()
	 */	
	private RadioGroup getPalmarPallorRadioGroup() {
		return palmarPallorRadioGroup;
	}

	/**
	 * Setter Method: setPalmarPallorRadioGroup()
	 */	
	private void setPalmarPallorRadioGroup(RadioGroup palmarPallorRadioGroup) {
		this.palmarPallorRadioGroup = palmarPallorRadioGroup;
	}

	/**
	 * Getter Method: getMuacTapeCustomRadioGroup()
	 */	
	private ToggleButtonGroupTableLayout getMuacTapeCustomRadioGroup() {
		return muacTapeCustomRadioGroup;
	}

	/**
	 * Setter Method: setMuacTapeCustomRadioGroup()
	 */	
	private void setMuacTapeCustomRadioGroup(ToggleButtonGroupTableLayout muacTapeCustomRadioGroup) {
		this.muacTapeCustomRadioGroup = muacTapeCustomRadioGroup;
	}

	/**
	 * Getter Method: getSwellingOfBothFeetRadioGroup()
	 */	
	private RadioGroup getSwellingOfBothFeetRadioGroup() {
		return swellingOfBothFeetRadioGroup;
	}

	/**
	 * Setter Method: setSwellingOfBothFeetRadioGroup()
	 */	
	private void setSwellingOfBothFeetRadioGroup(RadioGroup swellingOfBothFeetRadioGroup) {
		this.swellingOfBothFeetRadioGroup = swellingOfBothFeetRadioGroup;
	}

	/**
	 * Getter Method: getBreathsPerMinuteEditText()
	 */	
	private EditText getBreathsPerMinuteEditText() {
		return breathsPerMinuteEditText;
	}

	/**
	 * Setter Method: setBreathsPerMinuteEditText()
	 */	
	private void setBreathsPerMinuteEditText(EditText breathsPerMinuteEditText) {
		this.breathsPerMinuteEditText = breathsPerMinuteEditText;
	}

	private Button getBreatheButton() {
		return breatheButton;
	}

	private void setBreatheButton(Button breatheButton) {
		this.breatheButton = breatheButton;
	}

	private BreathCountTextWatcher getBreathCountTextWatcher() {
		return breathCountTextWatcher;
	}

	private void setBreathCountTextWatcher(BreathCountTextWatcher breathCountTextWatcher) {
		this.breathCountTextWatcher = breathCountTextWatcher;
	}
}