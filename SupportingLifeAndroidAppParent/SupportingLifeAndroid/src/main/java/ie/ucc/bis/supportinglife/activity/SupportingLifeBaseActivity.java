package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import ie.ucc.bis.supportinglife.assessment.model.FragmentLifecycle;
import ie.ucc.bis.supportinglife.assessment.model.listener.AssessmentExitDialogListener;
import ie.ucc.bis.supportinglife.dao.CustomSharedPreferences;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * This is the base class for activities in the Supporting LIFE
 * application.
 * 
 * It implements methods that are useful to all top level activities.
 * This includes the following:
 * 
 * (1) Stub methods for all the activity lifecycle methods.
 * (2) onClick methods for clicks on home, initial patient visit, patient search, etc.
 * (3) A method for displaying a message to the screen via the Toast class.
 * 
 * @author Tim O Sullivan
 *
 */
public abstract class SupportingLifeBaseActivity extends FragmentActivity {
	
	protected static final String DEV_BASE_URL = "http://143.239.97.70:8080/SupportingLife/";
	protected static final String AWS_BASE_URL = "http://supportinglife.elasticbeanstalk.com/";
	
	protected static final String USER_TYPE_KEY = "user_type";
	protected static final String GUEST_USER = "guest_user";
	protected static final String HSA_USER = "hsa_user";
	public static final String USER_ID = "user_id";
	public static final String USER_KEY = "user_key";
	public static final String APP_NAME = "Supporting LIFE";
	
