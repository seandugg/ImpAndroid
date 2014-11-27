package ie.ucc.bis.supportinglife.assessment.ccm.ui;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.activity.AssessmentActivity;
import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.ccm.model.GeneralPatientDetailsCcmPage;
import ie.ucc.bis.supportinglife.assessment.imci.model.DynamicView;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentViewPager;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.FragmentValidator;
import ie.ucc.bis.supportinglife.assessment.model.listener.AssessmentWizardTextWatcher;
import ie.ucc.bis.supportinglife.assessment.model.listener.DatePickerListener;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupCoordinatorListener;
import ie.ucc.bis.supportinglife.assessment.model.listener.RadioGroupListener;
import ie.ucc.bis.supportinglife.dao.CustomSharedPreferences;
import ie.ucc.bis.supportinglife.ui.utilities.DateUtilities;
import ie.ucc.bis.supportinglife.ui.utilities.ViewGroupUtilities;
import ie.ucc.bis.supportinglife.validation.Form;
import ie.ucc.bis.supportinglife.validation.NotEmptyValidation;
import ie.ucc.bis.supportinglife.validation.RadioGroupFieldValidations;
import ie.ucc.bis.supportinglife.validation.RadioGroupValidation;
import ie.ucc.bis.supportinglife.validation.TextFieldValidations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Responsible for UI fragment to display CCM 
 * general patient details assessment form
 * 
 * @author timothyosullivan
 * 
 */
public class GeneralPatientDetailsCcmFragment extends Fragment implements FragmentLifecycle, FragmentValidator {
	    
    private GeneralPatientDetailsCcmPage generalPatientDetailsCcmPage;    
    private PageFragmentCallbacks pageFragmentCallbacks;
    private String pageKey;
    private TextView todayDateTextView;
    private EditText hsaEditText;
    private EditText nationalIdEditText;
    private EditText nationalHealthIdEditText;
    private EditText firstNameEditText;
    private EditText surnameEditText;
    private EditText dateBirthEditText;
    private RadioGroup genderRadioGroup;
    private EditText caregiverEditText;
    private RadioGroup relationshipRadioGroup;
    private EditText relationshipSpecifiedEditText;
    private EditText physicalAddressEditText;
    private EditText villageEditText;
    private EditText taEditText;
    private ViewGroup animatedRelationshipSpecifiedView;
    private View relationshipView;
    private DynamicView relationshipSpecifiedDynamicView;
    private Boolean animatedRelationshipViewInVisibleState;
    
	// Form used for validation
    private Form form;
    
