package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.InitialAskCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.model.DynamicView;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.FragmentValidator;
import ie.ucc.bis.supportinglife.assessment.model.listener.AssessmentWizardTextWatcher;
import ie.ucc.bis.supportinglife.assessment.model.listener.DynamicViewListenerUtilities;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupCoordinatorListener;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupListener;
import ie.ucc.bis.supportinglife.ui.utilities.ViewGroupUtilities;
import ie.ucc.bis.supportinglife.validation.Form;
import ie.ucc.bis.supportinglife.validation.NotEmptyValidation;
import ie.ucc.bis.supportinglife.validation.RadioGroupFieldValidations;
import ie.ucc.bis.supportinglife.validation.RadioGroupValidation;
import ie.ucc.bis.supportinglife.validation.TextFieldValidations;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Responsible for UI fragment to display CCM 
 * Initial 'Ask' assessment form
 * 
 * @author timothyosullivan
 * 
 */
public class InitialAskCcmFragment extends Fragment implements FragmentLifecycle, FragmentValidator {

	private InitialAskCcmPage initialAskCcmPage;    
	private PageFragmentCallbacks pageFragmentCallbacks;
	private String pageKey;
	private EditText problemsEditText;
	private RadioGroup coughRadioGroup;
	private RadioGroup diarrhoeaRadioGroup;
	private RadioGroup bloodInStoolRadioGroup;
	private RadioGroup feverRadioGroup;
	private RadioGroup convulsionsRadioGroup;
	private RadioGroup drinkFeedDifficultyRadioGroup;
	private RadioGroup unableDrinkFeedRadioGroup;
	private EditText coughDurationEditText;
	private EditText diarrhoeaDurationEditText;
	private EditText feverDurationEditText;
	private ViewGroup animatedTopLevelView;
	private View coughView;
	private View diarrhoeaView;
	private View feverView;
	private View drinkFeedView;
	private DynamicView coughDurationDynamicView;
	private DynamicView diarrhoeaDurationDynamicView;
	private DynamicView feverDurationDynamicView;
	private DynamicView drinkFeedDynamicView;
	private Boolean animatedCoughDurationViewInVisibleState;
	private Boolean animatedDiarrhoeaDurationViewInVisibleState;
	private Boolean animatedFeverDurationViewInVisibleState;
	private Boolean animatedUnableDrinkFeedViewInVisibleState;
	
	// Form used for validation
    private Form form;
	
	public static InitialAskCcmFragment create(String pageKey) {
		Bundle args = new Bundle();
		args.putString(ARG_PAGE_KEY, pageKey);

		InitialAskCcmFragment fragment = new InitialAskCcmFragment();
		fragment.setArguments(args);
		fragment.setAnimatedCoughDurationViewInVisibleState(false);
		fragment.setAnimatedDiarrhoeaDurationViewInVisibleState(false);
		fragment.setAnimatedFeverDurationViewInVisibleState(false);
		fragment.setAnimatedUnableDrinkFeedViewInVisibleState(false);
		return fragment;
	}

