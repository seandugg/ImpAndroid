package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.analytics.AnalyticUtilities;
import ie.ucc.bis.supportinglife.assessment.model.AbstractModel;
import android.content.DialogInterface;

/**
 * Class: AssessmentExitDialogListener
 * 
 * Provides OnClick handler functionality for Home Button
 * and Back Button click event on an assessment activity
 * 
 * 		i.e. ImciAssessmentActivity / 
 * 			 CcmAssessmentActivity /
 * 			 ImciAssessmentResultsActivity /
 * 			 CcmAssessmentResultsActivity
 * 
 * @author TOSullivan
 *
 */
public final class AssessmentExitDialogListener implements DialogInterface.OnClickListener {
	
	public static final int DASHBOARD_SCREEN = 1;
	public static final int SETTINGS_SCREEN = 2;
	public static final int SYNC_SCREEN = 3;
	public static final int HELP_SCREEN = 4;

	private SupportingLifeBaseActivity supportingLifeBaseActivity;
	private AbstractModel model;
	private int navigationRequest;

	/**
	 * Constructor
	 * 
	 * @param supportingLifeBaseActivity
	 * @param navigationRequest
	 * @param model 
	 */
	public AssessmentExitDialogListener(SupportingLifeBaseActivity supportingLifeBaseActivity, int navigationRequest, AbstractModel model) {
		this.supportingLifeBaseActivity = supportingLifeBaseActivity;
		this.navigationRequest = navigationRequest;
		this.model = model;
	}

	public void onClick(DialogInterface dialog, int which) {
		
		// record any data analytic events logged with any individual page views
        AnalyticUtilities.recordDataAnalytics(supportingLifeBaseActivity.getApplicationContext(), model);
		
		switch (navigationRequest) {
			case DASHBOARD_SCREEN : 	supportingLifeBaseActivity.goHome(supportingLifeBaseActivity);
										break;
			case SETTINGS_SCREEN : 		supportingLifeBaseActivity.goToSettingsScreen();
								   		break;
			case SYNC_SCREEN : 			supportingLifeBaseActivity.goToSyncScreen();
										break;
			case HELP_SCREEN : 			supportingLifeBaseActivity.goToHelpScreen();
										break;
		}
	}
	
} // end of class