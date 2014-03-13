package ie.ucc.bis.supportinglife.activity.test;

import ie.ucc.bis.supportinglife.activity.HomeActivity;
import android.test.ActivityInstrumentationTestCase2;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public HomeActivityTest() {
        super(HomeActivity.class); 
    }

    public void testActivity() {
    	assertNotNull("activity should be launched successfully", getActivity());
    }
}

