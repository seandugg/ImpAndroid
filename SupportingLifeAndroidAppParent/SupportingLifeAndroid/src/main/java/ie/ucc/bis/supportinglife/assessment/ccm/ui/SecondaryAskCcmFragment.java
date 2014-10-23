package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.SecondaryAskCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.model.DynamicView;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.FragmentValidator;
import ie.ucc.bis.supportinglife.assessment.model.listener.DynamicViewListenerUtilities;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupCoordinatorListener;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupListener;
import ie.ucc.bis.supportinglife.ui.utilities.ViewGroupUtilities;
import ie.ucc.bis.supportinglife.validation.Form;
import ie.ucc.bis.supportinglife.validation.NotEmptyValidation;
import ie.ucc.bis.supportinglife.validation.NumberRangeValidation;
import ie.ucc.bis.supportinglife.validation.RadioGroupFieldValidations;
import ie.ucc.bis.supportinglife.validation.RadioGroupValidation;
import ie.ucc.bis.supportinglife.validation.TextFieldValidations;
import ie.ucc.bis.supportinglife.validation.Validation;

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
 * Secondary 'Ask' assessment form
 * 
 * @author timothyosullivan
 * 
 */
public class SecondaryAskCcmFragment extends Fragment implements FragmentLifecycle, FragmentValidator {

	private SecondaryAskCcmPage secondaryAskCcmPage;    
	private PageFragmentCallbacks pageFragmentCallbacks;
	private String pageKey;
	private RadioGroup vomitingRadioGroup;
	private RadioGroup vomitsEverythingRadioGroup;
	private RadioGroup redEyesRadioGroup;
	private RadioGroup seeingDifficultyRadioGroup;
	private RadioGroup cannotTreatProblemsRadioGroup;
	private EditText redEyesDurationEditText;
	private EditText seeingDifficultyDurationEditText;
	private EditText cannotTreatProblemsDetailsEditText;
	private ViewGroup animatedTopLevelView;
	private View vomitingView;
	private View redEyesView;
	private View seeingDifficultyView;
	private View cannotTreatProblemsView;
	private DynamicView vomitingDynamicView;
	private DynamicView redEyesDurationDynamicView;
	private DynamicView seeingDifficultyDurationDynamicView;
	private DynamicView cannotTreatProblemsDetailsDynamicView;
	private Boolean animatedVomitsEverythingViewInVisibleState;
	private Boolean animatedRedEyesDurationViewInVisibleState;
	private Boolean animatedSeeingDifficultyDurationViewInVisibleState;
	private Boolean animatedCannotTreatProblemsDetailsViewInVisibleState;

	// Form used for validation
    private Form form;
	
	public static SecondaryAskCcmFragment create(String pageKey) {
		Bundle args = new Bundle();
		args.putString(ARG_PAGE_KEY, pageKey);

		SecondaryAskCcmFragment fragment = new SecondaryAskCcmFragment();
		fragment.setArguments(args);
		fragment.setAnimatedVomitsEverythingViewInVisibleState(false);
		fragment.setAnimatedRedEyesDurationViewInVisibleState(false);
		fragment.setAnimatedSeeingDifficultyDurationViewInVisibleState(false);
		fragment.setAnimatedCannotTreatProblemsDetailsViewInVisibleState(false);
		return fragment;
	}

