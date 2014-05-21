package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Activity responsible for registering HSA user on initial launch
 * of the application.
 *
 * @author timothyosullivan
 */

public class UserRegistrationActivity extends SupportingLifeBaseActivity {

	/**
	 * OnCreate method is called when the activity is first created.
	 * 
	 * This is where all of the normal static set up should occur
	 * e.g. create views, bind data to lists, etc.
	 * 
	 * The method also provides a Bundle parameter containing the activity's
	 * 
	 * previously frozen state (if there was one).
	 * 
	 * This method is always followed by onStart().
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration);
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
	 * Determine if this activity should display an ActionBar when it is
	 * shown.
	 * 
	 * @return boolean
	 */
	@Override
	protected boolean shouldDisplayActionBar() {
		return false;
	}
	
	/**
	 * Click Handler: Handler the click of a user registration button
	 * 
	 * @param view View
	 * @return void
	 */
	public void onClickUserRegistrationButton(View view) {
		int id = view.getId();
		
		// TEMP START
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor preferenceEditor = settings.edit();
		// TEMP END
		
		switch(id) {
			case R.id.user_registration_submit_button:
				startActivity(new Intent(getApplicationContext(), HomeActivity.class));	
				// configure the activity animation transition effect
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);	
				break;
			case R.id.user_registration_clear_button:
				break;	
			default : 
				break;
		} // end of switch
	
	}
} // end class
