package ie.ucc.bis.supportinglife.service;

import ie.ucc.bis.supportinglife.activity.SupportingLifeBaseActivity;
import ie.ucc.bis.supportinglife.communication.PatientAssessmentComms;
import ie.ucc.bis.supportinglife.dao.AssessmentAnalyticsDao;
import ie.ucc.bis.supportinglife.dao.AssessmentAnalyticsDaoImpl;
import ie.ucc.bis.supportinglife.dao.ClassificationDao;
import ie.ucc.bis.supportinglife.dao.ClassificationDaoImpl;
import ie.ucc.bis.supportinglife.dao.CustomSharedPreferences;
import ie.ucc.bis.supportinglife.dao.DatabaseHandler;
import ie.ucc.bis.supportinglife.dao.PatientAssessmentDao;
import ie.ucc.bis.supportinglife.dao.PatientAssessmentDaoImpl;
import ie.ucc.bis.supportinglife.dao.SensorVitalSignsDao;
import ie.ucc.bis.supportinglife.dao.SensorVitalSignsDaoImpl;
import ie.ucc.bis.supportinglife.dao.TreatmentDao;
import ie.ucc.bis.supportinglife.dao.TreatmentDaoImpl;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;
import android.database.SQLException;
import android.location.Location;

/**
 * Service Interface layer for coordinating
 * DAO access
 * 
 * @author TOSullivan
 *
 */
public class SupportingLifeService implements SupportingLifeServiceInf {
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.service.SupportingLifeService";

	// DAOs
	private PatientAssessmentDao patientAssessmentDao;
	private ClassificationDao classificationDao;
	private TreatmentDao treatmentDao;
	private AssessmentAnalyticsDao assessmentAnalyticsDao;
	private SensorVitalSignsDao sensorVitalSignsDao;
	
	// Database fields
	private SQLiteDatabase database;
	private DatabaseHandler databaseHandler;
	
	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public SupportingLifeService(Context context) {	
		// the call to loadlibs for DB must occur before any other database operation
		SQLiteDatabase.loadLibs(context);
		
		setDatabaseHandler(new DatabaseHandler(context));
		setPatientAssessmentDao(new PatientAssessmentDaoImpl());
		setClassificationDao(new ClassificationDaoImpl());
		setTreatmentDao(new TreatmentDaoImpl());
		setAssessmentAnalyticsDao(new AssessmentAnalyticsDaoImpl());
		setSensorVitalSignsDao(new SensorVitalSignsDaoImpl());
	}
	
	/*******************************************************************************/
	/*******************************PATIENT ASSESSMENTS*****************************/
	/*******************************************************************************/
	@Override
	public void createPatientAssessment(PatientAssessment patientToAdd, String android_device_id, String hsaUserId, Location locat) {
		
		// updating multiple tables so wrap in a transaction
		getDatabase().beginTransaction();
		
		try {
			// the combination of the the 'unique android id'  and the current time in ms will
			// allow the device to create a unique 'patient assessment identifier'
			long timestamp = System.currentTimeMillis();
			String uniquePatientAssessmentIdentifier = android_device_id + "_" + timestamp;
			
			// add the patient assessment
			getPatientAssessmentDao().createPatientAssessmentComms(patientToAdd, uniquePatientAssessmentIdentifier, hsaUserId, this);
			
			// now add the associated 'patient assessment' classifications
			getClassificationDao().createPatientClassifications(patientToAdd, uniquePatientAssessmentIdentifier, this);
			
			// now add the associated 'patient assessment' treatments
			getTreatmentDao().createPatientTreatments(patientToAdd, uniquePatientAssessmentIdentifier, this);
			
			// now add the associated 'assessment' sensor readings
			getSensorVitalSignsDao().createSensorVitalSigns(patientToAdd, uniquePatientAssessmentIdentifier, locat, this);

			// now add the associated 'assessment' analytics
			getAssessmentAnalyticsDao().createAssessmentAnalytics(patientToAdd, uniquePatientAssessmentIdentifier, locat, this);
			
			// commit the transaction
			getDatabase().setTransactionSuccessful();
		} catch (Exception ex) {
			LoggerUtils.i(LOG_TAG, "SupportingLifeService: createPatientAssessment -- Exception");
			LoggerUtils.i(LOG_TAG, "SupportingLifeService: createPatientAssessment -- " + ex.getMessage());
		}
		finally {
			// end the database transaction
			getDatabase().endTransaction();
		}		
	}

