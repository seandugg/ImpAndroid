package ie.ucc.bis.supportinglife.dao;

import ie.ucc.bis.supportinglife.communication.PatientAssessmentComms;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.service.SupportingLifeService;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.util.List;

import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;

public class AssessmentAnalyticsDaoImpl implements AssessmentAnalyticsDao {
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.dao.AssessmentAnalyticsDaoImpl";
	
	private String[] allColumns = { AssessmentAnalyticsTable.COLUMN_ID,
									AssessmentAnalyticsTable.COLUMN_ASSESSMENT_ID,
									AssessmentAnalyticsTable.COLUMN_BREATH_COUNTER_USED_ID,
									AssessmentAnalyticsTable.COLUMN_BREATH_FULL_TIME_ASSESSMENT_ID};

	public AssessmentAnalyticsDaoImpl() {}
	
	/**
	 * Responsible for adding 'assessment analytics' record to the database on the 
	 * android device
	 * 
	 * @param assessmentToAdd
	 * @param service - SupportingLifeService
	 * 
	 * @return
	 */
	@Override
	public void createAssessmentAnalytics(PatientAssessment assessmentToAdd, String uniquePatientAssessmentIdentifier, SupportingLifeService service) {
		// show the number of analytic assessments in debug logger
		debugOutputShowAnalyticsCount(service);

		ContentValues values = new ContentValues();
		values.put(AssessmentAnalyticsTable.COLUMN_ASSESSMENT_ID, uniquePatientAssessmentIdentifier);
		values.put(AssessmentAnalyticsTable.COLUMN_BREATH_COUNTER_USED_ID, String.valueOf(assessmentToAdd.isBreathCounterUsed()));
		values.put(AssessmentAnalyticsTable.COLUMN_BREATH_FULL_TIME_ASSESSMENT_ID, String.valueOf(assessmentToAdd.isBreathFullTimeAssessment()));	
			
		// add the assessment analytics row
		long insertId = service.getDatabase().insert(AssessmentAnalyticsTable.TABLE_ASSESSMENT_ANALYTICS, null, values);
		
		LoggerUtils.i(LOG_TAG, "Assessment Analytics Record Added: " + insertId);
				
		// show the number of analytic assessments in debug logger
		debugOutputShowAnalyticsCount(service);
	}
	
	/**
	 * Responsible for populating the 'patient assessments' received with
	 * their associated analytics from the 'assessment analytics' table
	 * 
	 * @param patientAssessments - List<PatientAssessment>
	 * @param service - SupportingLifeService
	 * 
	 * @return
	 */
	@Override
	public void populateAssessmentAnalytics(List<PatientAssessmentComms> patientAssessmentComms, SupportingLifeService service) {
		Cursor cursor = null;
		
		// iterate over all of the patient assessments that we have to retrieve
		// for which we have to retrieve analytics
		for (PatientAssessmentComms patientAssessmentComm : patientAssessmentComms) {
			cursor = service.getDatabase().query(AssessmentAnalyticsTable.TABLE_ASSESSMENT_ANALYTICS,
					allColumns, AssessmentAnalyticsTable.COLUMN_ASSESSMENT_ID + " = '" + patientAssessmentComm.getDeviceGeneratedAssessmentId() + "'", 
					null, null, null, null);
	
			cursor.moveToFirst();
			
			// only one analytics record returned - one-to-one relationship assessment and analytics
			patientAssessmentComm.setBreathCounterUsed(Boolean.valueOf(cursor.getString(2)));
			patientAssessmentComm.setBreathFullTimeAssessment(Boolean.valueOf(cursor.getString(3)));
		}
		
		if (cursor != null) {
			cursor.close();
		}
	}

		
	/**
	 * Show the number of assessment analytics in debug logger
	 * 
	 * @param service
	 */
	private void debugOutputShowAnalyticsCount(SupportingLifeService service) {
		SQLiteStatement assessmentRowCountQuery = service.getDatabase().compileStatement("select count(*) from " + AssessmentAnalyticsTable.TABLE_ASSESSMENT_ANALYTICS);
		if (assessmentRowCountQuery != null) {
			long assessmentRowCount = assessmentRowCountQuery.simpleQueryForLong();
			LoggerUtils.i(LOG_TAG, "Current Assessment Row Count: " + assessmentRowCount);
			assessmentRowCountQuery.close();
		}
	}
} 
