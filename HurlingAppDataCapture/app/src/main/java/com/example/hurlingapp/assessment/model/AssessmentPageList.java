package com.example.hurlingapp.assessment.model;

import java.util.ArrayList;

public class AssessmentPageList extends ArrayList<AbstractAssessmentPage> implements AssessmentPageTreeNode {
	private static final long serialVersionUID = 1934134441792428239L;

	public AssessmentPageList(AbstractAssessmentPage... pages) {
        for (AbstractAssessmentPage page : pages) {
            add(page);
        }
    }

    public AbstractAssessmentPage findAssessmentPageByKey(String key) {
        for (AbstractAssessmentPage childPage : this) {
        	AbstractAssessmentPage found = childPage.findAssessmentPageByKey(key);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public void flattenCurrentPageSequence(ArrayList<AbstractAssessmentPage> pages) {
        for (AbstractAssessmentPage childPage : this) {
            childPage.flattenCurrentPageSequence(pages);
        }
    }
}
