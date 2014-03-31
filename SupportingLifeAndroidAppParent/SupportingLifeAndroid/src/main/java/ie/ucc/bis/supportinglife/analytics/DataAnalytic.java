package ie.ucc.bis.supportinglife.analytics;

import java.io.Serializable;

/**
 * Represents a single data analytic
 * 
 * @author timothyosullivan
 */
public class DataAnalytic implements Serializable {
	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 6521906653364293613L;

	// Defines the event category. Example event categories could be
	// based on the class of user actions e.g. click 
	private String category;
	// Defines the specific event action within the category specified.
	// e.g. button click
	private String action;
	// Defines a label associated with the event e.g. 'previous button'
	private String label;
	// Defines a numeric value associated with the event 
	private Long value;
	
    /**
     * Constructor
     * 
     * @param category
     * @param action
     * @param label
     * @param value
     * 
     */
    public DataAnalytic(String category, String action, String label, Long value, boolean authoriseUpload) {
        setCategory(category);
        setAction(action);
        setLabel(label);
        setValue(value);
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
}