	protected static final String FONT_AWESOME_TYPEFACE_ASSET = "fonts/fontawesome-webfont.ttf";
	protected static final String DASHBOARD_ICON_TYPEFACE_ASSET = "fonts/dashboard-flaticon.ttf";
	protected static final String FEATURE_UNIMPLEMENTED = "Feature not yet implemented";
	public static final String EXIT_ASSESSMENT_DIALOG_TAG = "Exit Assessment";
	public static final String LANGUAGE_SELECTION_KEY = "language_selection";
	public static final String BREATHING_DURATION_SELECTION_KEY = "breathing_duration_selection";

	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (shouldDisplayActionBar()) {
			// configure custom action bar
			configureActionBar();
		}
	}

	/**
	 * onDestroy method is the final method called before the activity is destroyed.
	 * 
	 * This can happen either because the activity is finishing (someone called 
	 * finish() on it, or because the system is temporarily destroying this
	 * instance of the activity to save space). These two scenarios can be
	 * distinguished with the isFinishing() method.
	 * 
	 */	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * onPause method is called when the system is about to start resuming a previous
	 * activity.
	 * 
	 * This method is typically used to commit unsaved changes to persistent data, stop
	 * animations and anything else that may be consuming CPU, etc.
	 * 
	 * Implementations of this method must be very quick because the next activity will
	 * not be resumed until this method returns.
	 * 
	 * This method is followed by either onResume() if the activity returns back to the
	 * front, or onStop() if the activity becomes invisible to the user.
	 * 
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * onRestart method is called when the activity will start interacting with the user.
	 * 
	 * At this point, the activity is at the top of the activity stack, with user input
	 * going into it.
	 * 
	 * This method is always followed by onPause().
	 * 	
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	/**
	 * onStart method is called when the activity is becoming visible to the user.
	 * 
	 * This method is followed by onResume() if the activity comes to the foreground.
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	/**
	 * onResume method is called when the activity will start interacting with the user.
	 * 
	 * At this point your activity is at the top of the activity stack, with user input going to it.
	 * 
	 * This method is always followed by onPause().
	 *
	 */
	@Override
	protected void onResume () {
		super.onResume ();
	}

	/**
	 * onStop method is called when the activity is no longer visible to the user
	 * because another activity has been resumed and is covering this one.
	 * 
	 * This may happen either because a new activity is being started, an existing one 
	 * is being brought in front of this one, or this one is being destroyed.
	 *
	 * This method is followed by either onRestart() if this activity is coming back to interact with the user, 
	 * or onDestroy() if this activity is going away.
	 */
	@Override
	protected void onStop () {
		super.onStop ();
		EasyTracker.getInstance().activityStop(this);
	}
	
	/**
	 * Method to return to the Home activity
	 * 
	 * @param context Context
	 * @return void
	 */
	public void goHome(Context context, boolean clearActivityStack) {
		launchActivity(HomeActivity.class, clearActivityStack);
	}
	
	/**
	 * Click Handler: Handle the click on the home button
	 * 
	 * @param view View
	 * @return void
	 */
	public void onClickHome(View view) {
		goHome(this, true);
	}
		
	/**
	 * Method to go to the Settings activity
	 * 
	 * @return void
	 */
	public void goToSettingsScreen(boolean clearActivityStack) {
		launchActivity(SettingsActivity.class, clearActivityStack);
	}
	
	/**
	 * Method to go to the Sync activity
	 * 
	 * @return void
	 */
	public void goToSyncScreen(boolean clearActivityStack) {
		launchActivity(SyncActivity.class, clearActivityStack);
	}
	
	/**
	 * Method to go to the Help activity
	 * 
	 * @return void
	 */
	public void goToHelpScreen(boolean clearActivityStack) {
//		launchActivity(HelpActivity.class, clearActivityStack);
	}
	
	/**
	 * Clean handler method for launching a fresh activity. Has capability to clear 
	 * the activity stack so that the user is unable to use 'back button' to revert back
	 * to previous activity.
	 * 
	 * @param class1
	 */
	protected <T extends SupportingLifeBaseActivity> void launchActivity(Class<T> baseActivityClass, boolean clearActivityStack) {
		final Intent intent = new Intent(getApplicationContext(), baseActivityClass);
		if (clearActivityStack) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			finish();
		}
		else {
			this.startActivity(intent);
		}

		// configure the activity animation transition effect
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	/**
	 * Click Handler: Handle the click on the 'Settings' Action Bar item
	 * 
	 * @return void
	 */
	public void onClickSettings() {
		goToSettingsScreen(false);
	}
	
	/**
	 * Click Handler: Handle the click on the 'Sync' Action Bar item
	 * 
	 * @return void
	 */
	public void onClickSync() {
		goToSyncScreen(false);
	}
	
	/**
	 * Click Handler: Handle the click on the 'Help' Action Bar item
	 * 
	 * @return void
	 */
	public void onClickHelp() {
		goToHelpScreen(false);
	}
	
	/**
	 * Method to use the activity label to set the text in the activity's title text view.
	 * The argument gives the name of the view.
	 * 
	 * This method is needed because we have a custom title bar rather than the default 
	 * Android title bar.
	 * 
	 * @param textViewId int
	 * @return void
	 */
	public void setTitleFromActivityLabel(int textViewId) {
		TextView textView = (TextView) findViewById(textViewId);
		if (textView != null) {
			textView.setText(getTitle());
		}
	}
	
	/**
	 * Utility method to show a string on the screen via Toast
	 * @param msg String
	 * @return void
	 */
	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Utility method to send a message to the debug log and display it using Toast
	 * @param msg String
	 * @return void
	 */
	public void trace(String msg) {
		Log.d("Supporting LIFE", msg);
		toast(msg);
	}

	/**
	 * Utility method to configure the locale of the application
	 * 
	 * @param lang String
	 * @return void
	 */
	public void setLocale(String lang) { 
		Locale userLocale = new Locale(lang); 
		
		// ensure the fragment is attached - i.e. onAttach callback is invoked
		if (getResources() != null) {
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics(); 
			Configuration config = getResources().getConfiguration();

			config.locale = userLocale; 
			getResources().updateConfiguration(config, displayMetrics);
		}
	} 
	
	/**
	 * Utility method (recursive) to add a touch listener to each non-EditText UI
	 * component such that the keyboard is hidden once this component is touched.
	 * This facilitates cleaner user interaction as the keyboard is hidden automatically
	 * saving the user an additional click operation.
	 *  
	 * @param view
	 */
	public void addSoftKeyboardHandling(View view) {
	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {
	        view.setOnTouchListener(new OnTouchListener() {
	        	
	            public boolean onTouch(View v, MotionEvent event) {
	                hideSoftKeyboard();
	                return false;
	            }
	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            View innerView = ((ViewGroup) view).getChildAt(i);
	            addSoftKeyboardHandling(innerView);
	        }
	    }
	}
	
	/**
	 * Utility method to hide the soft keyboard from window
	 *  
	 * @param view
	 */
	public void hideSoftKeyboard() {
	    if ((getCurrentFocus() != null) && (getCurrentFocus() instanceof EditText)) {
	    	InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
	    	inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	    	
	    	// editText has just been left by user so want to remove focus
	    	// [orange highlight and flashing cursor will be removed]
	    	
	    	// 1. To achieve this set the following attribute on the parent
	    	// layout view - android:focusableInTouchMode="true" 
	    	// This allows this view to receive focus
	    	
	    	// 2. Have the parent layout request focus
	    	((ViewGroup) getWindow().getDecorView()).getChildAt(0).requestFocus();
	    }
	}
	
	protected abstract boolean shouldDisplayActionBar();
	
	/**
	 * Responsible for configuring custom action bar for activity
	 * 
	 */
	private void configureActionBar() {
		
		// choose the layout of the action bar
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
		
		// Set up ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);
		       
		// need to collapse the default home button on the action bar as
		// otherwise our custom 'home icon' will be pushed out to the right
		View homeIcon = findViewById(android.R.id.home);
		((View) homeIcon.getParent()).setVisibility(View.GONE);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		// need to check the type of user using the SL app
		// if the user is a 'guest' user then we need to
		// hide the 'sync' option from the overflow menu
		if (isGuestUser()) {
			menu.removeItem(R.id.dashboard_action_sync);			
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	protected boolean isGuestUser() {
		boolean guestUser = true;
		
		// need to check the type of user using the SL app
		// if the user is a 'guest' user then we need to
		// hide the 'sync' option from the overflow menu
		CustomSharedPreferences preferences = CustomSharedPreferences.getPrefs(this, APP_NAME, Context.MODE_PRIVATE);
		if (preferences.getString(USER_TYPE_KEY, "").equalsIgnoreCase(HSA_USER) == true) {
			guestUser = false;
		}
		
		return guestUser;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.dashboard_action_settings:
	        	onClickSettings();
	            return true;
	        case R.id.dashboard_action_sync:
	        	onClickSync();
	            return true;
	        case R.id.dashboard_action_help:
	        	toast(FEATURE_UNIMPLEMENTED);
	        	onClickHelp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Home Button or Back Button Click Handler
	 * 
	 * Handle the home button or back button click event such that if user 
	 * is performing an IMCI or CCM assessment (or are in related Assessment 
	 * Results activity) then a confirmation dialog  will be displayed to 
	 * confirm that the user wishes to exit the patient assessment
	 * 
	 * @param navigationRequest 
	 * @param model 
	 * @param currentFragmentPosition
	 * @param fragmentStatePagerAdapter 

	 * @return void
	 */
	protected void exitAssessmentDialogHandler(final int navigationRequest, 
											   final AbstractModel model, FragmentStatePagerAdapter fragmentStatePagerAdapter,
											   int currentFragmentPosition) {
		DialogFragment dg = new DialogFragment() {
    		@Override
    		public Dialog onCreateDialog(Bundle savedInstanceState) {    			
    			return new AlertDialog.Builder(getActivity())
    			.setMessage(R.string.exit_assessment_confirm_message)
    			.setPositiveButton(R.string.exit_assessment_confirm_button, new AssessmentExitDialogListener(SupportingLifeBaseActivity.this, navigationRequest, model))
    			.setNegativeButton(android.R.string.cancel, null)
    			.create();
    		}
    	};
    	
		// before gathering analytic data, call the 'on pause' operation on the current fragment to make
        // sure the stop and duration timers for this page are accounted for
		FragmentLifecycle fragmentToHide = (FragmentLifecycle) fragmentStatePagerAdapter.getItem(currentFragmentPosition);
		fragmentToHide.onPauseFragment(model);
    	
    	dg.show(getSupportFragmentManager(), EXIT_ASSESSMENT_DIALOG_TAG);
	}
}