	/**
	 * Constructor
	 *
	 */
	public SecondaryAskCcmFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		setPageKey(args.getString(ARG_PAGE_KEY));
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// take record of visibility of 'Vomits Everything' view
		if (getAnimatedTopLevelView().indexOfChild(getVomitingDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedVomitsEverythingViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedVomitsEverythingViewInVisibleState", false);
		}

		// take record of visibility of 'Red Eyes Duration' view
		if (getAnimatedTopLevelView().indexOfChild(getRedEyesDurationDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedRedEyesDurationViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedRedEyesDurationViewInVisibleState", false);
		}

		// take record of visibility of 'Seeing Difficulty Duration' view
		if (getAnimatedTopLevelView().indexOfChild(getSeeingDifficultyDurationDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedSeeingDifficultyDurationViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedSeeingDifficultyDurationViewInVisibleState", false);
		}
		
		// take record of visibility of 'Seeing Difficulty Duration' view
		if (getAnimatedTopLevelView().indexOfChild(getSeeingDifficultyDurationDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedSeeingDifficultyDurationViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedSeeingDifficultyDurationViewInVisibleState", false);
		}
		
		// take record of visibility of 'Cannot Treat Problems Details' view
		if (getAnimatedTopLevelView().indexOfChild(getCannotTreatProblemsDetailsDynamicView().getWrappedView()) != -1) {
			savedInstanceState.putBoolean("animatedCannotTreatProblemsDetailsViewInVisibleState", true);
		}
		else {
			savedInstanceState.putBoolean("animatedCannotTreatProblemsDetailsViewInVisibleState", false);
		}
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			setAnimatedVomitsEverythingViewInVisibleState(savedInstanceState.getBoolean("animatedVomitsEverythingViewInVisibleState"));
			setAnimatedRedEyesDurationViewInVisibleState(savedInstanceState.getBoolean("animatedRedEyesDurationViewInVisibleState"));
			setAnimatedSeeingDifficultyDurationViewInVisibleState(savedInstanceState.getBoolean("animatedSeeingDifficultyDurationViewInVisibleState"));
			setAnimatedCannotTreatProblemsDetailsViewInVisibleState(savedInstanceState.getBoolean("animatedCannotTreatProblemsDetailsViewInVisibleState"));
		}

		// restore visibility of 'Vomits Everything' view
		if (!isAnimatedVomitsEverythingViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getVomitingDynamicView()));
		}

		// restore visibility of 'Red Eyes Duration' view 
		if (!isAnimatedRedEyesDurationViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getRedEyesDurationDynamicView()));
		}

		// restore visibility of 'Seeing Difficulty Duration' view 
		if (!isAnimatedSeeingDifficultyDurationViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getSeeingDifficultyDurationDynamicView()));
		}
		
		// restore visibility of 'Cannot Treat Problems Details' view
		if (!isAnimatedCannotTreatProblemsDetailsViewInVisibleState()) {
			ViewGroupUtilities.removeDynamicViews(getAnimatedTopLevelView(), Arrays.asList(getCannotTreatProblemsDetailsDynamicView()));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setSecondaryAskCcmPage((SecondaryAskCcmPage) getPageFragmentCallbacks().getPage(getPageKey()));
		
		View rootView = inflater.inflate(R.layout.fragment_ccm_page_secondary_ask_assessment, container, false);
		((TextView) rootView.findViewById(android.R.id.title)).setText(getSecondaryAskCcmPage().getTitle());

		// get a hold on the top level animated view
		setAnimatedTopLevelView(((ViewGroup) rootView.findViewById(R.id.ccm_ask_secondary_assessment_animated_top_level_view)));

		// vomiting
		configureVomitingAnimateView(rootView);

		// red eyes
		configureRedEyesDurationAnimateView(rootView);

		// seeing difficulty
		configureSeeingDifficultyDurationAnimateView(rootView);
		
		// any other problems I cannot treat
		configureCannotTreatProblemsDetailsAnimateView(rootView);

		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		((SupportingLifeBaseActivity) getActivity()).addSoftKeyboardHandling(rootView);
		
		// validation
		configureValidation(rootView);

		return rootView;
	}

	/**
	 * configureVomitingAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 
	 * 'vomits everything' radio group
	 * relating to the 'vomiting' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureVomitingAnimateView(View rootView) {
		// 'vomiting' view
		setVomitingView((View) rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_vomiting));

		// 'vomiting' radio group
		setVomitingRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_vomiting));
		getVomitingRadioGroup().check(getSecondaryAskCcmPage()
				.getPageData().getInt(SecondaryAskCcmPage.VOMITING_DATA_KEY));

		// 'vomits everything' radio group
		setVomitsEverythingRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_vomits_everything));
		getVomitsEverythingRadioGroup().check(getSecondaryAskCcmPage()
				.getPageData().getInt(SecondaryAskCcmPage.VOMITS_EVERYTHING_DATA_KEY));

		// 'vomits everything' radio group is a dynamic view within the UI
		setVomitingDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_vomits_everything),
				rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_vomits_everything)));
	}

	/**
	 * configureRedEyesDurationAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 'red eyes duration'
	 * relating to the 'red eyes' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureRedEyesDurationAnimateView(View rootView) {
		// red eyes view
		setRedEyesView((View) rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_red_eyes));

		// red eyes radio group
		setRedEyesRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_red_eyes));
		getRedEyesRadioGroup().check(getSecondaryAskCcmPage()
				.getPageData().getInt(SecondaryAskCcmPage.RED_EYES_DATA_KEY));

		// red eyes duration
		setRedEyesDurationEditText((EditText) rootView.findViewById(R.id.ccm_ask_secondary_assessment_red_eyes_duration));
		getRedEyesDurationEditText().setText(getSecondaryAskCcmPage().getPageData().getString(SecondaryAskCcmPage.RED_EYES_DURATION_DATA_KEY));

		// 'red eyes duration' is a dynamic view within the UI
		setRedEyesDurationDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_red_eyes_duration),
				rootView.findViewById(R.id.ccm_ask_secondary_assessment_red_eyes_duration)));
	}

	/**
	 * configureSeeingDifficultyDurationAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 'difficulty seeing duration'
	 * relating to the 'difficulty in seeing' radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureSeeingDifficultyDurationAnimateView(View rootView) {
		// difficulty seeing view
		setSeeingDifficultyView((View) rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_seeing_difficulty));

		// difficulty seeing radio group
		setSeeingDifficultyRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_seeing_difficulty));
		getSeeingDifficultyRadioGroup().check(getSecondaryAskCcmPage()
				.getPageData().getInt(SecondaryAskCcmPage.SEEING_DIFFICULTY_DATA_KEY));

		// difficulty seeing duration
		setSeeingDifficultyDurationEditText((EditText) rootView.findViewById(R.id.ccm_ask_secondary_assessment_seeing_difficulty_duration));
		getSeeingDifficultyDurationEditText().setText(getSecondaryAskCcmPage().getPageData().getString(SecondaryAskCcmPage.SEEING_DIFFICULTY_DURATION_DATA_KEY));

		// 'difficulty seeing duration' is a dynamic view within the UI
		setSeeingDifficultyDurationDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_seeing_difficulty_duration),
				rootView.findViewById(R.id.ccm_ask_secondary_assessment_seeing_difficulty_duration)));
	}

	/**
	 * configureCannotTreatProblemsDetailsAnimateView(View rootView)
	 * 
	 * Method responsible for configuring a the dynamic animation of 'any other problems I cannot treat'
	 * details textfield relating to the 'any other problems I cannot treat'
	 * radio button click event.
	 * 
	 * @param rootView
	 * 
	 */
	private void configureCannotTreatProblemsDetailsAnimateView(View rootView) {
		// 'any other problems I cannot treat' view
		setCannotTreatProblemsView((View) rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_cannot_treat_problems));

		// 'any other problems I cannot treat' radio group
		setCannotTreatProblemsRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_cannot_treat_problems));
		getCannotTreatProblemsRadioGroup().check(getSecondaryAskCcmPage()
				.getPageData().getInt(SecondaryAskCcmPage.CANNOT_TREAT_PROBLEMS_DATA_KEY));

		// 'any other problems I cannot treat' details
		setCannotTreatProblemsDetailsEditText((EditText) rootView.findViewById(R.id.ccm_ask_secondary_assessment_cannot_treat_problems_details));
		getCannotTreatProblemsDetailsEditText().setText(getSecondaryAskCcmPage().getPageData().getString(SecondaryAskCcmPage.CANNOT_TREAT_PROBLEMS_DETAILS_DATA_KEY));

		// 'any other problems I cannot treat' details is a dynamic view within the UI
		setCannotTreatProblemsDetailsDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_ask_secondary_assessment_view_cannot_treat_problems_details),
				rootView.findViewById(R.id.ccm_ask_secondary_assessment_cannot_treat_problems_details)));
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

		// add dynamic view listener to 'vomiting' radio group
		RadioGroupFieldValidations vomitsEverythingValidation = RadioGroupFieldValidations.using(getVomitsEverythingRadioGroup(), 
				(TextView) getView().findViewById(R.id.ccm_ask_secondary_assessment_radio_vomits_everything_label))
				.validate(RadioGroupValidation.build(this.getActivity()));
		getVomitingRadioGroup().setOnCheckedChangeListener(
				new RadioGroupCoordinatorListener(getSecondaryAskCcmPage(),
						SecondaryAskCcmPage.VOMITING_DATA_KEY, 
						Arrays.asList(getVomitingDynamicView()),
						getAnimatedTopLevelView(),
						getVomitingView(), getForm(), this, vomitsEverythingValidation));

		// add listener to 'vomits everything' radio group
		getVomitsEverythingRadioGroup().setOnCheckedChangeListener(
				new RadioGroupListener(getSecondaryAskCcmPage(),
						SecondaryAskCcmPage.VOMITS_EVERYTHING_DATA_KEY, getForm(), this));

		// add dynamic view listener to 'red eyes' radio group
		TextFieldValidations redEyesDurationValidation = TextFieldValidations.using(getRedEyesDurationEditText(), 
				getResources().getString(R.string.ccm_ask_secondary_assessment_red_eyes_duration_label))
				.validate(NotEmptyValidation.build(this.getActivity()))
				.validate(NumberRangeValidation.build(this.getActivity(), Validation.ONE_DAY, Validation.ONE_YEAR_IN_DAYS));
		DynamicViewListenerUtilities.addGenericDynamicViewListeners(getRedEyesView(), getRedEyesDurationDynamicView(),
				getAnimatedTopLevelView(),
				getRedEyesRadioGroup(), getRedEyesDurationEditText(),
				SecondaryAskCcmPage.RED_EYES_DATA_KEY, SecondaryAskCcmPage.RED_EYES_DURATION_DATA_KEY,
				getResources(), getSecondaryAskCcmPage(), getForm(), this, redEyesDurationValidation);

		// add dynamic view listener to 'seeing difficulty' radio group
		TextFieldValidations seeingDifficultyDurationValidation = TextFieldValidations.using(getSeeingDifficultyDurationEditText(), 
				getResources().getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_duration_label))
				.validate(NotEmptyValidation.build(this.getActivity()));
		DynamicViewListenerUtilities.addGenericDynamicViewListeners(getSeeingDifficultyView(), getSeeingDifficultyDurationDynamicView(),
				getAnimatedTopLevelView(),
				getSeeingDifficultyRadioGroup(), getSeeingDifficultyDurationEditText(),
				SecondaryAskCcmPage.SEEING_DIFFICULTY_DATA_KEY, SecondaryAskCcmPage.SEEING_DIFFICULTY_DURATION_DATA_KEY,
				getResources(), getSecondaryAskCcmPage(), getForm(), this, seeingDifficultyDurationValidation);
		
		// add dynamic view listener to 'any other problems I cannot treat' radio group
		TextFieldValidations otherProblemsValidation = TextFieldValidations.using(getCannotTreatProblemsDetailsEditText(), 
				getResources().getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_details_label)).validate(NotEmptyValidation.build(this.getActivity()));
		DynamicViewListenerUtilities.addGenericDynamicViewListeners(getCannotTreatProblemsView(), getCannotTreatProblemsDetailsDynamicView(),
				getAnimatedTopLevelView(),
				getCannotTreatProblemsRadioGroup(), getCannotTreatProblemsDetailsEditText(),
				SecondaryAskCcmPage.CANNOT_TREAT_PROBLEMS_DATA_KEY, SecondaryAskCcmPage.CANNOT_TREAT_PROBLEMS_DETAILS_DATA_KEY,
				getResources(), getSecondaryAskCcmPage(), getForm(), this, otherProblemsValidation);
	}

    @Override
    public void onPauseFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
		
    	if (assessmentPage != null) {
			// stop analytics timer for page
			AnalyticUtilities.configurePageTimer(assessmentPage, SecondaryAskCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY, AnalyticUtilities.STOP_PAGE_TIMER_ACTION);
			// duration analytics timer for page
			AnalyticUtilities.determineTimerDuration(assessmentPage,
													SecondaryAskCcmPage.ANALTYICS_DURATION_PAGE_TIMER_DATA_KEY,
													AnalyticUtilities.DURATION_PAGE_TIMER_ACTION,
													(DataAnalytic) assessmentPage.getPageData().getSerializable(SecondaryAskCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY),
													(DataAnalytic) assessmentPage.getPageData().getSerializable(SecondaryAskCcmPage.ANALTYICS_STOP_PAGE_TIMER_DATA_KEY));
    	}
    }

    @Override
    public void onResumeFragment(AbstractModel assessmentModel) {
    	
    	// need to use bundle to access page data
		Bundle args = getArguments();
		AbstractAssessmentPage assessmentPage = (AbstractAssessmentPage) ((AbstractAssessmentModel) assessmentModel).findAssessmentPageByKey(args.getString(ARG_PAGE_KEY));
    
		if (assessmentPage != null) {
			// start analytics timer for page
    		AnalyticUtilities.configurePageTimer(assessmentPage, SecondaryAskCcmPage.ANALTYICS_START_PAGE_TIMER_DATA_KEY, AnalyticUtilities.START_PAGE_TIMER_ACTION);    		
    	}
    }
	
	/**
	 * Responsible for configuring validation on the CCM page
	 * @param rootView 
	 */
	private void configureValidation(View rootView) {
        setForm(new Form(this.getActivity()));

        // validation rules
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getVomitingRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_vomiting_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getRedEyesRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_red_eyes_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getSeeingDifficultyRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_seeing_difficulty_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getCannotTreatProblemsRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_ask_secondary_assessment_radio_cannot_treat_problems_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));

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
	
	public SecondaryAskCcmPage getSecondaryAskCcmPage() {
		return secondaryAskCcmPage;
	}

	public void setSecondaryAskCcmPage(SecondaryAskCcmPage secondaryAskCcmPage) {
		this.secondaryAskCcmPage = secondaryAskCcmPage;
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

	public EditText getRedEyesDurationEditText() {
		return redEyesDurationEditText;
	}

	public void setRedEyesDurationEditText(EditText redEyesDurationEditText) {
		this.redEyesDurationEditText = redEyesDurationEditText;
	}

	public EditText getSeeingDifficultyDurationEditText() {
		return seeingDifficultyDurationEditText;
	}

	public void setSeeingDifficultyDurationEditText(EditText seeingDifficultyDurationEditText) {
		this.seeingDifficultyDurationEditText = seeingDifficultyDurationEditText;
	}

	public ViewGroup getAnimatedTopLevelView() {
		return animatedTopLevelView;
	}

	public void setAnimatedTopLevelView(ViewGroup animatedTopLevelView) {
		this.animatedTopLevelView = animatedTopLevelView;
	}

	public View getSeeingDifficultyView() {
		return seeingDifficultyView;
	}

	public void setSeeingDifficultyView(View seeingDifficultyView) {
		this.seeingDifficultyView = seeingDifficultyView;
	}

	public DynamicView getVomitingDynamicView() {
		return vomitingDynamicView;
	}

	public void setVomitingDynamicView(DynamicView vomitingDynamicView) {
		this.vomitingDynamicView = vomitingDynamicView;
	}

	public DynamicView getRedEyesDurationDynamicView() {
		return redEyesDurationDynamicView;
	}

	public void setRedEyesDurationDynamicView(DynamicView redEyesDurationDynamicView) {
		this.redEyesDurationDynamicView = redEyesDurationDynamicView;
	}

	public DynamicView getSeeingDifficultyDurationDynamicView() {
		return seeingDifficultyDurationDynamicView;
	}

	public void setSeeingDifficultyDurationDynamicView(DynamicView seeingDifficultyDurationDynamicView) {
		this.seeingDifficultyDurationDynamicView = seeingDifficultyDurationDynamicView;
	}

	public RadioGroup getRedEyesRadioGroup() {
		return redEyesRadioGroup;
	}

	public void setRedEyesRadioGroup(RadioGroup redEyesRadioGroup) {
		this.redEyesRadioGroup = redEyesRadioGroup;
	}

	public RadioGroup getVomitingRadioGroup() {
		return vomitingRadioGroup;
	}

	public void setVomitingRadioGroup(RadioGroup vomitingRadioGroup) {
		this.vomitingRadioGroup = vomitingRadioGroup;
	}

	public RadioGroup getVomitsEverythingRadioGroup() {
		return vomitsEverythingRadioGroup;
	}

	public void setVomitsEverythingRadioGroup(RadioGroup vomitsEverythingRadioGroup) {
		this.vomitsEverythingRadioGroup = vomitsEverythingRadioGroup;
	}

	public RadioGroup getSeeingDifficultyRadioGroup() {
		return seeingDifficultyRadioGroup;
	}

	public void setSeeingDifficultyRadioGroup(RadioGroup seeingDifficultyRadioGroup) {
		this.seeingDifficultyRadioGroup = seeingDifficultyRadioGroup;
	}

	public View getVomitingView() {
		return vomitingView;
	}

	public void setVomitingView(View vomitingView) {
		this.vomitingView = vomitingView;
	}

	public View getRedEyesView() {
		return redEyesView;
	}

	public void setRedEyesView(View redEyesView) {
		this.redEyesView = redEyesView;
	}

	public Boolean isAnimatedVomitsEverythingViewInVisibleState() {
		return animatedVomitsEverythingViewInVisibleState;
	}

	public void setAnimatedVomitsEverythingViewInVisibleState(Boolean animatedVomitsEverythingViewInVisibleState) {
		this.animatedVomitsEverythingViewInVisibleState = animatedVomitsEverythingViewInVisibleState;
	}

	public Boolean isAnimatedRedEyesDurationViewInVisibleState() {
		return animatedRedEyesDurationViewInVisibleState;
	}

	public void setAnimatedRedEyesDurationViewInVisibleState(Boolean animatedRedEyesDurationViewInVisibleState) {
		this.animatedRedEyesDurationViewInVisibleState = animatedRedEyesDurationViewInVisibleState;
	}

	public Boolean isAnimatedSeeingDifficultyDurationViewInVisibleState() {
		return animatedSeeingDifficultyDurationViewInVisibleState;
	}

	public void setAnimatedSeeingDifficultyDurationViewInVisibleState(Boolean animatedSeeingDifficultyDurationViewInVisibleState) {
		this.animatedSeeingDifficultyDurationViewInVisibleState = animatedSeeingDifficultyDurationViewInVisibleState;
	}

	public RadioGroup getCannotTreatProblemsRadioGroup() {
		return cannotTreatProblemsRadioGroup;
	}

	public void setCannotTreatProblemsRadioGroup(RadioGroup cannotTreatProblemsRadioGroup) {
		this.cannotTreatProblemsRadioGroup = cannotTreatProblemsRadioGroup;
	}

	public EditText getCannotTreatProblemsDetailsEditText() {
		return cannotTreatProblemsDetailsEditText;
	}

	public void setCannotTreatProblemsDetailsEditText(EditText cannotTreatProblemsDetailsEditText) {
		this.cannotTreatProblemsDetailsEditText = cannotTreatProblemsDetailsEditText;
	}

	public View getCannotTreatProblemsView() {
		return cannotTreatProblemsView;
	}

	public void setCannotTreatProblemsView(View cannotTreatProblemsView) {
		this.cannotTreatProblemsView = cannotTreatProblemsView;
	}

	public DynamicView getCannotTreatProblemsDetailsDynamicView() {
		return cannotTreatProblemsDetailsDynamicView;
	}

	public void setCannotTreatProblemsDetailsDynamicView(DynamicView cannotTreatProblemsDetailsDynamicView) {
		this.cannotTreatProblemsDetailsDynamicView = cannotTreatProblemsDetailsDynamicView;
	}

	public Boolean isAnimatedCannotTreatProblemsDetailsViewInVisibleState() {
		return animatedCannotTreatProblemsDetailsViewInVisibleState;
	}

	public void setAnimatedCannotTreatProblemsDetailsViewInVisibleState(
			Boolean animatedCannotTreatProblemsDetailsViewInVisibleState) {
		this.animatedCannotTreatProblemsDetailsViewInVisibleState = animatedCannotTreatProblemsDetailsViewInVisibleState;
	}
	
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}