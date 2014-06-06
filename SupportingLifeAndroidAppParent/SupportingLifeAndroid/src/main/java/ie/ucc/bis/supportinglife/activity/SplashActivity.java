package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.dao.CustomSharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * 
 * This is the Splash activity in the Supporting LIFE application.
 * 
 * The purpose of the activity is to display a splash screen when
 * launching the application.
 * 
 * @author timothyosullivan
 *
 */
public class SplashActivity extends SupportingLifeBaseActivity {

	private static final long SPLASH_DELAY = 1000; // 1 second
	private Thread splashThread;

	/**
	 * onCreate method
	 * 
	 * Called when the activity is first created.
	 * Method is always followed by onStart() method.
	 * 
	 * @param savedInstanceState Bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_splash);

		// thread for displaying the SplashScreen
		CustomSharedPreferences preferences = CustomSharedPreferences.getPrefs(this, APP_NAME, Context.MODE_PRIVATE);
		splashThread = new Thread(new SplashScreenRunnable(preferences));
		splashThread.start();
	} // end of onCreate(..) method

	/**
	 * onTouchEvent method
	 * 
	 * Allow a user to bypass the touch screen via a touch event
	 * 
	 * @param event MotionEvent
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized(splashThread){
				splashThread.notifyAll();
			}
		} // end of if
		return true;
	}

	/**
	 * Thread responsible for coordinating the display of the dashboard screen
	 * and for parsing the rules engine (IMCI and CCM) xml files into memory.
	 * 
	 */
	private class SplashScreenRunnable implements Runnable {
		
		private CustomSharedPreferences customSharedPreferences;
		
		public SplashScreenRunnable(CustomSharedPreferences customSharedPreferences) {
			this.customSharedPreferences = customSharedPreferences;	
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				synchronized(this) {
					wait(SPLASH_DELAY);
				} // end of sync
			} catch (InterruptedException interruptExp) {}
			finally {
				// need to determine if a HSA user has been registered
				boolean hsaUserRegistered = isHsaUserRegistered();

		        if (!hsaUserRegistered) {
		        	startActivity(new Intent(getApplicationContext(), UserSelectionActivity.class));
		        }
		        else {
		        	startActivity(new Intent(getApplicationContext(), HomeActivity.class));
		        }
				// configure the activity animation transition effect
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				
				// call finish on SplashActivity to prevent user from using
				// back button to navigate back to Splash screen
				finish();
			} // end of finally
		} // end of run() method

		/**
		 * Responsible for determining if a HSA user has been registered
		 * 
		 * @return boolean - true (HSA user registered)
		 * 				   - false (HSA user not registered)
		 */
		private boolean isHsaUserRegistered() {
			boolean registered = false;

	        String userType = this.customSharedPreferences.getString(USER_TYPE_KEY, "");
	        
	        // TODO need to enhance to check that a username and HSA user type is present
	        // in the shared preferences
	        if (userType.equalsIgnoreCase("hsa_user")) {
	        	registered = true;
	        }
	        
			return registered;
		}
	} // end of SplashScreenRunnable class

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
} // end of class
