package ie.ucc.bis.supportinglife.dao;

import ie.ucc.bis.supportinglife.communication.PatientAssessmentComms;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.service.SupportingLifeService;
import ie.ucc.bis.supportinglife.ui.utilities.DateUtilities;
import ie.ucc.bis.supportinglife.ui.utilities.LoggerUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Class: PatientAssessmentDao
 * 
 * This class maintains the database connection and supports 
 * adding new patient assessments and fetching all patient
 * assessments.
 * 
 * @author TOSullivan
 */
public class PatientAssessmentDaoImpl implements PatientAssessmentDao {
	
	private final String LOG_TAG = "ie.ucc.bis.supportinglife.dao.PatientAssessmentDaoImpl";

	private String[] allColumns = { PatientAssessmentTable.COLUMN_ID,
									PatientAssessmentTable.COLUMN_ASSESSMENT_ID,
									PatientAssessmentTable.COLUMN_NATIONAL_ID,
									PatientAssessmentTable.COLUMN_NATIONAL_HEALTH_ID,
									PatientAssessmentTable.COLUMN_HSA_USER_ID,
									PatientAssessmentTable.COLUMN_CHILD_FIRST_NAME,
									PatientAssessmentTable.COLUMN_CHILD_SURNAME,
									PatientAssessmentTable.COLUMN_BIRTH_DATE,
									PatientAssessmentTable.COLUMN_AGE_MONTHS,	
									PatientAssessmentTable.COLUMN_GENDER,
									PatientAssessmentTable.COLUMN_CAREGIVER_NAME,
									PatientAssessmentTable.COLUMN_RELATIONSHIP,
									PatientAssessmentTable.COLUMN_PHYSICAL_ADDRESS,
									PatientAssessmentTable.COLUMN_VILLAGE,
									PatientAssessmentTable.COLUMN_TA,
									PatientAssessmentTable.COLUMN_VISIT_DATE,
									PatientAssessmentTable.COLUMN_CHEST_INDRAWING,
									PatientAssessmentTable.COLUMN_BREATHS_PER_MINUTE,
									PatientAssessmentTable.COLUMN_SLEEPY_UNCONSCIOUS,
									PatientAssessmentTable.COLUMN_PALMAR_PALLOR,
									PatientAssessmentTable.COLUMN_MUAC_TAPE_COLOUR,
									PatientAssessmentTable.COLUMN_SWELLING_BOTH_FEET,
									PatientAssessmentTable.COLUMN_PROBLEM,
									PatientAssessmentTable.COLUMN_COUGH,
									PatientAssessmentTable.COLUMN_COUGH_DURATION,
									PatientAssessmentTable.COLUMN_DIARRHOEA,
									PatientAssessmentTable.COLUMN_DIARRHOEA_DURATION,
									PatientAssessmentTable.COLUMN_BLOOD_IN_STOOL,
									PatientAssessmentTable.COLUMN_FEVER,
									PatientAssessmentTable.COLUMN_FEVER_DURATION,
									PatientAssessmentTable.COLUMN_CONVULSIONS,
									PatientAssessmentTable.COLUMN_DIFFICULTY_DRINKING,
									PatientAssessmentTable.COLUMN_UNABLE_TO_DRINK,
									PatientAssessmentTable.COLUMN_VOMITING,
									PatientAssessmentTable.COLUMN_VOMITS_EVERYTHING,
									PatientAssessmentTable.COLUMN_RED_EYE,
									PatientAssessmentTable.COLUMN_RED_EYE_DURATION,
									PatientAssessmentTable.COLUMN_DIFFICULTY_SEEING,
									PatientAssessmentTable.COLUMN_DIFFICULTY_SEEING_DURATION,
									PatientAssessmentTable.COLUMN_CANNOT_TREAT,
									PatientAssessmentTable.COLUMN_CANNOT_TREAT_DETAILS,
									PatientAssessmentTable.COLUMN_SYNCED};

	public PatientAssessmentDaoImpl() {
	}

