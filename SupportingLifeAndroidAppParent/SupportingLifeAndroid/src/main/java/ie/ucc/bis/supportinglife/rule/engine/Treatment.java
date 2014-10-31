package ie.ucc.bis.supportinglife.rule.engine;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Class: Treatment
 * 
 * Encapsulates a Treatment as defined by 
 * treatment_rules.xml e.g.
 * 
 * <Treatment>
 * 		<CriteriaList rule="any">
 * 			<SymptomCriteria value="some">malnutrition_assessment_palmar_pallor_symptom_id</SymptomCriteria>
 * 			<SymptomCriteria value="severe">malnutrition_assessment_palmar_pallor_symptom_id</SymptomCriteria>
 * 		</CriteriaList>
 * 		<CriteriaList rule="all">
 * 			<SymptomCriteria value="high malaria risk">fever_assessment_malaria_risk_symptom_id</SymptomCriteria>
 * 		</CriteriaList>
 * 		<Recommendation>Give Oral Antimalarial</Recommendation>
 * </Treatment>
 * 
 * @author TOSullivan
 *
 */

public class Treatment implements Serializable {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 2969585100135738023L;

	private String identifier;
	private List<TreatmentCriterion> treatmentCriterion;
	private String recommendation;
	private boolean drugAdministered;
 
	/**
	 * Constructor
	 */
	public Treatment() {
		setTreatmentCriterion(new ArrayList<TreatmentCriterion>());
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<TreatmentCriterion> getTreatmentCriterion() {
		return treatmentCriterion;
	}

	public void setTreatmentCriterion(List<TreatmentCriterion> treatmentCriterion) {
		this.treatmentCriterion = treatmentCriterion;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public boolean isDrugAdministered() {
		return drugAdministered;
	}

	public void setDrugAdministered(boolean drugAdministered) {
		this.drugAdministered = drugAdministered;
	}

	/**
	 * Provides debug output of a treatment
	 */
	public String debugOutput() {
		StringBuilder debugOutput = new StringBuilder();

		debugOutput.append("------------------------------------ \n");

		for (TreatmentCriterion treatmentCriterion : getTreatmentCriterion()) {
			debugOutput.append(treatmentCriterion.debugOutput());
		}

		debugOutput.append("Recommendation: " + getRecommendation() + "\n");
		debugOutput.append("Drug Administered: " + isDrugAdministered() + "\n");
		
		return debugOutput.toString();
	}

}
