package com.example.hurlingapp.assessment.model;

import java.util.ArrayList;

public interface AssessmentPageTreeNode {
    public void flattenCurrentPageSequence(ArrayList<AbstractAssessmentPage> dest);
    public AbstractAssessmentPage findAssessmentPageByKey(String key);
}
