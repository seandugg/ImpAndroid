package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.imci.model.ImciAssessmentModel;
import ie.ucc.bis.supportinglife.assessment.imci.ui.PageSelectedListener;
import ie.ucc.bis.supportinglife.assessment.imci.ui.StepPagerStrip;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentPagerAdapter;
import ie.ucc.bis.supportinglife.assessment.model.AssessmentViewPager;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author timothyosullivan
 */

public class ImciAssessmentActivity extends AssessmentActivity {
    
	/**
	 * OnCreate method is called when the activity is first created.
	 * 
	 * This is where all of the normal static set up should occur
	 * e.g. create views, bind data to lists, etc.
	 * 
	 * The method also provides a Bundle parameter containing the activity's
	 * previously frozen state (if there was one).
	 * 
	 * This method is always followed by onStart().
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAssessmentModel(new ImciAssessmentModel(this));
        
        setContentView(R.layout.activity_imci_assessment);
        
        setTitleFromActivityLabel(R.id.action_bar_title_text);
        
        if (savedInstanceState != null) {
        	getAssessmentModel().load(savedInstanceState.getBundle("assessmentModel"));
        }

        getAssessmentModel().registerListener(this);

        setAssessmentPagerAdapter(new AssessmentPagerAdapter(this, getSupportFragmentManager()));
        setAssessmentViewPager((AssessmentViewPager) findViewById(R.id.pager));
        getAssessmentViewPager().setAdapter(getAssessmentPagerAdapter());
        setStepPagerStrip((StepPagerStrip) findViewById(R.id.strip));
        
        // configure listener on StepPagerStrip UI component
        getStepPagerStrip().setPageSelectedListener(new PageSelectedListener() {
            public void onPageStripSelected(int position) {
                position = Math.min(getAssessmentPagerAdapter().getCount() - 1, position);
                if (getAssessmentViewPager().getCurrentItem() != position) {
                	getAssessmentViewPager().setCurrentItem(position);
                }
            }
        });

        setNextButton((Button) findViewById(R.id.next_button));
        setPrevButton((Button) findViewById(R.id.prev_button));

        getAssessmentViewPager().setOnPageChangeListener(pageChangeListener);

        // configure click listener on Next Button       
        getNextButton().setOnClickListener(new ImciAssessmentActivity.NextButtonListener());

        getPrevButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	getAssessmentViewPager().setCurrentItem(getAssessmentViewPager().getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
        
		// add soft keyboard handler - essentially hiding soft
		// keyboard when an EditText is not in focus
		 addSoftKeyboardHandling(findViewById(R.id.assessment_wizard));
    }

    /**
     * Anonymous Inner Class: OnPageChangeListener
     * 
     * Provides functionality to listen to ViewPager change events
     * to alert fragments regarding pausing and resuming activity.
     * Essentially provides hooks for analytic timing tracking.
     * 
     * Additionally facilitates notification to update the StepPageStrip
     * and the bottom bar
     * 
     * @author TOSullivan
     *
     */    
    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
    	int currentPosition = 0;

    	@Override
    	public void onPageSelected(int newPosition) {
    		// inform respective fragment via the FragmentLifecycle interface of pause or resumption
    		// event
    		FragmentLifecycle fragmentToShow = (FragmentLifecycle) getAssessmentPagerAdapter().getItem(newPosition);
    		fragmentToShow.onResumeFragment(getAssessmentModel());

    		FragmentLifecycle fragmentToHide = (FragmentLifecycle) getAssessmentPagerAdapter().getItem(currentPosition);
    		fragmentToHide.onPauseFragment(getAssessmentModel());

    		currentPosition = newPosition;
    		
    		// additionally need to update the StepPageStrip
        	getStepPagerStrip().setCurrentPage(newPosition);          	
        	
	        if (isConsumePageSelectedEvent()) {
	            setConsumePageSelectedEvent(false);
	            return;
	        }
	
	        setEditingAfterReview(false);
	        updateBottomBar();
    	}

    	@Override
    	public void onPageScrolled(int arg0, float arg1, int arg2) { }

    	public void onPageScrollStateChanged(int arg0) { }
    };
    
    /**
     * Inner Class: NextButtonListener
     * 
     * Provides OnClick handler functionality for Next Button
     * on the Wizard bread-crumb UI 
     * 
     * @author TOSullivan
     *
     */
    private final class NextButtonListener implements View.OnClickListener {
		public void onClick(View view) {
		    if (getAssessmentViewPager().getCurrentItem() == getAssessmentModel().getAssessmentPageSequence().size()) {
		    	// we're currently on the review pane so display confirmation dialog
		        DialogFragment dg = new DialogFragment() {
		            @Override
		            public Dialog onCreateDialog(Bundle savedInstanceState) {
		            	ImciAssessmentResultsActivity imciAssessmentResultsActivity = new ImciAssessmentResultsActivity();
		                return new AlertDialog.Builder(getActivity())
		                        .setMessage(R.string.submit_confirm_message)
		                        .setPositiveButton(R.string.submit_confirm_button, new AssessmentDialogListener(imciAssessmentResultsActivity))
		                        .setNegativeButton(android.R.string.cancel, null)
		                        .create();
		            }
		        };
		        
				// before gathering analytic data, call the 'on pause' operation on the review fragment to make
		        // sure the stop and duration timers for this page are accounted for
	    		FragmentLifecycle fragmentToHide = (FragmentLifecycle) getAssessmentPagerAdapter().getItem(getAssessmentViewPager().getCurrentItem());
	    		fragmentToHide.onPauseFragment(getAssessmentModel());		        
		        
		        dg.show(getSupportFragmentManager(), "Submit Assessment");
		    } else {
		        if (isEditingAfterReview()) {
		        	getAssessmentViewPager().setCurrentItem(getAssessmentPagerAdapter().getCount() - 1);
		        } else {
		        	getAssessmentViewPager().setCurrentItem(getAssessmentViewPager().getCurrentItem() + 1);
		        }
		    }
		}
	} // end of inner class
}