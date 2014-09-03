package ie.ucc.bis.supportinglife.dao;

import ie.ucc.bis.supportinglife.communication.PatientAssessmentComms;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.service.SupportingLifeService;

import java.util.List;

/**
 * Class: SensorVitalSignsDao
 * 
 * This class maintains the database connection and supports 
 * adding new vital sign sensor readings related to an assessment and 
 * fetching vital sign sensor readings pertaining to a patient assessment.
 * 
 * @author TOSullivan
 */
public interface SensorVitalSignsDao {
	
	public void createSensorVitalSigns(PatientAssessment assessmentToAdd, String uniquePatientAssessmentIdentifier, SupportingLifeService service);

	public void populateSensorVitalSigns(List<PatientAssessmentComms> patientAssessmentComms, SupportingLifeService service);
}