	/**
	 * Responsible for adding 'PatientAssessment' record to the database on the 
	 * android device
	 * 
	 * @param patientToAdd
	 * @param hsaUserId
	 * @param service
	 * 
	 * @return
	 */
	@Override
	public PatientAssessmentComms createPatientAssessmentComms(PatientAssessment patientToAdd, String hsaUserId, SupportingLifeService service) {		

		// show the number of patient assessments in debug logger
		debugOutputShowPatientAssessmentCount(service);

		ContentValues values = new ContentValues();
		values.put(PatientAssessmentTable.COLUMN_ASSESSMENT_ID, patientToAdd.getDeviceGeneratedAssessmentId());
		values.put(PatientAssessmentTable.COLUMN_NATIONAL_ID, patientToAdd.getNationalId());
		values.put(PatientAssessmentTable.COLUMN_NATIONAL_HEALTH_ID, patientToAdd.getNationalHealthId());	
		values.put(PatientAssessmentTable.COLUMN_HSA_USER_ID, hsaUserId);		
		values.put(PatientAssessmentTable.COLUMN_CHILD_FIRST_NAME, patientToAdd.getChildFirstName());					
		values.put(PatientAssessmentTable.COLUMN_CHILD_SURNAME, patientToAdd.getChildSurname());
		values.put(PatientAssessmentTable.COLUMN_AGE_MONTHS, patientToAdd.getMonthsAge());
		values.put(PatientAssessmentTable.COLUMN_GENDER, patientToAdd.getGender());
		values.put(PatientAssessmentTable.COLUMN_CAREGIVER_NAME, patientToAdd.getCaregiverName());
		values.put(PatientAssessmentTable.COLUMN_RELATIONSHIP, patientToAdd.getRelationship());
		values.put(PatientAssessmentTable.COLUMN_PHYSICAL_ADDRESS, patientToAdd.getPhysicalAddress());	
		values.put(PatientAssessmentTable.COLUMN_VILLAGE, patientToAdd.getVillage());
		values.put(PatientAssessmentTable.COLUMN_TA, patientToAdd.getTa());	
		values.put(PatientAssessmentTable.COLUMN_CHEST_INDRAWING, String.valueOf(patientToAdd.isChestIndrawing()));
		values.put(PatientAssessmentTable.COLUMN_BREATHS_PER_MINUTE, patientToAdd.getBreathsPerMinute());
		values.put(PatientAssessmentTable.COLUMN_SLEEPY_UNCONSCIOUS, String.valueOf(patientToAdd.isSleepyUnconscious()));
		values.put(PatientAssessmentTable.COLUMN_PALMAR_PALLOR, String.valueOf(patientToAdd.isPalmarPallor()));
		values.put(PatientAssessmentTable.COLUMN_MUAC_TAPE_COLOUR, patientToAdd.getMuacTapeColour());
		values.put(PatientAssessmentTable.COLUMN_SWELLING_BOTH_FEET, String.valueOf(patientToAdd.isSwellingBothFeet()));
		values.put(PatientAssessmentTable.COLUMN_PROBLEM, patientToAdd.getProblem());
		values.put(PatientAssessmentTable.COLUMN_COUGH, String.valueOf(patientToAdd.isCough()));
		values.put(PatientAssessmentTable.COLUMN_COUGH_DURATION, patientToAdd.getCoughDuration());
		values.put(PatientAssessmentTable.COLUMN_DIARRHOEA, String.valueOf(patientToAdd.isDiarrhoea()));
		values.put(PatientAssessmentTable.COLUMN_DIARRHOEA_DURATION, patientToAdd.getDiarrhoeaDuration());
		values.put(PatientAssessmentTable.COLUMN_BLOOD_IN_STOOL, String.valueOf(patientToAdd.isBloodInStool()));
		values.put(PatientAssessmentTable.COLUMN_FEVER, String.valueOf(patientToAdd.isFever()));
		values.put(PatientAssessmentTable.COLUMN_FEVER_DURATION, patientToAdd.getFeverDuration());
		values.put(PatientAssessmentTable.COLUMN_CONVULSIONS, String.valueOf(patientToAdd.isConvulsions()));
		values.put(PatientAssessmentTable.COLUMN_DIFFICULTY_DRINKING, String.valueOf(patientToAdd.isDifficultyDrinkingOrFeeding()));
		values.put(PatientAssessmentTable.COLUMN_UNABLE_TO_DRINK, String.valueOf(patientToAdd.isUnableToDrinkOrFeed()));
		values.put(PatientAssessmentTable.COLUMN_VOMITING, String.valueOf(patientToAdd.isVomiting()));
		values.put(PatientAssessmentTable.COLUMN_VOMITS_EVERYTHING, String.valueOf(patientToAdd.isVomitsEverything()));
		values.put(PatientAssessmentTable.COLUMN_RED_EYE, String.valueOf(patientToAdd.isRedEye()));
		values.put(PatientAssessmentTable.COLUMN_RED_EYE_DURATION, patientToAdd.getRedEyeDuration());	
		values.put(PatientAssessmentTable.COLUMN_DIFFICULTY_SEEING, String.valueOf(patientToAdd.isDifficultySeeing()));
		values.put(PatientAssessmentTable.COLUMN_DIFFICULTY_SEEING_DURATION, patientToAdd.getDifficultySeeingDuration());		
		values.put(PatientAssessmentTable.COLUMN_CANNOT_TREAT, String.valueOf(patientToAdd.isCannotTreatProblem()));
		values.put(PatientAssessmentTable.COLUMN_CANNOT_TREAT_DETAILS, patientToAdd.getCannotTreatProblemDetails());	
		values.put(PatientAssessmentTable.COLUMN_SYNCED, Boolean.valueOf(false).toString());	
		
	
		try {
			values.put(PatientAssessmentTable.COLUMN_BIRTH_DATE, 
					DateUtilities.formatDate(patientToAdd.getBirthDate(), DateUtilities.DATE_CUSTOM_FORMAT));	
			values.put(PatientAssessmentTable.COLUMN_VISIT_DATE, 
					DateUtilities.formatDate(patientToAdd.getVisitDate(), DateUtilities.DATE_TIME_CUSTOM_FORMAT));
		} catch (ParseException e) {
			LoggerUtils.i(LOG_TAG, "Parse Exception thrown whilst extracting patient assessment dates");
			e.printStackTrace();
		}
		
		
		// add the patient row
		long insertId = service.getDatabase().insert(PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT, null, values);
		
		LoggerUtils.i(LOG_TAG, "Patient Assessment Record Added: " + insertId);
		
		Cursor cursor = service.getDatabase().query(PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT, allColumns, 
										PatientAssessmentTable.COLUMN_ID + " = " + insertId, 
										null, null, null, null);
		cursor.moveToFirst();
		PatientAssessmentComms patientAssessmentComms = cursorToPatientAssessmentComms(cursor);
		cursor.close();
		
		// show the number of patient assessments in debug logger
		debugOutputShowPatientAssessmentCount(service);
		
		return patientAssessmentComms;
	}

