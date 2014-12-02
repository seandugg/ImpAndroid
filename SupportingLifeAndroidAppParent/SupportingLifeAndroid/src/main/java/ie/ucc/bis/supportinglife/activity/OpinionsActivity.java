package ie.ucc.bis.supportinglife.activity;

import ie.ucc.bis.supportinglife.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Supporting LIFE Opinions Interface.
 *
 * @author timothyosullivan
 */

public class OpinionsActivity extends SupportingLifeBaseActivity {
	
	protected static final String OPINIONS_ICON_TYPEFACE_ASSET = "fonts/opinions-flaticon.ttf";
	protected static final String APP_UNINSTALLED = "App is not installed";
	
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
		setContentView(R.layout.activity_opinions);
		
		configureButtonFontIcons();
	}
	
	/**
	 * Responsible for configuring the font icons (provided by FlatIcon)
	 * on the opinions buttons
	 * 
	 */
	private void configureButtonFontIcons() {
		// load the flaticon font for the buttons
		Typeface font = Typeface.createFromAsset(getAssets(), OPINIONS_ICON_TYPEFACE_ASSET);
		
		((Button) findViewById(R.id.opinions_twitter_button)).setTypeface(font);
		((Button) findViewById(R.id.opinions_facebook_button)).setTypeface(font);
		((Button) findViewById(R.id.opinions_googleplus_button)).setTypeface(font);
	}
	
	/**
	 * Determine if this activity should display an ActionBar when it is
	 * shown.
	 * 
	 * @return boolean
	 */
	@Override
	protected boolean shouldDisplayActionBar() {
		return true;
	}
	
	/**
	 * Click Handler: Handler the click of a social media button
	 * 
	 * @param view View
	 * @return void
	 */
	public void onClickOpinionsButton(View view) {
		int id = view.getId();
		switch(id) {
			case R.id.opinions_twitter_button :
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/")));
				break;
			case R.id.opinions_facebook_button :
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/")));
				break;
			case R.id.opinions_googleplus_button :
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/")));
				break;
			default : 
				break;
		} // end of switch
	}

} // end class
