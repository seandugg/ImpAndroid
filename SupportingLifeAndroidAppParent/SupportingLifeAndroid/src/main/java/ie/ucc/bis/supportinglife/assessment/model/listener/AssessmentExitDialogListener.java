package ie.ucc.bis.supportinglife.assessment.model.listener;

import ie.ucc.bis.supportinglife.activity.AssessmentResultsActivity;
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
		setSupportingLifeBaseActivity(supportingLifeBaseActivity);
		setNavigationRequest(navigationRequest);
		setModel(model);
	}

	public void onClick(DialogInterface dialog, int which) {
		
		// record treatments administered in DB
		if (getSupportingLifeBaseActivity() instanceof AssessmentResultsActivity && !getSupportingLifeBaseActivity().isGuestUser()) {
			((AssessmentResultsActivity) getSupportingLifeBaseActivity()).storePatientTreatmentsAdministered();
		}
		
		// record any data analytic events logged with any individual page views
        AnalyticUtilities.recordDataAnalytics(supportingLifeBaseActivity.getApplicationContext(), model);
        boolean clearActivityStack = true;
		
		switch (navigationRequest) {
			case DASHBOARD_SCREEN : 	getSupportingLifeBaseActivity().goHome(getSupportingLifeBaseActivity(), clearActivityStack);
										break;
			case SETTINGS_SCREEN : 		getSupportingLifeBaseActivity().goToSettingsScreen(clearActivityStack);
								   		break;
			case SYNC_SCREEN : 			getSupportingLifeBaseActivity().goToSyncScreen(clearActivityStack);
										break;
			case HELP_SCREEN : 			getSupportingLifeBaseActivity().goToHelpScreen(clearActivityStack);
										break;
		}
	}

	private SupportingLifeBaseActivity getSupportingLifeBaseActivity() {
		return supportingLifeBaseActivity;
	}

	private void setSupportingLifeBaseActivity(
			SupportingLifeBaseActivity supportingLifeBaseActivity) {
		this.supportingLifeBaseActivity = supportingLifeBaseActivity;
	}

	private void setModel(AbstractModel model) {
		this.model = model;
	}

	private void setNavigationRequest(int navigationRequest) {
		this.navigationRequest = navigationRequest;
	}
	
} // end of class