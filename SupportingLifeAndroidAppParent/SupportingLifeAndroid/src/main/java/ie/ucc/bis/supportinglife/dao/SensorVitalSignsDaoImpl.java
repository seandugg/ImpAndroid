package ie.ucc.bis.supportinglife.dao;

import ie.ucc.bis.supportinglife.communication.PatientAssessmentComms;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.service.SupportingLifeService;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.util.List;

import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;

public class SensorVitalSignsDaoImpl implements SensorVitalSignsDao {
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.dao.SensorVitalSignsDaoImpl";
	
	private String[] allColumns = { SensorVitalSignsTable.COLUMN_ID,
									SensorVitalSignsTable.COLUMN_ASSESSMENT_ID,
									SensorVitalSignsTable.COLUMN_HEART_RATE,
									SensorVitalSignsTable.COLUMN_RESPIRATORY_RATE,
									SensorVitalSignsTable.COLUMN_BODY_TEMPERATURE};

	public SensorVitalSignsDaoImpl() {}
	
	/**
	 * Responsible for adding 'sensor vital signs' details pertaining to an assessment 
	 * to the database on the android device
	 * 
	 * @param assessmentToAdd
	 * @param service - SupportingLifeService
	 * 
	 * @return
	 */
	@Override
	public void createSensorVitalSigns(PatientAssessment assessmentToAdd, String uniquePatientAssessmentIdentifier, Location locat, SupportingLifeService service) {
		// show the number of assessment specific 'sensor vital sign readings; in debug logger
		debugOutputShowSensorReadingsCount(service);

		ContentValues values = new ContentValues();
		values.put(SensorVitalSignsTable.COLUMN_ASSESSMENT_ID, uniquePatientAssessmentIdentifier);
		values.put(SensorVitalSignsTable.COLUMN_HEART_RATE, String.valueOf(assessmentToAdd.getSensorHeartRate()));
		values.put(SensorVitalSignsTable.COLUMN_RESPIRATORY_RATE, String.valueOf(assessmentToAdd.getSensorRespiratoryRate()));
		values.put(SensorVitalSignsTable.COLUMN_BODY_TEMPERATURE, String.valueOf(assessmentToAdd.getSensorBodyTemperature()));
					
		// add the 'sensor vital sign readings' row
		long insertId = service.getDatabase().insert(SensorVitalSignsTable.TABLE_SENSOR_VITAL_SIGNS, null, values);
		
		LoggerUtils.i(LOG_TAG, "Sensor Vital Signs Record Added: " + insertId);
				
		// show the number of assessment specific 'sensor vital sign readings; in debug logger
		debugOutputShowSensorReadingsCount(service);
	}
	
	/**
	 * Responsible for populating the 'patient assessments' with
	 * their 'sensor vital signs' details
	 * 
	 * @param patientAssessments - List<PatientAssessment>
	 * @param service - SupportingLifeService
	 * 
	 * @return
	 */
	@Override
	public void populateSensorVitalSigns(List<PatientAssessmentComms> patientAssessmentComms, SupportingLifeService service) {
		Cursor cursor = null;
		
		// iterate over all of the patient assessments that we have to retrieve
		// for which we have to retrieve analytics
		for (PatientAssessmentComms patientAssessmentComm : patientAssessmentComms) {
			cursor = service.getDatabase().query(SensorVitalSignsTable.TABLE_SENSOR_VITAL_SIGNS,
					allColumns, SensorVitalSignsTable.COLUMN_ASSESSMENT_ID + " = '" + patientAssessmentComm.getDeviceGeneratedAssessmentId() + "'", 
					null, null, null, null);
	
			cursor.moveToFirst();
			
			// only one 'sensor vital sign readings' record returned - one-to-one relationship assessment and sensor readings
			patientAssessmentComm.setSensorHeartRate(cursor.getString(2));
			patientAssessmentComm.setSensorRespiratoryRate(cursor.getString(3));
			patientAssessmentComm.setSensorBodyTemperature(cursor.getString(4));
		}
		
		if (cursor != null) {
			cursor.close();
		}
	}

		
	/**
	 * Show the number of assessment specific 'sensor vital sign readings in debug logger
	 * 
	 * @param service
	 */
	private void debugOutputShowSensorReadingsCount(SupportingLifeService service) {
		SQLiteStatement sensorReadingRowCountQuery = service.getDatabase().compileStatement("select count(*) from " + SensorVitalSignsTable.TABLE_SENSOR_VITAL_SIGNS);
		if (sensorReadingRowCountQuery != null) {
			long sensorReadingRowCount = sensorReadingRowCountQuery.simpleQueryForLong();
			LoggerUtils.i(LOG_TAG, "Current Sensor Reading Row Count: " + sensorReadingRowCount);
			sensorReadingRowCountQuery.close();
		}
	}
} 
