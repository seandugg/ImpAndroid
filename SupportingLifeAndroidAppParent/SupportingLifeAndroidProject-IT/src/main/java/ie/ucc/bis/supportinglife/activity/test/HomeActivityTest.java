package ie.ucc.bis.supportinglife.activity.test;

import ie.ucc.bis.supportinglife.activity.HomeActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.google.analytics.tracking.android.GoogleAnalytics;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public HomeActivityTest() {
        super(HomeActivity.class);
        
        // turn off google analytics for duration of test
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getActivity());
        googleAnalytics.setAppOptOut(true);
    }

    public void testActivity() {
    	assertNotNull("activity should be launched successfully", getActivity());
    }
}

