package ie.ucc.bis.supportinglife.helper;

import ie.ucc.bis.supportinglife.R;
import ie.ucc.bis.supportinglife.assessment.model.review.ReviewItem;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.ui.utilities.DateUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.res.Resources;

public class PatientHandlerUtils {

	private static final String POSITIVE_ANSWER = "Yes";
	private static final Locale LOCALE = Locale.UK;
	
	/**
	 * Handler method to populate a patient instance with the following information:
	 * 
	 * - General Patient Details (user entered)
	 * - Look Symptoms (user entered)
	 * - Ask & Look Symptoms (user entered)
	 * - Classifications (system generated)
	 * - Treatments (system generated)
	 * 
	 * @param resources
	 * @param reviewItems
	 * 
	 * @return Patient
	 * @throws ParseException 
	 */
	public PatientAssessment populateCcmPatientDetails(Resources resources, List<ReviewItem> reviewItems) throws ParseException {
		PatientAssessment patient = new PatientAssessment();

		// firstly create hash map of review items for quick look-up and
		// subsequent population of patient instance
		Map<String, String> reviewItemMap = new HashMap<String, String>();
		for (ReviewItem reviewItem : reviewItems) {
			if (reviewItem.getIdentifier() != null) { 
				reviewItemMap.put(reviewItem.getIdentifier(), reviewItem.getDisplayValue());
			}
		}

		retrieveGeneralPatientDetails(patient, resources, reviewItemMap);
		retrieveLookSymptoms(patient, resources, reviewItemMap);
		retrieveAskLookSymptoms(patient, resources, reviewItemMap);
		retrieveAssessmentSensorReadings(patient, resources, reviewItemMap);
		retrieveAssessmentSpecificAnalytics(patient, resources, reviewItemMap);

		return patient;
	}