	/**
	 * Show the number of patient assessments in debug logger
	 * 
	 * @param service
	 */
	private void debugOutputShowPatientAssessmentCount(SupportingLifeService service) {
		SQLiteStatement assessmentRowCountQuery = service.getDatabase().compileStatement("select count(*) from " + PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT);
		if (assessmentRowCountQuery != null) {
			long assessmentRowCount = assessmentRowCountQuery.simpleQueryForLong();

			LoggerUtils.i(LOG_TAG, "Current Patient Assessment Row Count: " + assessmentRowCount);
			assessmentRowCountQuery.close();
		}
	}

	@Override
	public void deletePatientAssessment(PatientAssessment patient, SupportingLifeService service) {
		long id = patient.getId();
		System.out.println("Patient deleted with id: " + id);
		service.getDatabase().delete(PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT, PatientAssessmentTable.COLUMN_ID
				+ " = " + id, null);
	}

	@Override
	public List<PatientAssessmentComms> getAllNonSyncedPatientAssessmentComms(SupportingLifeService service) {
		List<PatientAssessmentComms> patientComms = new ArrayList<PatientAssessmentComms>();

		Cursor cursor = service.getDatabase().query(PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT,
				allColumns, PatientAssessmentTable.COLUMN_SYNCED + " = '" + Boolean.valueOf(false) + "'", 
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PatientAssessmentComms patientComm = cursorToPatientAssessmentComms(cursor);
			patientComms.add(patientComm);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return patientComms;
	}
	
	@Override
	public List<PatientAssessmentComms> getAllPatientAssessments(SupportingLifeService service) {
		List<PatientAssessmentComms> patientComms = new ArrayList<PatientAssessmentComms>();

		Cursor cursor = service.getDatabase().query(PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT,
				allColumns, "", null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PatientAssessmentComms patientComm = cursorToPatientAssessmentComms(cursor);
			patientComms.add(patientComm);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return patientComms;
	}

	private PatientAssessmentComms cursorToPatientAssessmentComms(Cursor cursor) {

//		Param Line 1: 	Integer id (0), String deviceGeneratedAssessmentId (1), String nationalId (2), String nationalHealthId (3), String nationalHealthId (4),
//		Param Line 2: 	String childFirstName (5), String childSurname (6), String birthDate (7), Integer monthsAge (8),
//		Param Line 3: 	String gender (9), String caregiverName (10), String relationship (11), String physicalAddress (12),
//		Param Line 4: 	String village (13), String ta(14), String visitDate (15), String chestIndrawing (16), Integer breathsPerMinute (17),
//		Param Line 5: 	String sleepyUnconscious (18), String palmarPallor (19), String muacTapeColour (20), 
//		Param Line 6: 	String swellingBothFeet (21), String problem (22), String cough (23), Integer coughDuration (24),
//		Param Line 7: 	String diarrhoea (25), Integer diarrhoeaDuration (26), String bloodInStool (27), String fever (28),
//		Param Line 8: 	Integer feverDuration (29), String convulsions (30), String difficultyDrinkingOrFeeding (31),
//		Param Line 9: 	String unableToDrinkOrFeed (32), String vomiting (33), String vomitsEverything (34),
//		Param Line 10: 	String redEye (35), Integer redEyeDuration (36), String difficultySeeing (37),
//		Param Line 11:	Integer difficultySeeingDuration (38), String cannotTreatProblem (39), 
//		Param Line 12: 	String cannotTreatProblemDetails (40)
		
		PatientAssessmentComms patientAssessmentComms = new PatientAssessmentComms(DaoUtilities.readInt(cursor, 0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), 
													cursor.getString(5), cursor.getString(6), cursor.getString(7), DaoUtilities.readInt(cursor, 8),
													cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
													cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), DaoUtilities.readInt(cursor, 17),
													cursor.getString(18), cursor.getString(19), cursor.getString(20),
													cursor.getString(21), cursor.getString(22), cursor.getString(23), DaoUtilities.readInt(cursor, 24),
													cursor.getString(25), DaoUtilities.readInt(cursor, 26), cursor.getString(27), cursor.getString(28),
													DaoUtilities.readInt(cursor, 29), cursor.getString(30), cursor.getString(31),
													cursor.getString(32), cursor.getString(33), cursor.getString(34),
													cursor.getString(35), DaoUtilities.readInt(cursor, 36), cursor.getString(37),
													DaoUtilities.readInt(cursor, 38), cursor.getString(39),
													cursor.getString(40));
		
		return patientAssessmentComms;
	}

	public int setPatientAssessmentToSynced(String deviceGeneratedAssessmentId, SupportingLifeService service) {
		
		ContentValues values = new ContentValues();
		values.put(PatientAssessmentTable.COLUMN_SYNCED, "true");
		
		int rowCount = service.getDatabase().update(PatientAssessmentTable.TABLE_PATIENT_ASSESSMENT, values, 
				PatientAssessmentTable.COLUMN_ASSESSMENT_ID + " = '" + deviceGeneratedAssessmentId + "'", 
				null);
		
		return rowCount;
	}
} 
