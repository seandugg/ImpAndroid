package ie.ucc.bis.supportinglife.rule.engine;

import java.io.Serializable;

/**
 * This class encapsulates a treatment recommendation and will 
 * be utilised within the diagnostic assessment results of a
 * patient.
 * 
 * @author timothyosullivan
 */
public class TreatmentRecommendation implements Serializable {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = -7471295692235777893L;

	private String treatmentIdentifier;
	private String treatmentDescription;
	private boolean drugAdministered;
	
	/**
	 * Constructor
	 */
	public TreatmentRecommendation() {}

	/**
	 * Constructor
	 * 
	 * @param treatmentIdentifier
	 * @param treatmentDescription
	 * @param drugAdministered 
	 */
	public TreatmentRecommendation(String treatmentIdentifier, String treatmentDescription, boolean drugAdministered) {
		setTreatmentIdentifier(treatmentIdentifier);
		setTreatmentDescription(treatmentDescription);
		setDrugAdministered(drugAdministered);
	}

	/**
	 * Getter Method: getTreatmentIdentifier()
	 */
	public String getTreatmentIdentifier() {
		return treatmentIdentifier;
	}

	/**
	 * Setter Method: setTreatmentIdentifier()
	 */
	public void setTreatmentIdentifier(String treatmentIdentifier) {
		this.treatmentIdentifier = treatmentIdentifier;
	}

	/**
	 * Getter Method: getTreatmentDescription()
	 */
	public String getTreatmentDescription() {
		return treatmentDescription;
	}

	/**
	 * Setter Method: setTreatmentDescription()
	 */
	public void setTreatmentDescription(String treatmentDescription) {
		this.treatmentDescription = treatmentDescription;
	}

	public boolean isDrugAdministered() {
		return drugAdministered;
	}

	public void setDrugAdministered(boolean drugAdministered) {
		this.drugAdministered = drugAdministered;
	}

}
