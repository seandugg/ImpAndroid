package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.LookCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentViewPager;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.FragmentValidator;
import ie.ucc.bis.supportinglife.assessment.model.listener.BreathCountTextWatcher;
import ie.ucc.bis.supportinglife.assessment.model.listener.BreathCounterDialogListener;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupListener;
import ie.ucc.bis.supportinglife.ui.custom.ToggleButtonGroupTableLayout;
import ie.ucc.bis.supportinglife.validation.ButtonGroupTableValidations;
import ie.ucc.bis.supportinglife.validation.ButtonGroupValidation;
import ie.ucc.bis.supportinglife.validation.Form;
import ie.ucc.bis.supportinglife.validation.NotEmptyValidation;
import ie.ucc.bis.supportinglife.validation.NumberRangeValidation;
import ie.ucc.bis.supportinglife.validation.RadioGroupFieldValidations;
import ie.ucc.bis.supportinglife.validation.RadioGroupValidation;
import ie.ucc.bis.supportinglife.validation.TextFieldValidations;
import ie.ucc.bis.supportinglife.validation.Validation;
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
public class LookCcmFragment extends Fragment implements FragmentLifecycle, FragmentValidator {
    
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
	
	// Form used for validation
    private Form form;
	
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
		
		// validation
		configureValidation(rootView);

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
				new RadioGroupListener(getLookCcmPage(), LookCcmPage.CHEST_INDRAWING_DATA_KEY, getForm(), this));

		// add listener to 'breaths per minute' textfield
		setBreathCountTextWatcher(new BreathCountTextWatcher(getLookCcmPage(), 
						LookCcmPage.BREATHS_PER_MINUTE_DATA_KEY, LookCcmPage.BREATHS_COUNTER_USED_DATA_KEY,
						LookCcmPage.FULL_BREATH_COUNT_TIME_ASSESSMENT_DATA_KEY, getForm(), this));
		getBreathsPerMinuteEditText().addTextChangedListener(getBreathCountTextWatcher());	

		// add listener to 'very sleepy or unconscious' radio group
		getVerySleepyOrUnconsciousRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(), LookCcmPage.VERY_SLEEPY_OR_UNCONSCIOUS_DATA_KEY, getForm(), this));
		
		// add listener to palmar pallor radio group
		getPalmarPallorRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(), LookCcmPage.PALMAR_PALLOR_DATA_KEY, getForm(), this));
		
        // add listener to custom muac tape radio group
		getMuacTapeCustomRadioGroup().setPage(getLookCcmPage());
		getMuacTapeCustomRadioGroup().setDataKey(LookCcmPage.MUAC_TAPE_COLOUR_DATA_KEY);
        getMuacTapeCustomRadioGroup().setForm(getForm());
        getMuacTapeCustomRadioGroup().setFragment(this);
				
		// add listener to 'swelling of both feet' radio group
		getSwellingOfBothFeetRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getLookCcmPage(), LookCcmPage.SWELLING_OF_BOTH_FEET_DATA_KEY, getForm(), this));
		
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
								getBreathsPerMinuteEditText().setText("");
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
	 * Responsible for configuring validation on the CCM page
	 * @param rootView 
	 */
	private void configureValidation(View rootView) {
        setForm(new Form(this.getActivity()));

        // validation rules
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getChestIndrawingRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_look_assessment_radio_chest_indrawing_label))
        		.validate(RadioGroupValidation.build(this.getActivity()))); 
        getForm().addTextFieldValidations(TextFieldValidations.using(getBreathsPerMinuteEditText(), 
        		getResources().getString(R.string.ccm_look_assessment_radio_breaths_per_minute_label))
        		.validate(NotEmptyValidation.build(this.getActivity()))
        		.validate(NumberRangeValidation.build(this.getActivity(), Validation.ONE_DAY, Validation.ONE_HUNDRED_DAYS)));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getVerySleepyOrUnconsciousRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_look_assessment_radio_very_sleepy_or_unconscious_label))
        		.validate(RadioGroupValidation.build(this.getActivity()))); 
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getPalmarPallorRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_look_assessment_radio_palmar_pallor_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));
     
        getForm().addButtonGroupTableValidations(ButtonGroupTableValidations.using(getMuacTapeCustomRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_look_assessment_radio_muac_tape_colour_label))
        		.validate(ButtonGroupValidation.build(this.getActivity())));
        
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getSwellingOfBothFeetRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_look_assessment_radio_swelling_of_both_feet_label))
        		.validate(RadioGroupValidation.build(this.getActivity()))); 
                
        // run validation check
		runPageValidationCheck();
	}
	
	private void runPageValidationCheck() {
		AssessmentViewPager pager = ((AssessmentActivity) getActivity()).getAssessmentViewPager();
		AbstractAssessmentPage page = ((AssessmentActivity) getActivity()).getAssessmentModel().findAssessmentPageByKey(getPageKey());
		int pagePosition = ((AssessmentActivity) getActivity()).getAssessmentModel().getAssessmentPages().indexOf(page);
		pager.configurePagingEnabledElement(pagePosition, performValidation());
	}
	
	@Override
	public boolean performValidation() {
		if (getForm() != null) {
			return getForm().performValidation();
		}
		else {
			return true;
		}
	}
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { 
    		runPageValidationCheck();
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

	private LookCcmPage getLookCcmPage() {
		return lookCcmPage;
	}

	private void setLookCcmPage(LookCcmPage lookCcmPage) {
		this.lookCcmPage = lookCcmPage;
	}

	private PageFragmentCallbacks getPageFragmentCallbacks() {
		return pageFragmentCallbacks;
	}

	private void setPageFragmentCallbacks(PageFragmentCallbacks pageFragmentCallbacks) {
		this.pageFragmentCallbacks = pageFragmentCallbacks;
	}

	private String getPageKey() {
		return pageKey;
	}

	private void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	private RadioGroup getChestIndrawingRadioGroup() {
		return chestIndrawingRadioGroup;
	}

	private void setChestIndrawingRadioGroup(RadioGroup chestIndrawingRadioGroup) {
		this.chestIndrawingRadioGroup = chestIndrawingRadioGroup;
	}

	private RadioGroup getVerySleepyOrUnconsciousRadioGroup() {
		return verySleepyOrUnconsciousRadioGroup;
	}

	private void setVerySleepyOrUnconsciousRadioGroup(RadioGroup verySleepyOrUnconsciousRadioGroup) {
		this.verySleepyOrUnconsciousRadioGroup = verySleepyOrUnconsciousRadioGroup;
	}

	private RadioGroup getPalmarPallorRadioGroup() {
		return palmarPallorRadioGroup;
	}

	private void setPalmarPallorRadioGroup(RadioGroup palmarPallorRadioGroup) {
		this.palmarPallorRadioGroup = palmarPallorRadioGroup;
	}

	private ToggleButtonGroupTableLayout getMuacTapeCustomRadioGroup() {
		return muacTapeCustomRadioGroup;
	}

	private void setMuacTapeCustomRadioGroup(ToggleButtonGroupTableLayout muacTapeCustomRadioGroup) {
		this.muacTapeCustomRadioGroup = muacTapeCustomRadioGroup;
	}

	private RadioGroup getSwellingOfBothFeetRadioGroup() {
		return swellingOfBothFeetRadioGroup;
	}

	private void setSwellingOfBothFeetRadioGroup(RadioGroup swellingOfBothFeetRadioGroup) {
		this.swellingOfBothFeetRadioGroup = swellingOfBothFeetRadioGroup;
	}

	private EditText getBreathsPerMinuteEditText() {
		return breathsPerMinuteEditText;
	}

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
	
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}