	/**
	 * Method to populate a patient instance with the following:
	 * 
	 * - General Patient Details (user entered)
	 * 
	 * @param patient
	 * @param resources
	 * @param reviewItems
	 * 
	 * @throws ParseException 
	 */
	private void retrieveGeneralPatientDetails(PatientAssessment patient, Resources resources, Map<String, String> reviewItemMap) throws ParseException {
		
		// hsa user id
		patient.setHsaUserId(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_hsa_user_id))));
		
		// national id
		patient.setNationalId(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_national_id))));

		// national health id
		patient.setNationalHealthId(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_national_health_id))));
		
		// child first name
		patient.setChildFirstName(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_child_first_name_id))));

		// child surname
		patient.setChildSurname(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_child_surname_id))));

		// birthdate
		patient.setBirthDate(assessDatePatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_date_of_birth_id)),
				DateUtilities.DATE_CUSTOM_FORMAT));
		
		patient.setMonthsAge(determineAgeInMonths(patient.getBirthDate()));

		// gender
		patient.setGender(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_gender_id))));

		// caregiver name
		patient.setCaregiverName(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_caregiver_name_id))));

		// relationship
		patient.setRelationship(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_relationship_id))));

		// physical address
		patient.setPhysicalAddress(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_physical_address_id))));

		// village
		patient.setVillage(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_village_id))));
		
		// TA
		patient.setTa(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_general_patient_details_ta_id))));

		// visit date
		patient.setVisitDate(DateUtilities.getTodaysDateTimestamp());
	}

	/**
	 * Utility method to check for null and convert string to upper case
	 * 
	 * @param value to be converted to upper case
	 * 
	 * @return upper case representation of parameter value
	 */
	private String upperCaseConversion(String value) {
		if (value != null) {
			return value.toUpperCase(LOCALE);
		}
		else {
		return null;
		}
	}

	/**
	 * Method to populate a patient instance with the following:
	 * 
	 * - Look Symptoms (user entered)
	 * 
	 * @param patient
	 * @param resources
	 * @param reviewItems
	 * 
	 * @throws ParseException 
	 */
	private void retrieveLookSymptoms(PatientAssessment patient, Resources resources, Map<String, String> reviewItemMap) throws ParseException {

		// chest indrawing
		patient.setChestIndrawing(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_chest_indrawing_id))));

		// breaths per minute
		patient.setBreathsPerMinute(assessIntegerPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_breaths_per_minute_id))));

		// sleepy unconscious
		patient.setSleepyUnconscious(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_very_sleepy_or_unconscious_id))));

		// palmar pallor
		patient.setPalmarPallor(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_palmar_pallor_id))));

		// muac tape colour
		patient.setMuacTapeColour(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_muac_tape_colour_id))));

		// swelling of both feet
		patient.setSwellingBothFeet(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_swelling_of_both_feet_id))));
	}
	
	/**
	 * Method to populate a patient instance with the following:
	 * 
	 * - Ask & Look Symptoms (user entered)
	 * 
	 * @param patient
	 * @param resources
	 * @param reviewItems
	 * 
	 * @throws ParseException 
	 */
	private void retrieveAskLookSymptoms(PatientAssessment patient, Resources resources, Map<String, String> reviewItemMap) throws ParseException {

		// problem
		patient.setProblem(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_problems_id))));
		
		// cough
		patient.setCough(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_cough_id))));

		// cough duration
		patient.setCoughDuration(assessIntegerPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_cough_duration_id))));

		// diarrhoea
		patient.setDiarrhoea(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_diarrhoea_id))));

		// diarrhoea duration
		patient.setDiarrhoeaDuration(assessIntegerPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_diarrhoea_duration_id))));

		// blood in stool
		patient.setBloodInStool(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_blood_in_stool_id))));

		// fever
		patient.setFever(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_fever_id))));

		// fever duration
		patient.setFeverDuration(assessIntegerPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_fever_duration_id))));

		// convulsions
		patient.setConvulsions(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_convulsions_id))));

		// difficulity drinking or feeding
		patient.setDifficultyDrinkingOrFeeding(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_drink_or_feed_difficulty_id))));

		// unable to drink or feed
		patient.setUnableToDrinkOrFeed(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_initial_assessment_unable_to_drink_or_feed_id))));

		// vomiting
		patient.setVomiting(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_vomiting_id))));

		// vomits everything
		patient.setVomitsEverything(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_vomits_everything_id))));

		// red eye
		patient.setRedEye(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_red_eye_id))));

		// red eye duration
		patient.setRedEyeDuration(assessIntegerPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_red_eye_duration_id))));
		
		// difficulty seeing
		patient.setDifficultySeeing(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_id))));

		// difficulty seeing duration
		patient.setDifficultySeeingDuration(assessIntegerPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_seeing_difficulty_duration_id))));
		
		// cannot treat problems
		patient.setCannotTreatProblem(assessBooleanPatientSymptom(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_id))));

		// 'cannot treat problems' details
		patient.setCannotTreatProblemDetails(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_ask_secondary_assessment_cannot_treat_problems_details_id))));
	}
	
	/**
	 * Method to populate a patient instance with the following:
	 * 
	 * - Sensor Vital Sign Reading
	 * 
	 * @param patient
	 * @param resources
	 * @param reviewItems
	 * 
	 * @throws ParseException 
	 */
	private void retrieveAssessmentSensorReadings(PatientAssessment patient, Resources resources, Map<String, String> reviewItemMap) {
		// sensor heart rate
		patient.setSensorHeartRate(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_sensor_assessment_heart_rate_id))));
		// sensor respiration rate
		patient.setSensorRespiratoryRate(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_sensor_assessment_respiration_rate_id))));
		// sensor body temperature
		patient.setSensorBodyTemperature(upperCaseConversion(reviewItemMap.get(resources.getString(R.string.ccm_sensor_assessment_skin_temperature_id))));
	}

	/**
	 * Method to populate a patient instance with the following:
	 * 
	 * - Assessment Specific Analytics
	 * 
	 * @param patient
	 * @param resources
	 * @param reviewItems
	 * 
	 * @throws ParseException 
	 */
	private void retrieveAssessmentSpecificAnalytics(PatientAssessment patient,	Resources resources, Map<String, String> reviewItemMap) {
		// breath counter used
		patient.setBreathCounterUsed(Boolean.valueOf(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_breath_counter_used_id))));
		
		// breath count - full time assessment employed
		patient.setBreathFullTimeAssessment(Boolean.valueOf(reviewItemMap.get(resources.getString(R.string.ccm_look_assessment_breath_full_time_assessment_id))));
	}
	
	/**
	 * Assess whether boolean symptom review item is true or false
	 * 
	 * @param string
	 * 
	 * @return boolean
	 */
	private boolean assessBooleanPatientSymptom(String symptomValue) {
		if (symptomValue != null && symptomValue.equalsIgnoreCase(POSITIVE_ANSWER)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Assess whether date symptom review item is null and 
	 * also correctly format returned date
	 * 
	 * @param string
	 * 
	 * @return Date
	 * @throws ParseException 
	 */
	public Date assessDatePatientSymptom(String dateValue, String slDateFormat) throws ParseException {
		if (dateValue != null) {
			Date dateInstance = new SimpleDateFormat(slDateFormat, LOCALE)
										.parse(dateValue);
			return dateInstance;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Determines age of patient in month
	 * 
	 * @param Data
	 * 
	 * @return Integer
	 */	
	private Integer determineAgeInMonths(Date birthDate) {
		Integer months = null;

		if (birthDate != null) {
			Calendar cal = Calendar.getInstance();
			months = DateUtilities.getDiffMonths(birthDate, cal.getTime());
		}
		return months;
	}

	
	/**
	 * Assess whether a symptom review item is null and 
	 * if not returns return integer related value
	 * 
	 * @param integerValue
	 * 
	 * @return Integer
	 */
	private Integer assessIntegerPatientSymptom(String integerValue) {
		Integer patientSymptom = null;
		if (integerValue != null && !("".equalsIgnoreCase(integerValue))) {
			patientSymptom = Integer.valueOf(integerValue);
		}
		return patientSymptom;
	}

	/**
	 * Utility method to remove escape characters from
	 * string content e.g. for tidying up treatment descriptions
	 * before transferring to server
	 * 
	 * @param text
	 * 
	 * @return String (with escape characters removed)
	 */
	public static String removeEscapeCharacters(String text) {
		// remove newline characters
		String formatString = text.replace("\\n", "").replace("\n", "");
		// remove multiple spaces between words
		formatString = formatString.replaceAll("\\s+", " ");
		
		return formatString;		
	}
	
}