    public static GeneralPatientDetailsCcmFragment create(String pageKey) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_KEY, pageKey);

        GeneralPatientDetailsCcmFragment fragment = new GeneralPatientDetailsCcmFragment();
        fragment.setArguments(args);
        fragment.setAnimatedRelationshipViewInVisibleState(false);
        return fragment;
    }

	/**
	 * Constructor
	 *
	 */
    public GeneralPatientDetailsCcmFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        setPageKey(args.getString(ARG_PAGE_KEY));
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {      
      if (getAnimatedRelationshipSpecifiedView().indexOfChild(getRelationshipSpecifiedDynamicView().getWrappedView()) != -1) {
    	  // Animated Fever view is visible
    	  savedInstanceState.putBoolean("animatedRelationshipViewInVisibleState", true);
      }
      else {
    	  // Animated Fever view is invisible
    	  savedInstanceState.putBoolean("animatedRelationshipViewInVisibleState", false);
      }
      super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
    	super.onViewStateRestored(savedInstanceState);
    	if (savedInstanceState != null) {
    		setAnimatedRelationshipViewInVisibleState(savedInstanceState.getBoolean("animatedRelationshipViewInVisibleState"));
    	}

    	if (!isAnimatedRelationshipViewInVisibleState()) {
    		ViewGroupUtilities.removeDynamicViews(getAnimatedRelationshipSpecifiedView(), Arrays.asList(getRelationshipSpecifiedDynamicView()));
    	}

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setGeneralPatientDetailsCcmPage((GeneralPatientDetailsCcmPage) getPageFragmentCallbacks().getPage(getPageKey()));
        
        View rootView = inflater.inflate(R.layout.fragment_ccm_page_general_patient_details, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(getGeneralPatientDetailsCcmPage().getTitle());

        // today's date
        setTodayDateTextView(((TextView) rootView.findViewById(R.id.ccm_general_patient_details_today_date)));
        
        // Health Surveillance Assistant (HSA)
        setHsaEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_hsa_identifier)));
        getHsaEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.HEALTH_SURVEILLANCE_ASSISTANT_DATA_KEY));
        
        // National Id
        setNationalIdEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_national_id)));
        getNationalIdEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.NATIONAL_ID_DATA_KEY));       
  
        // National Health Id
        setNationalHealthIdEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_national_health_id)));
        getNationalHealthIdEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.NATIONAL_HEALTH_ID_DATA_KEY));
        
        // child's first name
        setFirstNameEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_first_name)));
        getFirstNameEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.FIRST_NAME_DATA_KEY));

        // child's surname
        setSurnameEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_surname)));
        getSurnameEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.SURNAME_DATA_KEY));
        
        // date of birth
        setDateBirthEditText((EditText) rootView.findViewById(R.id.ccm_general_patient_details_date_of_birth));
        getDateBirthEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.DATE_OF_BIRTH_DATA_KEY));
       
        // gender
        setGenderRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_general_patient_details_radio_gender));
        getGenderRadioGroup().check(getGeneralPatientDetailsCcmPage()
        		.getPageData().getInt(GeneralPatientDetailsCcmPage.GENDER_DATA_KEY));
        
        // caregiver
        setCaregiverEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_caregiver)));
        getCaregiverEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.CAREGIVER_DATA_KEY));
     
        // configure the animated view of relationship
        // i.e. Relationship: Other --> Specify Relationship Textfield  
        configureRelationshipAnimatedView(rootView);
        
        // physical address
        setPhysicalAddressEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_physical_address)));
        getPhysicalAddressEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.PHYSICAL_ADDRESS_DATA_KEY));
        
        // village
        setVillageEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_village)));
        getVillageEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.VILLAGE_DATA_KEY));
        
        // TA
        setTaEditText(((EditText) rootView.findViewById(R.id.ccm_general_patient_details_ta)));
        getTaEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.TA_DATA_KEY));
        
		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		((SupportingLifeBaseActivity) getActivity()).addSoftKeyboardHandling(rootView);
		
    	configureValidation(rootView);
		
        return rootView;
    }
    
    
	private void configureRelationshipAnimatedView(View rootView) {
		// Relationship view
		setRelationshipView((View) rootView.findViewById(R.id.ccm_general_patient_details_view_relationship));

        // relationship
        setRelationshipRadioGroup((RadioGroup) rootView.findViewById(R.id.ccm_general_patient_details_radio_relationship));
        getRelationshipRadioGroup().check(getGeneralPatientDetailsCcmPage()
        		.getPageData().getInt(GeneralPatientDetailsCcmPage.RELATIONSHIP_DATA_KEY));
        
        // specify relationship
        setRelationshipSpecifiedEditText((EditText) rootView.findViewById(R.id.ccm_general_patient_details_relationship_specified));
        getRelationshipSpecifiedEditText().setText(getGeneralPatientDetailsCcmPage().getPageData().getString(GeneralPatientDetailsCcmPage.RELATIONSHIP_SPECIFIED_DATA_KEY));
        
        // 'specify relationship' is a dynamic view within the UI
        setRelationshipSpecifiedDynamicView(new DynamicView(rootView.findViewById(R.id.ccm_general_patient_details_view_relationship_specified),
        									rootView.findViewById(R.id.ccm_general_patient_details_relationship_specified)));
        
        // get a hold on the top level animated view
        setAnimatedRelationshipSpecifiedView(((ViewGroup) rootView.findViewById(R.id.ccm_general_patient_details_relationship_animated_view)));
        
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
        
        // special case: need to explicitly make an initial call to onResumeFragment to start
        // the page timer
        onResumeFragment(getPageFragmentCallbacks().getWizardModel());

        // today's date
        getTodayDateTextView().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.TODAY_DATE_DATA_KEY));
        getTodayDateTextView().setText(DateUtilities.getTodaysDate());
        
        // Health Surveillance Assistant (HSA)
        getHsaEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.HEALTH_SURVEILLANCE_ASSISTANT_DATA_KEY));
        
        // HSA User Login
		CustomSharedPreferences preferences = CustomSharedPreferences.getPrefs((SupportingLifeBaseActivity) getActivity(), SupportingLifeBaseActivity.APP_NAME, Context.MODE_PRIVATE);
		String hsaUserId = preferences.getString(SupportingLifeBaseActivity.USER_ID, "guest");
        getHsaEditText().setText(hsaUserId);
    
        // national id
        getNationalIdEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.NATIONAL_ID_DATA_KEY));  
        
        // national health id
        getNationalHealthIdEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.NATIONAL_HEALTH_ID_DATA_KEY));  
        
        // child's first name
        getFirstNameEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.FIRST_NAME_DATA_KEY, getForm(), this));  

        // child's surname
        getSurnameEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.SURNAME_DATA_KEY, getForm(), this));
        
        // date of birth
        getDateBirthEditText().setOnFocusChangeListener(new DatePickerListener(this, getGeneralPatientDetailsCcmPage(), 
        		GeneralPatientDetailsCcmPage.DATE_OF_BIRTH_DATA_KEY, getForm()));
        
        // turn off soft keyboard input method for 'Date of Birth' EditText
        getDateBirthEditText().setInputType(InputType.TYPE_NULL);

        // gender
        getGenderRadioGroup().setOnCheckedChangeListener(
        		new RadioGroupListener(getGeneralPatientDetailsCcmPage(),
        				GeneralPatientDetailsCcmPage.GENDER_DATA_KEY, getForm(), this));
        
        // caregiver
        getCaregiverEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.CAREGIVER_DATA_KEY, getForm(), this));
        
        // add dynamic view listener to relationship radio group
        addRelationshipDynamicViewListener();  
                
        // physical address
        getPhysicalAddressEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.PHYSICAL_ADDRESS_DATA_KEY, getForm(), this));
        
        // village
        getVillageEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.VILLAGE_DATA_KEY, getForm(), this));
        
        // TA
        getTaEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.TA_DATA_KEY, getForm(), this));
    }

	/**
	 * addRelationshipDynamicViewListener()
	 * 
	 * Responsible for adding a listener to the Relationship view
	 * 
	 */
	private void addRelationshipDynamicViewListener() {
        
		List<String> animateUpRadioButtonTextTriggers = new ArrayList<String>();
		animateUpRadioButtonTextTriggers.add(getResources().getString(R.string.ccm_general_patient_details_radio_relationship_mother));
		animateUpRadioButtonTextTriggers.add(getResources().getString(R.string.ccm_general_patient_details_radio_relationship_father));
        
		TextFieldValidations specifyRelationshipValidation = TextFieldValidations.using(getRelationshipSpecifiedEditText(), 
				getResources().getString(R.string.ccm_general_patient_details_relationship_specified_label))
				.validate(NotEmptyValidation.build(this.getActivity()));
        getRelationshipRadioGroup().setOnCheckedChangeListener(
        		new RadioGroupCoordinatorListener(getGeneralPatientDetailsCcmPage(),
        				GeneralPatientDetailsCcmPage.RELATIONSHIP_DATA_KEY, 
        				Arrays.asList(getRelationshipSpecifiedDynamicView()),
        				getAnimatedRelationshipSpecifiedView(),
        				getRelationshipView(),
        				animateUpRadioButtonTextTriggers,
        				getForm(), this, specifyRelationshipValidation));
        
        // add listener to 'specify relationship'
        getRelationshipSpecifiedEditText().addTextChangedListener(
        		new AssessmentWizardTextWatcher(getGeneralPatientDetailsCcmPage(), 
        				GeneralPatientDetailsCcmPage.RELATIONSHIP_SPECIFIED_DATA_KEY, getForm(), this));
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
	 * Responsible for configuring validation on the CCM page
	 * @param rootView 
	 */
	private void configureValidation(View rootView) {
        setForm(new Form(this.getActivity()));

        // validation rules
        getForm().addTextFieldValidations(TextFieldValidations.using(getFirstNameEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_first_name_label))
        		.validate(NotEmptyValidation.build(this.getActivity())));
        getForm().addTextFieldValidations(TextFieldValidations.using(getSurnameEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_surname_label))
        		.validate(NotEmptyValidation.build(this.getActivity())));
        getForm().addTextFieldValidations(TextFieldValidations.using(getDateBirthEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_date_of_birth_label))
        		.validate(NotEmptyValidation.build(this.getActivity())));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getGenderRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_general_patient_details_radio_gender_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));
        getForm().addTextFieldValidations(TextFieldValidations.using(getCaregiverEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_caregiver_label))
        		.validate(NotEmptyValidation.build(this.getActivity())));
        getForm().addRadioGroupFieldValidations(RadioGroupFieldValidations.using(getRelationshipRadioGroup(), 
        		(TextView) rootView.findViewById(R.id.ccm_general_patient_details_radio_relationship_label))
        		.validate(RadioGroupValidation.build(this.getActivity())));
        getForm().addTextFieldValidations(TextFieldValidations.using(getPhysicalAddressEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_physical_address_label))
        		.validate(NotEmptyValidation.build(this.getActivity())));              
        getForm().addTextFieldValidations(TextFieldValidations.using(getVillageEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_village_label)).validate(NotEmptyValidation.build(this.getActivity())));
        getForm().addTextFieldValidations(TextFieldValidations.using(getTaEditText(), 
        		getResources().getString(R.string.ccm_general_patient_details_ta_label)).validate(NotEmptyValidation.build(this.getActivity())));    
        
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
	
	public GeneralPatientDetailsCcmPage getGeneralPatientDetailsCcmPage() {
		return generalPatientDetailsCcmPage;
	}
	
	public void setGeneralPatientDetailsCcmPage(GeneralPatientDetailsCcmPage generalPatientDetailsCcmPage) {
		this.generalPatientDetailsCcmPage = generalPatientDetailsCcmPage;
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

	public TextView getTodayDateTextView() {
		return todayDateTextView;
	}

	public void setTodayDateTextView(TextView todayDateTextView) {
		this.todayDateTextView = todayDateTextView;
	}

	public EditText getNationalIdEditText() {
		return nationalIdEditText;
	}

	public void setNationalIdEditText(EditText nationalIdEditText) {
		this.nationalIdEditText = nationalIdEditText;
	}

	public EditText getNationalHealthIdEditText() {
		return nationalHealthIdEditText;
	}

	public void setNationalHealthIdEditText(EditText nationalHealthIdEditText) {
		this.nationalHealthIdEditText = nationalHealthIdEditText;
	}

	public EditText getHsaEditText() {
		return hsaEditText;
	}

	public void setHsaEditText(EditText hsaEditText) {
		this.hsaEditText = hsaEditText;
	}

	public EditText getFirstNameEditText() {
		return firstNameEditText;
	}

	public void setFirstNameEditText(EditText firstNameEditText) {
		this.firstNameEditText = firstNameEditText;
	}

	public EditText getSurnameEditText() {
		return surnameEditText;
	}

	public void setSurnameEditText(EditText surnameEditText) {
		this.surnameEditText = surnameEditText;
	}

	public RadioGroup getGenderRadioGroup() {
		return genderRadioGroup;
	}

	public void setGenderRadioGroup(RadioGroup genderRadioGroup) {
		this.genderRadioGroup = genderRadioGroup;
	}

	public EditText getDateBirthEditText() {
		return dateBirthEditText;
	}

	public void setDateBirthEditText(EditText dateBirthEditText) {
		this.dateBirthEditText = dateBirthEditText;
	}

	public EditText getCaregiverEditText() {
		return caregiverEditText;
	}

	public void setCaregiverEditText(EditText caregiverEditText) {
		this.caregiverEditText = caregiverEditText;
	}

	public RadioGroup getRelationshipRadioGroup() {
		return relationshipRadioGroup;
	}

	public void setRelationshipRadioGroup(RadioGroup relationshipRadioGroup) {
		this.relationshipRadioGroup = relationshipRadioGroup;
	}

	public EditText getRelationshipSpecifiedEditText() {
		return relationshipSpecifiedEditText;
	}

	public void setRelationshipSpecifiedEditText(EditText relationshipSpecifiedEditText) {
		this.relationshipSpecifiedEditText = relationshipSpecifiedEditText;
	}

	public EditText getPhysicalAddressEditText() {
		return physicalAddressEditText;
	}

	public void setPhysicalAddressEditText(EditText physicalAddressEditText) {
		this.physicalAddressEditText = physicalAddressEditText;
	}

	public EditText getVillageEditText() {
		return villageEditText;
	}

	public void setVillageEditText(EditText villageEditText) {
		this.villageEditText = villageEditText;
	}

	public EditText getTaEditText() {
		return taEditText;
	}

	public void setTaEditText(EditText taEditText) {
		this.taEditText = taEditText;
	}

	public ViewGroup getAnimatedRelationshipSpecifiedView() {
		return animatedRelationshipSpecifiedView;
	}

	public void setAnimatedRelationshipSpecifiedView(ViewGroup animatedRelationshipSpecifiedView) {
		this.animatedRelationshipSpecifiedView = animatedRelationshipSpecifiedView;
	}

	public View getRelationshipView() {
		return relationshipView;
	}

	public void setRelationshipView(View relationshipView) {
		this.relationshipView = relationshipView;
	}

	public DynamicView getRelationshipSpecifiedDynamicView() {
		return relationshipSpecifiedDynamicView;
	}

	public void setRelationshipSpecifiedDynamicView(DynamicView relationshipOtherDynamicView) {
		this.relationshipSpecifiedDynamicView = relationshipOtherDynamicView;
	}

	public Boolean isAnimatedRelationshipViewInVisibleState() {
		return animatedRelationshipViewInVisibleState;
	}

	public void setAnimatedRelationshipViewInVisibleState(Boolean animatedRelationshipViewInVisibleState) {
		this.animatedRelationshipViewInVisibleState = animatedRelationshipViewInVisibleState;
	}
	
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
}
