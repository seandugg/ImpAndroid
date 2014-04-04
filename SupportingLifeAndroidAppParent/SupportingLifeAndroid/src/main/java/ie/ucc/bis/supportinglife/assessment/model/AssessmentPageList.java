package ie.ucc.bis.supportinglife.assessment.model;

import java.util.ArrayList;

/**
 * Represents a list (ArrayList) of bread-crumb UI Wizard assessment pages.
 * 
 * @author timothyosullivan
 */
public class AssessmentPageList extends ArrayList<AbstractAssessmentPage> implements AssessmentPageTreeNode {
	private static final long serialVersionUID = 1934134441792428239L;

	/**
	 * Constructor
	 * 
	 * @param pages : AnalyticsPage...
	 */	
	public AssessmentPageList(AbstractAssessmentPage... pages) {
        for (AbstractAssessmentPage page : pages) {
            add(page);
        }
    }

	/**
	 * Method: findPageByKey
	 * 
	 * Utility method to retrieve a bread-crumb UI Wizard
	 * page based on the key
	 * 
	 * @param key : String
	 * @return AnalyticsPage
	 * 
	 */	
    public AbstractAssessmentPage findAssessmentPageByKey(String key) {
        for (AbstractAssessmentPage childPage : this) {
        	AbstractAssessmentPage found = childPage.findAssessmentPageByKey(key);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

	/**
	 * Method: flattenCurrentPageSequence
	 * 
	 * Utility method to retrieve the current list of wizard steps.
	 * Nested (dependent) pages will be be flattened based on the
	 * user's choices.
	 * 
	 * @param pages : ArrayList<AnalyticsPage>
	 * 
	 */	
    public void flattenCurrentPageSequence(ArrayList<AbstractAssessmentPage> pages) {
        for (AbstractAssessmentPage childPage : this) {
            childPage.flattenCurrentPageSequence(pages);
        }
    }
}
