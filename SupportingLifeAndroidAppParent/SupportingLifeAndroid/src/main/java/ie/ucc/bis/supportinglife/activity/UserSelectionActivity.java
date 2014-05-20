package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity responsible for facilitating user selection on initial launch
 * of the application. User can chose either to be:
 * 
 * 1. Guest User
 * 2. HSA User
 *
 * @author timothyosullivan
 */

public class UserSelectionActivity extends SupportingLifeBaseActivity {
	
	private Button guestUserButton;
	private Button hsaUserButton;
	
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
		setContentView(R.layout.activity_user_selection);
		
		// load the font-awesome font for the buttons
		Typeface font = Typeface.createFromAsset(getAssets(), FONT_AWESOME_TYPEFACE_ASSET);
		
		setGuestUserButton((Button) findViewById(R.id.user_selection_guest_user_button));
		getGuestUserButton().setTypeface(font);
		
		setHsaUserButton((Button) findViewById(R.id.user_selection_hsa_user_button));
		getHsaUserButton().setTypeface(font);
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
	 * Click Handler: Handler the click of a user type button
	 * 
	 * @param view View
	 * @return void
	 */
	public void onClickUserTypeButton(View view) {
		int id = view.getId();
		switch(id) {
	//		case R.id.dashboard_about_button :
	//			startActivity(new Intent(getApplicationContext(), AboutActivity.class));
	//			break;			
			default : 
				break;
		} // end of switch
		
		// TEMP - START
		startActivity(new Intent(getApplicationContext(), HomeActivity.class));
		// TEMP - END
		
		// configure the activity animation transition effect
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public Button getGuestUserButton() {
		return guestUserButton;
	}

	public void setGuestUserButton(Button guestUserButton) {
		this.guestUserButton = guestUserButton;
	}

	public Button getHsaUserButton() {
		return hsaUserButton;
	}

	public void setHsaUserButton(Button hsaUserButton) {
		this.hsaUserButton = hsaUserButton;
	}	
} // end class