	/**
	 * Constructor
	 *
	 */
	public InitialAskCcmFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		setPageKey(args.getString(ARG_PAGE_KEY));
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// take record of visibility of 'Cough Duration' view
		if (getAnimatedTopLevelView().indexOfChild(getCoughDurationDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedCoughDurationViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedCoughDurationViewInVisibleState", false);
		}

		// take record of visibility of 'Diarrhoea Duration' view
		if (getAnimatedTopLevelView().indexOfChild(getDiarrhoeaDurationDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedDiarrhoeaDurationViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedDiarrhoeaDurationViewInVisibleState", false);
		}

		// take record of visibility of 'Fever Duration' view
		if (getAnimatedTopLevelView().indexOfChild(getFeverDurationDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedFeverDurationViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedFeverDurationViewInVisibleState", false);
		}

		// take record of visibility of 'Not Able to Drink or Feed' view
		if (getAnimatedTopLevelView().indexOfChild(getDrinkFeedDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedUnableDrinkFeedViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedUnableDrinkFeedViewInVisibleState", false);
		}
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			setAnimatedCoughDurationViewInVisibleState(savedInstanceState.getBoolean("animatedCoughDurationViewInVisibleState"));
			setAnimatedDiarrhoeaDurationViewInVisibleState(savedInstanceState.getBoolean("animatedDiarrhoeaDurationViewInVisibleState"));
			setAnimatedFeverDurationViewInVisibleState(savedInstanceState.getBoolean("animatedFeverDurationViewInVisibleState"));
			setAnimatedUnableDrinkFeedViewInVisibleState(savedInstanceState.getBoolean("animatedUnableDrinkFeedViewInVisibleState"));
		}

		// restore visibility of 'Cough Duration' view
		if (!isAnimatedCoughDurationViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getCoughDurationDynamicView()));
		}

		// restore visibility of 'Diarrhoea Duration' view 
		if (!isAnimatedDiarrhoeaDurationViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getDiarrhoeaDurationDynamicView()));
		}

		// restore visibility of 'Fever Duration' view 
		if (!isAnimatedFeverDurationViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getFeverDurationDynamicView()));
		}

		// restore visibility of 'Not Able to Drink or Feed' view 
		if (!isAnimatedUnableDrinkFeedViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getDrinkFeedDynamicView()));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setInitialAskCcmPage((InitialAskCcmPage) getPageFragmentCallbacks().getPage(getPageKey()));
		
		View rootView = inflater.inflate(R.layout.fragment_ccm_page_initial_ask_assessment, container, false);
		((TextView) rootView.findViewById(android.R.id.title)).setText(getInitialAskCcmPage().getTitle());

		// get a hold on the top level animated view
		setAnimatedTopLevelView(((ViewGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_animated_top_level_view)));

		// child's problems
		setProblemsEditText(((EditText) rootView.findViewById(R.id.ccm_ask_initial_assessment_problems)));
		getProblemsEditText().setText(getInitialAskCcmPage().getPageData().getString(InitialAskCcmPage.PROBLEMS_DATA_KEY));

		// cough
		configureCoughDurationAnimatedView(rootView);

		// diarrhoea
		configureDiarrhoeaDurationAnimateView(rootView);

		// blood in stool
		setBloodInStoolRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_blood_in_stool));
		getBloodInStoolRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.BLOOD_IN_STOOL_DATA_KEY));

		// fever
		configureFeverDurationAnimateView(rootView);

		// convulsions
		setConvulsionsRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_convulsions));
		getConvulsionsRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.CONVULSIONS_DATA_KEY));

		// difficulty drinking or feeding
		configureDrinkFeedAnimateView(rootView);
		
		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		((SupportingLifeBaseActivity) getActivity()).addSoftKeyboardHandling(rootView);
		
		// validation
		configureValidation();

		return rootView;
	}

	/**
	 * configureCoughDurationAnimatedView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 'cough duration'
	 * relating to the 'cough' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureCoughDurationAnimatedView(View rootView) {
		// cough view
		setCoughView((View) rootView.findViewById(R.id.ccm_ask_initial_assessment_view_cough));

		// cough radio group
		setCoughRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_cough));
		getCoughRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.COUGH_DATA_KEY));

		// cough duration
		setCoughDurationEditText((EditText) rootView.findViewById(R.id.ccm_ask_initial_assessment_cough_duration));
		getCoughDurationEditText().setText(getInitialAskCcmPage().getPageData().getString(InitialAskCcmPage.COUGH_DURATION_DATA_KEY));

		// 'cough duration' is a dynamic view within the UI
		setCoughDurationDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_initial_assessment_view_cough_duration),
				rootView.findViewById(R.id.ccm_ask_initial_assessment_cough_duration)));
	}

	/**
	 * configureDiarrhoeaDurationAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 'diarrhoea duration'
	 * relating to the 'diarrhoea' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureDiarrhoeaDurationAnimateView(View rootView) {
		// diarrhoea view
		setDiarrhoeaView((View) rootView.findViewById(R.id.ccm_ask_initial_assessment_view_diarrhoea));

		// diarrhoea radio group
		setDiarrhoeaRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_diarrhoea));
		getDiarrhoeaRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.DIARRHOEA_DATA_KEY));

		// diarrhoea duration
		setDiarrhoeaDurationEditText((EditText) rootView.findViewById(R.id.ccm_ask_initial_assessment_diarrhoea_duration));
		getDiarrhoeaDurationEditText().setText(getInitialAskCcmPage().getPageData().getString(InitialAskCcmPage.DIARRHOEA_DURATION_DATA_KEY));

		// 'diarrhoea duration' is a dynamic view within the UI
		setDiarrhoeaDurationDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_initial_assessment_view_diarrhoea_duration),
				rootView.findViewById(R.id.ccm_ask_initial_assessment_diarrhoea_duration)));
	}

	/**
	 * configureFeverDurationAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 'fever duration'
	 * relating to the 'fever' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureFeverDurationAnimateView(View rootView) {
		// fever view
		setFeverView((View) rootView.findViewById(R.id.ccm_ask_initial_assessment_view_fever));

		// fever radio group
		setFeverRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_fever));
		getFeverRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.FEVER_DATA_KEY));

		// fever duration
		setFeverDurationEditText((EditText) rootView.findViewById(R.id.ccm_ask_initial_assessment_fever_duration));
		getFeverDurationEditText().setText(getInitialAskCcmPage().getPageData().getString(InitialAskCcmPage.FEVER_DURATION_DATA_KEY));

		// 'fever duration' is a dynamic view within the UI
		setFeverDurationDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_initial_assessment_view_fever_duration),
				rootView.findViewById(R.id.ccm_ask_initial_assessment_fever_duration)));
	}

	/**
	 * configureDrinkFeedAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 
	 * 'not able to drink or feed anything' radio group
	 * relating to the 'difficulty drinking or feeding' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureDrinkFeedAnimateView(View rootView) {
		// 'difficulty drinking or feeding' view
		setDrinkFeedView((View) rootView.findViewById(R.id.ccm_ask_initial_assessment_view_drink_or_feed));

		// 'difficulty drinking or feeding' radio group
		setDrinkFeedDifficultyRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_drink_or_feed_difficulty));
		getDrinkFeedDifficultyRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.DRINK_OR_FEED_DIFFICULTY_DATA_KEY));

		// 'not able to drink or feed anything' radio group
		setUnableDrinkFeedRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_unable_to_drink_or_feed));
		getUnableDrinkFeedRadioGroup().check(getInitialAskCcmPage()
				.getPageData().getInt(InitialAskCcmPage.UNABLE_TO_DRINK_OR_FEED_DATA_KEY));

		// 'not able to drink or feed anything' radio group is a dynamic view within the UI
		setDrinkFeedDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_initial_assessment_view_unable_to_drink_or_feed),
				rootView.findViewById(R.id.ccm_ask_initial_assessment_radio_unable_to_drink_or_feed)));
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

		// child's problems
		getProblemsEditText().addTextChangedListener(
				new AssessmentWizardTextWatcher(getInitialAskCcmPage(), 
						InitialAskCcmPage.PROBLEMS_DATA_KEY, getForm(), this));
		
		// add dynamic view listener to cough radio group
		TextFieldValidations coughDurationValidation = TextFieldValidations.using(getCoughDurationEditText(), getResources().getString(R.string.ccm_ask_initial_assessment_cough_duration_label)).validate(NotEmptyValidation.build(this.getActivity()));
		DynamicViewListenerUtilities.addGenericDynamicViewListeners(getCoughView(), getCoughDurationDynamicView(),
				getAnimatedTopLevelView(),
				getCoughRadioGroup(), getCoughDurationEditText(),
				InitialAskCcmPage.COUGH_DATA_KEY, InitialAskCcmPage.COUGH_DURATION_DATA_KEY, 
				getResources(), getInitialAskCcmPage(), getForm(), this, coughDurationValidation);

		// add dynamic view listener to diarrhoea radio group
		DynamicViewListenerUtilities.addGenericDynamicViewListeners(getDiarrhoeaView(), getDiarrhoeaDurationDynamicView(),
				getAnimatedTopLevelView(),
				getDiarrhoeaRadioGroup(), getDiarrhoeaDurationEditText(),
				InitialAskCcmPage.DIARRHOEA_DATA_KEY, InitialAskCcmPage.DIARRHOEA_DURATION_DATA_KEY,
				getResources(), getInitialAskCcmPage());

		// add listener to blood in stool radio group
		getBloodInStoolRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getInitialAskCcmPage(),
						InitialAskCcmPage.BLOOD_IN_STOOL_DATA_KEY, getForm(), this));		 

		// add dynamic view listener to fever radio group
		DynamicViewListenerUtilities.addGenericDynamicViewListeners(getFeverView(), getFeverDurationDynamicView(),
				getAnimatedTopLevelView(),
				getFeverRadioGroup(), getFeverDurationEditText(),
				InitialAskCcmPage.FEVER_DATA_KEY, InitialAskCcmPage.FEVER_DURATION_DATA_KEY,
				getResources(), getInitialAskCcmPage());

		// add listener to convulsions radio group
		getConvulsionsRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getInitialAskCcmPage(),
						InitialAskCcmPage.CONVULSIONS_DATA_KEY));

		// add dynamic view listener to 'difficulty drinking or feeding' radio group
		getDrinkFeedDifficultyRadioGroup().setOnCheckedChangeListener(
				new RadioGroupCoordinatorListener(getInitialAskCcmPage(),
						InitialAskCcmPage.DRINK_OR_FEED_DIFFICULTY_DATA_KEY, 
						Arrays.asList(getDrinkFeedDynamicView()),
						getAnimatedTopLevelView(),
						getDrinkFeedView()));

		// add listener to 'not able to drink or feed anything' radio group
		getUnableDrinkFeedRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getInitialAskCcmPage(),
						InitialAskCcmPage.UNABLE_TO_DRINK_OR_FEED_DATA_KEY));
		
		// validation
    	configureValidation();
	}
	
    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (assessmentPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(assessmentPage, InitialAskCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(assessmentPage,
					InitialAskCcmPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) assessmentPage.getPageData().getSerializable(InitialAskCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) assessmentPage.getPageData().getSerializable(InitialAskCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (assessmentPage != null) {
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(assessmentPage, InitialAskCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}
    }
    
	/**
	 * Responsible for configuring validation on the CCM page
	 */
	private void configureValidation() {
        setForm(new Form(this.getActivity()));

        // validation rules
        getForm().addTextFieldValidations(TextFieldValidations.using(getProblemsEditText(), getResources().getString(R.string.ccm_ask_initial_assessment_problems_label)).validate(NotEmptyValidation.build(this.getActivity())));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getCoughView(), getResources().getString(R.string.ccm_ask_initial_assessment_review_cough)).validate(RadioGroupValidation.build(this.getActivity())));

        // run validation check
        ((AssessmentActivity) getActivity()).getAssessmentViewPager().setPagingEnabled(performValidation());
	}
	
	@Override
	public boolean performValidation() {
		if (getForm() != null) {
			return getForm().performValidation();
		}
		else {
			return false;
		}
	}
	
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { 
        	((AssessmentActivity) getActivity()).getAssessmentViewPager().setPagingEnabled(performValidation());
        }
    }
    
	public InitialAskCcmPage getInitialAskCcmPage() {
		return initialAskCcmPage;
	}

	public void setInitialAskCcmPage(InitialAskCcmPage initialAskCcmPage) {
		this.initialAskCcmPage = initialAskCcmPage;
	}

	public PageFragmentCallbacks getPageFragmentCallbacks() {
		return pageFragmentCallbacks;
	}

	public void setPageFragmentCallbacks(PageFragmentCallbacks pageFragmentCallbacks) {
		this.pageFragmentCallbacks = pageFragmentCallbacks;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public EditText getProblemsEditText() {
		return problemsEditText;
	}

	public void setProblemsEditText(EditText problemsEditText) {
		this.problemsEditText = problemsEditText;
	}

	public RadioGroup getCoughRadioGroup() {
		return coughRadioGroup;
	}

	public void setCoughRadioGroup(RadioGroup coughRadioGroup) {
		this.coughRadioGroup = coughRadioGroup;
	}

	public RadioGroup getDiarrhoeaRadioGroup() {
		return diarrhoeaRadioGroup;
	}

	public void setDiarrhoeaRadioGroup(RadioGroup diarrhoeaRadioGroup) {
		this.diarrhoeaRadioGroup = diarrhoeaRadioGroup;
	}

	public RadioGroup getBloodInStoolRadioGroup() {
		return bloodInStoolRadioGroup;
	}

	public void setBloodInStoolRadioGroup(RadioGroup bloodInStoolRadioGroup) {
		this.bloodInStoolRadioGroup = bloodInStoolRadioGroup;
	}

	public RadioGroup getFeverRadioGroup() {
		return feverRadioGroup;
	}

	public void setFeverRadioGroup(RadioGroup feverRadioGroup) {
		this.feverRadioGroup = feverRadioGroup;
	}

	public RadioGroup getConvulsionsRadioGroup() {
		return convulsionsRadioGroup;
	}

	public void setConvulsionsRadioGroup(RadioGroup convulsionsRadioGroup) {
		this.convulsionsRadioGroup = convulsionsRadioGroup;
	}

	public RadioGroup getDrinkFeedDifficultyRadioGroup() {
		return drinkFeedDifficultyRadioGroup;
	}

	public void setDrinkFeedDifficultyRadioGroup(RadioGroup drinkFeedDifficultyRadioGroup) {
		this.drinkFeedDifficultyRadioGroup = drinkFeedDifficultyRadioGroup;
	}

	public RadioGroup getUnableDrinkFeedRadioGroup() {
		return unableDrinkFeedRadioGroup;
	}

	public void setUnableDrinkFeedRadioGroup(RadioGroup unableDrinkFeedRadioGroup) {
		this.unableDrinkFeedRadioGroup = unableDrinkFeedRadioGroup;
	}

	public EditText getCoughDurationEditText() {
		return coughDurationEditText;
	}

	public void setCoughDurationEditText(EditText coughDurationEditText) {
		this.coughDurationEditText = coughDurationEditText;
	}

	public EditText getDiarrhoeaDurationEditText() {
		return diarrhoeaDurationEditText;
	}

	public void setDiarrhoeaDurationEditText(EditText diarrhoeaDurationEditText) {
		this.diarrhoeaDurationEditText = diarrhoeaDurationEditText;
	}

	public EditText getFeverDurationEditText() {
		return feverDurationEditText;
	}

	public void setFeverDurationEditText(EditText feverDurationEditText) {
		this.feverDurationEditText = feverDurationEditText;
	}

	public ViewGroup getAnimatedTopLevelView() {
		return animatedTopLevelView;
	}

	public void setAnimatedTopLevelView(ViewGroup animatedTopLevelView) {
		this.animatedTopLevelView = animatedTopLevelView;
	}

	public View getCoughView() {
		return coughView;
	}

	public void setCoughView(View coughView) {
		this.coughView = coughView;
	}

	public View getDiarrhoeaView() {
		return diarrhoeaView;
	}

	public void setDiarrhoeaView(View diarrhoeaView) {
		this.diarrhoeaView = diarrhoeaView;
	}

	public View getFeverView() {
		return feverView;
	}

	public void setFeverView(View feverView) {
		this.feverView = feverView;
	}

	public View getDrinkFeedView() {
		return drinkFeedView;
	}

	public void setDrinkFeedView(View drinkFeedView) {
		this.drinkFeedView = drinkFeedView;
	}

	public DynamicView getCoughDurationDynamicView() {
		return coughDurationDynamicView;
	}

	public void setCoughDurationDynamicView(DynamicView coughDurationDynamicView) {
		this.coughDurationDynamicView = coughDurationDynamicView;
	}

	public DynamicView getDiarrhoeaDurationDynamicView() {
		return diarrhoeaDurationDynamicView;
	}

	public void setDiarrhoeaDurationDynamicView(DynamicView diarrhoeaDurationDynamicView) {
		this.diarrhoeaDurationDynamicView = diarrhoeaDurationDynamicView;
	}

	public DynamicView getFeverDurationDynamicView() {
		return feverDurationDynamicView;
	}

	public void setFeverDurationDynamicView(DynamicView feverDurationDynamicView) {
		this.feverDurationDynamicView = feverDurationDynamicView;
	}

	public DynamicView getDrinkFeedDynamicView() {
		return drinkFeedDynamicView;
	}

	public void setDrinkFeedDynamicView(DynamicView drinkFeedDynamicView) {
		this.drinkFeedDynamicView = drinkFeedDynamicView;
	}

	public Boolean isAnimatedCoughDurationViewInVisibleState() {
		return animatedCoughDurationViewInVisibleState;
	}

	public void setAnimatedCoughDurationViewInVisibleState(Boolean animatedCoughDurationViewInVisibleState) {
		this.animatedCoughDurationViewInVisibleState = animatedCoughDurationViewInVisibleState;
	}

	public Boolean isAnimatedDiarrhoeaDurationViewInVisibleState() {
		return animatedDiarrhoeaDurationViewInVisibleState;
	}

	public void setAnimatedDiarrhoeaDurationViewInVisibleState(Boolean animatedDiarrhoeaDurationViewInVisibleState) {
		this.animatedDiarrhoeaDurationViewInVisibleState = animatedDiarrhoeaDurationViewInVisibleState;
	}

	public Boolean isAnimatedFeverDurationViewInVisibleState() {
		return animatedFeverDurationViewInVisibleState;
	}

	public void setAnimatedFeverDurationViewInVisibleState(Boolean animatedFeverDurationViewInVisibleState) {
		this.animatedFeverDurationViewInVisibleState = animatedFeverDurationViewInVisibleState;
	}

	public Boolean isAnimatedUnableDrinkFeedViewInVisibleState() {
		return animatedUnableDrinkFeedViewInVisibleState;
	}

	public void setAnimatedUnableDrinkFeedViewInVisibleState(Boolean animatedUnableDrinkFeedViewInVisibleState) {
		this.animatedUnableDrinkFeedViewInVisibleState = animatedUnableDrinkFeedViewInVisibleState;
	}
	
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}