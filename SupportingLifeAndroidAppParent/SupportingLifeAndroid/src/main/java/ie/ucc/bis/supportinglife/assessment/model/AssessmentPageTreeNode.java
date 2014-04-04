package ie.ucc.bis.supportinglife.assessment.model;

import java.util.ArrayList;

/**
 * Represents a node in the page tree. 
 * Can either be a single page, or a page container.
 * 
 * @author timothyosullivan
 */
public interface AssessmentPageTreeNode {
    public void flattenCurrentPageSequence(ArrayList<AbstractAssessmentPage> dest);
    public AbstractAssessmentPage findAssessmentPageByKey(String key);
}
