package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
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
	private int navigationRequest;

	/**
	 * Constructor
	 */
	public AssessmentExitDialogListener(SupportingLifeBaseActivity supportingLifeBaseActivity, int navigationRequest) {
		this.supportingLifeBaseActivity = supportingLifeBaseActivity;
		this.navigationRequest = navigationRequest;
	}

	public void onClick(DialogInterface dialog, int which) {
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