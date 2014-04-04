package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.analytics.DataAnalytic;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.imci.ui.ReviewFragmentCallbacks;
import ie.ucc.bis.supportinglife.assessment.imci.ui.StepPagerStrip;
import ie.ucc.bis.supportinglife.assessment.model.AbstractAssessmentPage;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentPagerAdapter;
import ie.ucc.bis.supportinglife.assessment.model.ModelCallbacks;
import ie.ucc.bis.supportinglife.assessment.model.listener.AssessmentExitDialogListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

/**
 * 
 * @author timothyosullivan
 */

public class AssessmentActivity extends SupportingLifeBaseActivity implements
		PageFragmentCallbacks, ReviewFragmentCallbacks, ModelCallbacks {

	protected static final String ASSESSMENT_REVIEW_ITEMS = "ASSESSMENT_REVIEW_ITEMS";
	
	private ViewPager viewPager;
    private AssessmentPagerAdapter assessmentPagerAdapter;
	private AbstractModel assessmentModel;
	private StepPagerStrip stepPagerStrip;
	
    private boolean editingAfterReview;
    private boolean consumePageSelectedEvent;

    private Button nextButton;
	private Button prevButton;
	
	@Override
    public void onPageTreeChanged() {
        recalculateCutOffPage();
        getStepPagerStrip().setPageCount(getAssessmentModel().getAssessmentPageSequence().size() + 1); // + 1 = review step
        getAssessmentPagerAdapter().notifyDataSetChanged();
        updateBottomBar();
    }

    public void onEditScreenAfterReview(String key) {
        for (int i = getAssessmentModel().getAssessmentPageSequence().size() - 1; i >= 0; i--) {
            if (getAssessmentModel().getAssessmentPageSequence().get(i).getKey().equals(key)) {
                setConsumePageSelectedEvent(true);
                setEditingAfterReview(true);
                getViewPager().setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(AbstractAssessmentPage page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
            	getAssessmentPagerAdapter().notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }
        
    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = getAssessmentModel().getAssessmentPageSequence().size() + 1;
        for (int i = 0; i < getAssessmentModel().getAssessmentPageSequence().size(); i++) {
            AbstractAssessmentPage page = getAssessmentModel().getAssessmentPageSequence().get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (getAssessmentPagerAdapter().getCutOffPage() != cutOffPage) {
        	getAssessmentPagerAdapter().setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }
    

	/**
	 * Method: updateBottomBar
	 * 
	 * Responsible for configuring the display of buttons (i.e. previous;
	 * next; review;) on the Assessment wizard screens
	 * 
	 */      
    protected void updateBottomBar() {
        int position = getViewPager().getCurrentItem();
        if (position == getAssessmentModel().getAssessmentPageSequence().size()) {
            // change text on the next button to indicate
        	// assessment data entry is complete
            getNextButton().setText(R.string.assessment_wizard_finish_button);
        } else {
        	getNextButton().setText(isEditingAfterReview()
                    ? R.string.assessment_wizard_review_button
                    : R.string.assessment_wizard_next_button);
        	getNextButton().setBackgroundResource(R.drawable.breadcrumb_next_button);
        	getNextButton().setTextColor(getResources().getColor(R.color.White));

            getNextButton().setEnabled(position != getAssessmentPagerAdapter().getCutOffPage());
        }

        getPrevButton().setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }
    
	/**
	 * Method: getPage
	 * 
	 * Implementation support for 'PageFragmentCallbacks' Interface
	 * 
	 * Retrieve a bread-crumb UI Wizard page based on the key
	 * 
	 * @param key : String
	 */    
    public AbstractAssessmentPage getPage(String key) {
        return getAssessmentModel().findAssessmentPageByKey(key);
    }   

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getAssessmentModel().unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("assessmentModel", getAssessmentModel().save());
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
        	getAssessmentModel().load(savedInstanceState.getBundle("assessmentModel"));
        }  	
    }
    
	/**
	 * Method: getWizardModel
	 * 
	 * Implementation support for 'PageFragmentCallbacks' Interface
	 * 
	 * @return AbstractWizardModel
	 */
    public AbstractModel getWizardModel() {
    	return getAssessmentModel();
    }
    
	/**
	 * Click Handler: Handle the click on the home button
	 * 
	 * @param view View
	 * @return void
	 */
    @Override
    public void onClickHome(View view) {
    	// if user is performing an IMCI or CCM assessment then 
    	// display a confirmation dialog to confirm that the user wishes 
    	// to exit the patient assessment
    	exitAssessmentDialogHandler(AssessmentExitDialogListener.DASHBOARD_SCREEN);
    }
    
	/**
	 * Click Handler: Handle the back button click event such that if user 
	 * is performing an IMCI or CCM assessment then a confirmation dialog 
	 * will be displayed to confirm that the user wishes to exit the 
	 * patient assessment
	 * 
	 * @param view View
	 * @return void
	 */
    @Override
    public void onBackPressed() {
    	// Handle the back button click event such that if user 
    	// is performing an IMCI or CCM assessment then a confirmation dialog 
    	// will be displayed to confirm that the user wishes to exit the 
    	// patient assessment
    	exitAssessmentDialogHandler(AssessmentExitDialogListener.DASHBOARD_SCREEN);
    }
    
	/**
	 * Click Handler: Handle the click on the 'Settings' Action Bar item
	 * 
	 * @param view View
	 * @return void
	 */
	@Override
	public void onClickSettings() {
    	// if user is performing an IMCI or CCM assessment then 
    	// display a confirmation dialog to confirm that the user wishes 
    	// to exit the patient assessment
    	exitAssessmentDialogHandler(AssessmentExitDialogListener.SETTINGS_SCREEN);
	}
	
	/**
	 * Click Handler: Handle the click on the 'Sync' Action Bar item
	 * 
	 * @param view View
	 * @return void
	 */
	@Override
	public void onClickSync() {
    	// if user is performing an IMCI or CCM assessment then 
    	// display a confirmation dialog to confirm that the user wishes 
    	// to exit the patient assessment
    	exitAssessmentDialogHandler(AssessmentExitDialogListener.SYNC_SCREEN);
	}
	
	/**
	 * Click Handler: Handle the click on the 'Help' Action Bar item
	 * 
	 * @param view View
	 * @return void
	 */
	@Override
	public void onClickHelp() {
    	// if user is performing an IMCI or CCM assessment then 
    	// display a confirmation dialog to confirm that the user wishes 
    	// to exit the patient assessment
    	exitAssessmentDialogHandler(AssessmentExitDialogListener.HELP_SCREEN);
	}
    
    /**
     * Inner Class: AssessmentDialogListener
     * 
     * Provides OnClick handler functionality for Submit Button
     * on the Assessment Submission Dialog
     * 
     * @author TOSullivan
     *
     */
    protected final class AssessmentDialogListener implements DialogInterface.OnClickListener {
    	
    	private SupportingLifeBaseActivity resultsActivity;

    	/**
    	 * Constructor
    	 */
    	public AssessmentDialogListener(SupportingLifeBaseActivity resultsActivity) {
    		this.resultsActivity = resultsActivity;
    	}
    	
		public void onClick(DialogInterface dialog, int which) {
			
			Intent intent = new Intent(getApplicationContext(), resultsActivity.getClass());
			intent.putExtra(ASSESSMENT_REVIEW_ITEMS, getAssessmentModel().gatherAssessmentReviewItems());
			startActivity(intent);

	        // need to take note of the data analytics associated with the patient assessment pages
			Tracker tracker = GoogleAnalytics.getInstance(getApplicationContext()).getDefaultTracker();
			
			// configure demographic information of user
			String ageRange = "25-39";
			String gender = "male";
			
			tracker.setCustomDimension(1, ageRange);
			tracker.setCustomDimension(2, gender);
			// Dimension value is associated and sent with this hit.
			tracker.sendView();
			
			for (DataAnalytic dataAnalyticItem : getAssessmentModel().gatherPageDataAnalytics()) {
				tracker.sendEvent(dataAnalyticItem.getCategory(), dataAnalyticItem.getAction(), 
						dataAnalyticItem.getLabel(), dataAnalyticItem.getValue());
			}
			
			// configure the activity animation transition effect
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
    } // end of inner class
    
    
	/**
	 * Getter Method: getAssessmentPagerAdapter()
	 */			
	public AssessmentPagerAdapter getAssessmentPagerAdapter() {
		return assessmentPagerAdapter;
	}

	/**
	 * Setter Method: setAssessmentPagerAdapter()
	 */   	
	public void setAssessmentPagerAdapter(AssessmentPagerAdapter assessmentPagerAdapter) {
		this.assessmentPagerAdapter = assessmentPagerAdapter;
	}
	
	/**
	 * Getter Method: getViewPager()
	 */		
	public ViewPager getViewPager() {
		return viewPager;
	}

	/**
	 * Setter Method: setViewPager()
	 */  
	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	/**
	 * Getter Method: getStepPagerStrip()
	 */	
	public StepPagerStrip getStepPagerStrip() {
		return stepPagerStrip;
	}

	/**
	 * Setter Method: setStepPagerStrip()
	 */  
	public void setStepPagerStrip(StepPagerStrip stepPagerStrip) {
		this.stepPagerStrip = stepPagerStrip;
	}

	/**
	 * Getter Method: getAssessmentModel()
	 */
	public AbstractModel getAssessmentModel() {
		return assessmentModel;
	}

	/**
	 * Setter Method: setAssessmentModel()
	 */
	public void setAssessmentModel(AbstractModel assessmentModel) {
		this.assessmentModel = assessmentModel;
	}

	/**
	 * Getter Method: getNextButton()
	 */		
    public Button getNextButton() {
		return nextButton;
	}

	/**
	 * Setter Method: setNextButton()
	 */  
	public void setNextButton(Button nextButton) {
		this.nextButton = nextButton;
	}

	/**
	 * Getter Method: getPrevButton()
	 */	
	public Button getPrevButton() {
		return prevButton;
	}

	/**
	 * Setter Method: setPrevButton()
	 */  
	public void setPrevButton(Button prevButton) {
		this.prevButton = prevButton;
	}

	/**
	 * Getter Method: isEditingAfterReview()
	 */
	public boolean isEditingAfterReview() {
		return editingAfterReview;
	}

	/**
	 * Setter Method: setEditingAfterReview()
	 */
	public void setEditingAfterReview(boolean editingAfterReview) {
		this.editingAfterReview = editingAfterReview;
	}

	/**
	 * Getter Method: isConsumePageSelectedEvent()
	 */
	public boolean isConsumePageSelectedEvent() {
		return consumePageSelectedEvent;
	}

	/**
	 * Setter Method: setConsumePageSelectedEvent()
	 */
	public void setConsumePageSelectedEvent(boolean consumePageSelectedEvent) {
		this.consumePageSelectedEvent = consumePageSelectedEvent;
	}
}
