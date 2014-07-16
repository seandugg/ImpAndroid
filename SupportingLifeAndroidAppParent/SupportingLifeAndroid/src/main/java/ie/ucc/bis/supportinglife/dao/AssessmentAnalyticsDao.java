package ie.ucc.bis.supportinglife.dao;

import java.util.List;

import android.location.Location;

import ie.ucc.bis.supportinglife.communication.PatientAssessmentComms;
import ie.ucc.bis.supportinglife.domain.PatientAssessment;
import ie.ucc.bis.supportinglife.service.SupportingLifeService;

/**
 * Class: AssessmentAnalyticsDao
 * 
 * This class maintains the database connection and supports 
 * adding new analytics related to an assessment and 
 * fetching analytic details pertaining to a patient assessment.
 * 
 * @author TOSullivan
 */
public interface AssessmentAnalyticsDao {
	
	public void createAssessmentAnalytics(PatientAssessment assessmentToAdd, String uniquePatientAssessmentIdentifier, Location locat, SupportingLifeService service);

	public void populateAssessmentAnalytics(List<PatientAssessmentComms> patientAssessmentComms, SupportingLifeService service);
}