	@Override
	public void deletePatientAssessment(PatientAssessment patient) {
		getPatientAssessmentDao().deletePatientAssessment(patient, this);
	}

	@Override
	public List<PatientAssessmentComms> getAllNonSyncedPatientAssessmentComms() {
		List<PatientAssessmentComms> patientAssessmentComms = new ArrayList<PatientAssessmentComms>();
		
		// 1. pull back all non-synced records from the 'patient assessment' table
		patientAssessmentComms = getPatientAssessmentDao().getAllNonSyncedPatientAssessmentComms(this);
		
		// only need to pull back classifications and treatments if there are 
		// patients assessments requiring syncing
		if (patientAssessmentComms.size() > 0) {		
			// 2. For the patient assessments pulled back, pull back the associated classifications
			getClassificationDao().populatePatientClassifications(patientAssessmentComms, this);
			
			// 3. For the patient assessments pulled back, pull back the associated classifications
			getTreatmentDao().populatePatientTreatments(patientAssessmentComms, this);
			
			// 4. For the patient assessments pulled back, pull back the associated analytics
			getAssessmentAnalyticsDao().populateAssessmentAnalytics(patientAssessmentComms, this);
			
			// 5. For the patient assessments pulled back, pull back the associated vital sign sensor readings
			getSensorVitalSignsDao().populateSensorVitalSigns(patientAssessmentComms, this);
		}
		
		return patientAssessmentComms;
	}

	@Override
	public List<PatientAssessmentComms> getAllPatientAssessmentComms() {
		return getPatientAssessmentDao().getAllPatientAssessments(this);
	}

	public int setPatientAssessmentToSynced(String deviceGeneratedAssessmentId) {
		return getPatientAssessmentDao().setPatientAssessmentToSynced(deviceGeneratedAssessmentId, this);	
	}
	
	/*******************************************************************************/
	/***********************GENERAL DATABASE MANAGEMENT*****************************/
	/*******************************************************************************/
	@Override
	public void open(SupportingLifeBaseActivity slActivity) throws SQLException {
		CustomSharedPreferences preferences = CustomSharedPreferences.getPrefs(slActivity, SupportingLifeBaseActivity.APP_NAME, Context.MODE_PRIVATE);
		String dbKey = preferences.getString(SupportingLifeBaseActivity.USER_KEY, "");
		setDatabase(databaseHandler.getWritableDatabase(dbKey));
	}

	@Override
	public void close() {
		getDatabaseHandler().close();
	}
	
	private PatientAssessmentDao getPatientAssessmentDao() {
		return patientAssessmentDao;
	}

	private void setPatientAssessmentDao(PatientAssessmentDao patientAssessmentDao) {
		this.patientAssessmentDao = patientAssessmentDao;
	}

	private ClassificationDao getClassificationDao() {
		return classificationDao;
	}

	private void setClassificationDao(ClassificationDao classificationDao) {
		this.classificationDao = classificationDao;
	}

	public TreatmentDao getTreatmentDao() {
		return treatmentDao;
	}

	private void setTreatmentDao(TreatmentDao treatmentDao) {
		this.treatmentDao = treatmentDao;
	}

	private AssessmentAnalyticsDao getAssessmentAnalyticsDao() {
		return assessmentAnalyticsDao;
	}

	private void setAssessmentAnalyticsDao(AssessmentAnalyticsDao assessmentAnalyticsDao) {
		this.assessmentAnalyticsDao = assessmentAnalyticsDao;
	}

	private SensorVitalSignsDao getSensorVitalSignsDao() {
		return sensorVitalSignsDao;
	}

	private void setSensorVitalSignsDao(SensorVitalSignsDao sensorVitalSignsDao) {
		this.sensorVitalSignsDao = sensorVitalSignsDao;
	}

	@Override
	public SQLiteDatabase getDatabase() {
		return database;
	}

	@Override
	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public DatabaseHandler getDatabaseHandler() {
		return databaseHandler;
	}
	
	@Override
	public void setDatabaseHandler(DatabaseHandler databaseHandler) {
		this.databaseHandler = databaseHandler;
	}
}
